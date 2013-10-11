package com.clemble.casino.server.spring.player;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.player.PlayerCommonSpringConfiguration;

@Configuration
@EnableCouchbaseRepositories(basePackageClasses = PlayerProfileRepository.class)
@Import(value = { CommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class })
public class PlayerManagementSpringConfiguration implements SpringConfiguration {

}
