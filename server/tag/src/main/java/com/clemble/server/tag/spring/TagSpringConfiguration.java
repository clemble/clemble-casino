package com.clemble.server.tag.spring;

import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.server.tag.controller.ClembleTagServiceController;
import com.clemble.server.tag.repository.ServerPlayerTagsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 2/3/15.
 */
@Configuration
@Import({CommonSpringConfiguration.class, MongoSpringConfiguration.class})
public class TagSpringConfiguration implements SpringConfiguration {

    @Bean
    public ServerPlayerTagsRepository serverPlayerTagsRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(ServerPlayerTagsRepository.class);
    }

    @Bean
    public ClembleTagServiceController clembleTagServiceController(ServerPlayerTagsRepository tagsRepository) {
        return new ClembleTagServiceController(tagsRepository);
    }

}
