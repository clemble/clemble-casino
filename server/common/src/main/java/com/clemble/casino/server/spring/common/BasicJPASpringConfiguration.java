package com.clemble.casino.server.spring.common;

import java.io.IOException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.clemble.casino.server.error.ClembleConstraintExceptionResolver;

@Configuration
@Import(value = { BasicJPASpringConfiguration.DefaultAndTest.class })
public class BasicJPASpringConfiguration implements SpringConfiguration {

    final private static Logger LOG = LoggerFactory.getLogger(BasicJPASpringConfiguration.class);
    
    @Autowired
    @Qualifier("dataSource")
    public DataSource dataSource;

    @Autowired
    @Qualifier("jpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter;

    @Bean
    public PersistenceExceptionTranslator persistenceExceptionTranslator() {
        return new ClembleConstraintExceptionResolver();
    }

    @Configuration
    @Profile(value = { DEFAULT, UNIT_TEST, INTEGRATION_TEST, CLOUD})
    public static class DefaultAndTest implements ApplicationContextAware {

        private ApplicationContext applicationContext;

        @Bean
        public JpaVendorAdapter jpaVendorAdapter() {
            HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
            hibernateJpaVendorAdapter.setDatabase(Database.H2);
            hibernateJpaVendorAdapter.setGenerateDdl(false);
            hibernateJpaVendorAdapter.setShowSql(true);
            return hibernateJpaVendorAdapter;
        }

        @Bean
        public DataSource dataSource() throws IOException {
            // Step 0. Creating embedded database
            EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
            factory.setDatabaseName("clemble_casino");
            factory.setDatabaseType(EmbeddedDatabaseType.H2);
            // Step 1. Initializing database populator
            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
            // Step 2. Searching for schema script files
            for (Resource schema : applicationContext.getResources("classpath*:/sql/schema/h2/*.sql")) {
                LOG.debug("Adding schema {}", schema);
                databasePopulator.addScript(schema);
            }
            // Step 3. Searching for data script files, adding order to support test override of default values
            for (Resource data : applicationContext.getResources("classpath*:/sql/data/*.sql")) {
                LOG.debug("Adding script {}", data);
                databasePopulator.addScript(data);
            }
            // Step 4. Specifying aggregated script as part of Factory
            factory.setDatabasePopulator(databasePopulator);
            return factory.getDatabase();
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }
    }

}
