package com.gogomaya.server.spring.player;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.service.AbstractServiceCreator.ServiceNameTuple;
import org.cloudfoundry.runtime.service.relational.CloudDataSourceFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.HibernatePersistence;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.wallet.PlayerWallet;
import com.gogomaya.server.spring.common.CommonModuleSpringConfiguration;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import static com.gogomaya.server.spring.player.PlayerManagementSpringConfiguration.*;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gogomaya.server.player")
@ComponentScan(basePackages = "com.gogomaya.server.player")
@Import(value = { CommonModuleSpringConfiguration.class, PlayerManagementCloudFoundryConfigurations.class, PlayerManagementDefaultConfigurations.class,
        PlayerManagementTestConfigurations.class })
public class PlayerManagementSpringConfiguration {

    @Inject
    private DataSource dataSource;

    @Inject
    private JpaVendorAdapter jpaVendorAdapter;

    @Inject
    public JpaDialect jpaDialect() {
        return new HibernateJpaDialect();
    }

    @Bean(name = "transactionManager")
    @Singleton
    public PlatformTransactionManager annotationDrivenTransactionManager() {
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
        entityManagerFactory.setPackagesToScan(new String[] { "com.gogomaya.server.player" });
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
    static class PlayerManagementCloudFoundryConfigurations {
        @Inject
        CloudEnvironment cloudEnvironment;

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
            CloudDataSourceFactory cloudDataSourceFactory = new CloudDataSourceFactory(cloudEnvironment);
            try {
                Collection<ServiceNameTuple<DataSource>> dataSources = cloudDataSourceFactory.createInstances();
                dataSources = Collections2.filter(dataSources, new Predicate<ServiceNameTuple<DataSource>>() {
                    public boolean apply(ServiceNameTuple<DataSource> input) {
                        return input.name.equalsIgnoreCase("gogomaya-mysql");
                    }
                });
                assert dataSources.size() == 1 : "Returned illegal DataSource";
                return dataSources.iterator().next().service;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Configuration
    @Profile(value = "default")
    static class PlayerManagementDefaultConfigurations {
        @Bean
        @Singleton
        public JpaVendorAdapter jpaVendorAdapter() {
            HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
            hibernateJpaVendorAdapter.setDatabase(Database.H2);
            hibernateJpaVendorAdapter.setShowSql(true);
            return hibernateJpaVendorAdapter;
        }

        @Bean
        @Singleton
        public DataSource dataSource() {
            EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
            factory.setDatabaseName("gogomaya");
            factory.setDatabaseType(EmbeddedDatabaseType.H2);
            return factory.getDatabase();
        }
    }

    @Configuration
    @Profile(value = "test")
    static class PlayerManagementTestConfigurations {

        @PostConstruct
        public void createSchemaFile() {
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

            configuration
            .addAnnotatedClass(PlayerProfile.class)
            .addAnnotatedClass(PlayerCredential.class)
            .addAnnotatedClass(PlayerIdentity.class)
            .addAnnotatedClass(PlayerWallet.class)
            .addAnnotatedClass(GameSpecification.class)
            .setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect")
            .setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");

            new SchemaExport(configuration).setDelimiter(";").setFormat(true).setOutputFile("src/main/resources/sql/schema.sql").create(true, false);
        }

        @Bean
        @Singleton
        public JpaVendorAdapter jpaVendorAdapter() {
            HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
            hibernateJpaVendorAdapter.setDatabase(Database.H2);
            hibernateJpaVendorAdapter.setShowSql(true);
            hibernateJpaVendorAdapter.setGenerateDdl(true);
            return hibernateJpaVendorAdapter;
        }

        @Bean
        @Singleton
        public DataSource dataSource() {
            EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
            factory.setDatabaseName("gogomaya");
            factory.setDatabaseType(EmbeddedDatabaseType.H2);
            return factory.getDatabase();
        }

    }

}
