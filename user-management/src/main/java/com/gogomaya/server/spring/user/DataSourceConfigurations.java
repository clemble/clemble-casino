package com.gogomaya.server.spring.user;

import java.beans.PropertyVetoException;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.service.AbstractServiceCreator.ServiceNameTuple;
import org.cloudfoundry.runtime.service.relational.CloudDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@Import(value = { DataSourceConfigurations.CloudFoundryConfigurations.class, DataSourceConfigurations.DefaultConfigurations.class, DataSourceConfigurations.TestConfigurations.class })
public class DataSourceConfigurations {

    @Configuration
    @Profile(value = "cloud")
    static class CloudFoundryConfigurations {
        @Inject
        private CloudEnvironment cloudEnvironment;

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
    static class DefaultConfigurations {

        @Bean
        @Singleton
        public DataSource dataSource() throws PropertyVetoException {
            ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
            comboPooledDataSource.setDriverClass("com.mysql.jdbc.Driver");
            comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/gogomaya");
            comboPooledDataSource.setUser("gogomaya");
            comboPooledDataSource.setPassword("gogomaya");
            comboPooledDataSource.setInitialPoolSize(50);
            comboPooledDataSource.setMaxPoolSize(50);
            comboPooledDataSource.setCheckoutTimeout(18000);
            return comboPooledDataSource;
        }

    }

    @Configuration
    @Profile(value = "test")
    static class TestConfigurations {

        @Bean
        @Singleton
        public DataSource dataSource() {
            EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
            factory.setDatabaseName("gogomaya");
            factory.setDatabaseType(EmbeddedDatabaseType.H2);
            factory.setDatabasePopulator(databasePopulator());
            return factory.getDatabase();
        }

        // Populates DB with appropriate MySQL Schema
        private DatabasePopulator databasePopulator() {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            return populator;
        }
    }

}
