package com.gogomaya.server.spring.management;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.spring.common.CommonSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.payment.PaymentCommonSpringConfiguration;
import com.gogomaya.server.spring.player.PlayerCommonSpringConfiguration;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class, PaymentCommonSpringConfiguration.class })
public class ManagementCommonSpringConfiguration implements SpringConfiguration {

}
