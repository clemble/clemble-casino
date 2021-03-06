package com.clemble.casino.server.game.spring;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.PlatformTransactionManager;

import com.clemble.casino.server.spring.common.BasicJPASpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;

@Configuration
@Import(BasicJPASpringConfiguration.class)
@EnableJpaRepositories(basePackages = "com.clemble.casino.server.game.repository",
    entityManagerFactoryRef = "gameEntityManagerFactory",
    transactionManagerRef = "gameTransactionManager",
    includeFilters = { @Filter(value = JpaRepository.class, type = FilterType.ASSIGNABLE_TYPE) })
public class GameJPASpringConfiguration implements SpringConfiguration {

    @Bean
    public PlatformTransactionManager gameTransactionManager(DataSource dataSource, EntityManagerFactory gameEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(gameEntityManagerFactory);
        transactionManager.setJpaDialect(new HibernateJpaDialect());
        transactionManager.setDataSource(dataSource);
        transactionManager.setPersistenceUnitName("gameEntityManager");
        return transactionManager;
    }

    @Bean
    public EntityManagerFactory gameEntityManagerFactory(JpaVendorAdapter jpaVendorAdapter, DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("com.clemble.casino.game");
        entityManagerFactory.setPersistenceProvider(new HibernatePersistenceProvider());
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setPersistenceUnitName("gameEntityManager");
        entityManagerFactory.afterPropertiesSet();
        return entityManagerFactory.getObject();
    }

}
