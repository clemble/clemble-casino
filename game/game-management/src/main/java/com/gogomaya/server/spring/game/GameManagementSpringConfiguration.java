package com.gogomaya.server.spring.game;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gogomaya.server.game.connection.GameServerConnectionManager;
import com.gogomaya.server.game.connection.SimpleGameServerConnectionManager;
import com.gogomaya.server.game.table.GameTableQueue;
import com.gogomaya.server.game.table.JavaGameTableQueue;
import com.gogomaya.server.game.table.RedisGameTableQueue;
import com.gogomaya.server.spring.common.CommonModuleSpringConfiguration;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gogomaya.server.game", entityManagerFactoryRef = "entityManagerFactory")
@ComponentScan(basePackages = "com.gogomaya.server.game")
@Import(value = { CommonModuleSpringConfiguration.class, GameManagementSpringConfiguration.GameManagementTestConfiguration.class,
        GameManagementSpringConfiguration.GameManagementCloudConfiguration.class })
public class GameManagementSpringConfiguration {

    @Profile(value = { "default", "test" })
    public static class GameManagementTestConfiguration {

        @Bean
        @Singleton
        public GameServerConnectionManager serverConnectionManager() {
            return new SimpleGameServerConnectionManager("localhost", "localhost");
        }

        @Bean
        @Singleton
        public GameTableQueue gameTableQueue() {
            return new JavaGameTableQueue();
        }

    }

    @Profile(value = { "cloud" })
    public static class GameManagementCloudConfiguration {

        @Inject
        @Named("tableQueueTemplate")
        public RedisTemplate<byte[], Long> redisTemplate;

        @Bean
        @Singleton
        public GameServerConnectionManager serverConnectionManager() {
            return new SimpleGameServerConnectionManager("ec2-50-16-93-157.compute-1.amazonaws.com", "gogomaya.cloudfoundry.com");
        }

        @Bean
        @Singleton
        public GameTableQueue gameTableQueue() {
            return new RedisGameTableQueue(redisTemplate);
        }

    }

}
