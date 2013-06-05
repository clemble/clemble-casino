package com.gogomaya.server.spring.game;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.game.table.GameTableQueue;
import com.gogomaya.server.game.table.JavaGameTableQueue;
import com.gogomaya.server.game.table.RedisGameTableQueue;
import com.gogomaya.server.notification.ServerRegistry;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gogomaya.server.game", entityManagerFactoryRef = "entityManagerFactory")
@Import(value = { CommonSpringConfiguration.class, GameManagementSpringConfiguration.DefaultAndTest.class,
        GameManagementSpringConfiguration.Cloud.class })
public class GameManagementSpringConfiguration {

    @Profile(value = { "default", "test" })
    public static class DefaultAndTest {

        @Bean
        @Singleton
        public TableServerRegistry tableServerRegistry() {
            ServerRegistry serverRegistry = new ServerRegistry();
            serverRegistry.register(10000L, "localhost");
            return new TableServerRegistry(serverRegistry);
        }

        @Bean
        @Singleton
        public GameTableQueue gameTableQueue() {
            return new JavaGameTableQueue();
        }

    }

    @Profile(value = { "cloud" })
    public static class Cloud {

        @Inject
        @Named("tableQueueTemplate")
        public RedisTemplate<byte[], Long> redisTemplate;

        @Bean
        @Singleton
        public TableServerRegistry tableServerRegistry() {
            ServerRegistry serverRegistry = new ServerRegistry();
            serverRegistry.register(10000L, "gogomaya.cloudfoundry.com");
            return new TableServerRegistry(serverRegistry);
        }

        @Bean
        @Singleton
        public GameTableQueue gameTableQueue() {
            return new RedisGameTableQueue(redisTemplate);
        }

    }

}
