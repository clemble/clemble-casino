package com.clemble.casino.goal.configurationn.controller;

import com.clemble.casino.goal.configuration.GoalConfiguration;
import com.clemble.casino.goal.configuration.controller.GoalConfigurationServiceController;
import com.clemble.casino.goal.configuration.spring.GoalConfigurationSpringConfiguration;
import com.clemble.casino.json.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by mavarazy on 9/16/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GoalConfigurationSpringConfiguration.class)
public class GoalConfigurationServiceControllerTest {

    @Autowired
    public GoalConfigurationServiceController configurationServiceController;

    @Test
    public void test() throws IOException {
        ObjectMapper objectMapper = ObjectMapperUtils.OBJECT_MAPPER;
        String configurationSerialized = objectMapper.writeValueAsString(configurationServiceController.getConfigurations());
        Collection<GoalConfiguration> readConfigurations =  Arrays.asList(objectMapper.readValue(configurationSerialized, GoalConfiguration[].class));
        Assert.assertEquals(readConfigurations, configurationServiceController.getConfigurations());
    }

}
