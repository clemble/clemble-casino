package com.gogomaya.server.spring.game;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gogomaya.server.spring.common.CommonModuleSpringConfiguration;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gogomaya.server.game.session")
@ComponentScan(basePackages = "com.gogomaya.server.game.session")
@Import(value = { CommonModuleSpringConfiguration.class })
public class GameManagementRepositoriesSpringConfiguration {

}
