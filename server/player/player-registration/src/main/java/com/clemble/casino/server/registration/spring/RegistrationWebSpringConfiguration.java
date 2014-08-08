package com.clemble.casino.server.registration.spring;

import com.clemble.casino.server.security.AESPlayerTokenFactory;
import com.clemble.casino.server.spring.PlayerTokenSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.WebCommonSpringConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * Created by mavarazy on 7/5/14.
 */
@Configuration
@Import({RegistrationSpringConfiguration.class, WebCommonSpringConfiguration.class})
public class RegistrationWebSpringConfiguration implements SpringConfiguration {

}
