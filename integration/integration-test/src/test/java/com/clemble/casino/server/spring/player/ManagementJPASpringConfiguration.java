package com.clemble.casino.server.spring.player;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
@EnableJpaRepositories(basePackages = "com.clemble.casino.server.repository.player",
    entityManagerFactoryRef = "managementEntityManagerFactory",
    transactionManagerRef = "managementTransactionManager",
    includeFilters = { @Filter(value = JpaRepository.class, type = FilterType.ASSIGNABLE_TYPE) })
public class ManagementJPASpringConfiguration implements SpringConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Bean
    public PlatformTransactionManager managementTransactionManager(EntityManagerFactory managementEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(managementEntityManagerFactory);
        transactionManager.setJpaDialect(new HibernateJpaDialect());
        transactionManager.setDataSource(dataSource);
        transactionManager.setPersistenceUnitName("playerEntityManager");
        return transactionManager;
    }

    @Bean
    public EntityManagerFactory managementEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("com.clemble.casino.player");
        entityManagerFactory.setPersistenceProvider(new HibernatePersistenceProvider());
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setPersistenceUnitName("playerEntityManager");
        entityManagerFactory.afterPropertiesSet();
        return entityManagerFactory.getObject();
    }
}
