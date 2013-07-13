package com.gogomaya.server.spring.common;

import java.io.File;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.RdbmsServiceInfo;
import org.cloudfoundry.runtime.service.relational.RdbmsServiceCreator;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.gogomaya.server.repository", entityManagerFactoryRef = "entityManagerFactory")
@Import(value = { JPASpringConfiguration.Cloud.class, JPASpringConfiguration.DefaultAndTest.class })
public class JPASpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("dataSource")
    public DataSource dataSource;

    @Autowired
    @Qualifier("jpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter;

    @Bean
    @Singleton
    public JpaDialect jpaDialect() {
        return new HibernateJpaDialect();
    }

    @Bean
    @Singleton
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory());
        transactionManager.setJpaDialect(jpaDialect());
        transactionManager.setDataSource(dataSource);
        transactionManager.setPersistenceUnitName("entityManager");
        return transactionManager;
    }

    @Bean
    @Singleton
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan(new String[] { "com.gogomaya.server" });
        entityManagerFactory.setPersistenceProvider(new HibernatePersistence());
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setPersistenceUnitName("entityManager");
        entityManagerFactory.afterPropertiesSet();
        return entityManagerFactory.getObject();
    }

    @Bean
    @Singleton
    public PersistenceExceptionTranslator persistenceExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Configuration
    @Profile(value = "cloud")
    public static class Cloud {

        @Autowired
        public CloudEnvironment cloudEnvironment;

        @Bean
        @Singleton
        public JpaVendorAdapter jpaVendorAdapter() {
            HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
            hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
            hibernateJpaVendorAdapter.setShowSql(false);
            return hibernateJpaVendorAdapter;
        }

        @Bean
        @Singleton
        public DataSource dataSource() {
            try {
                RdbmsServiceInfo serviceInfo = cloudEnvironment.getServiceInfo("gogomaya-mysql", RdbmsServiceInfo.class);
                RdbmsServiceCreator serviceCreator = new RdbmsServiceCreator();
                DataSource dataSource = serviceCreator.createService(serviceInfo);
                if (dataSource == null)
                    throw new NullPointerException("Data source can't be created");
                return dataSource;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Configuration
    @Profile(value = { "default", "test" })
    public static class DefaultAndTest {

        @Bean
        @Singleton
        public JpaVendorAdapter jpaVendorAdapter() {
            HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
            hibernateJpaVendorAdapter.setDatabase(Database.H2);
            hibernateJpaVendorAdapter.setGenerateDdl(false);
            hibernateJpaVendorAdapter.setShowSql(true);
            return hibernateJpaVendorAdapter;
        }

        @Bean
        @Singleton
        public EmbeddedDatabaseFactory dataSourceFactory() {
            EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
            factory.setDatabaseName("gogomaya");
            factory.setDatabaseType(EmbeddedDatabaseType.H2);
            factory.setDatabasePopulator(databasePopulator());
            return factory;
        }

        @Bean
        @Singleton
        public DataSource dataSource() {
            return dataSourceFactory().getDatabase();
        }

        @Bean
        @Singleton
        public ResourceDatabasePopulator databasePopulator() {
            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
            // Step 1. Searching for data script folder
            File dataScripts = new File("sql/data/");
            while (!dataScripts.exists()) {
                dataScripts = new File("../" + dataScripts.getPath());
            }
            // Step 2. Inserting schema files
            for (File schemaFile : new File(dataScripts.getAbsolutePath() + "/../schema/h2").listFiles()) {
                databasePopulator.addScript(new FileSystemResource(schemaFile));
            }
            // Step 3. Inserting data files
            for (File schemaFile : dataScripts.listFiles()) {
                databasePopulator.addScript(new FileSystemResource(schemaFile));
            }
            // Step 4. Returning aggregated script
            return databasePopulator;
        }
    }

}
