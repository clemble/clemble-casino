package com.gogomaya.server.spring.player;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.spring.common.CommonSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class })
public class PlayerManagementSpringConfiguration implements SpringConfiguration {

}
