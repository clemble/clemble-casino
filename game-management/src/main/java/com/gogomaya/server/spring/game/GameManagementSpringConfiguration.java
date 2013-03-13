package com.gogomaya.server.spring.game;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.PlatformTransactionManager;

import com.gogomaya.server.game.match.GameMatchingService;
import com.gogomaya.server.game.session.GameSessionRepository;

@Configuration
@Import(value = { GameManagementRepositoriesSpringConfiguration.class })
public class GameManagementSpringConfiguration {

    @Inject
    private DataSource dataSource;

    @Inject
    private JpaVendorAdapter jpaVendorAdapter;

    @Inject
    public RedisTemplate<byte[], Long> sessionTemplate;

//    @Inject
    public GameSessionRepository sessionRepository;

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
        entityManagerFactory.setPackagesToScan(new String[] { "com.gogomaya.server.game" });
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

//    @Bean
//    @Singleton
    public GameMatchingService gameMatchingService() {
        return new GameMatchingService(sessionTemplate, sessionRepository);
    }

}
