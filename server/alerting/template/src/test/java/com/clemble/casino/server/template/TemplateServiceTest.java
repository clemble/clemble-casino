package com.clemble.casino.server.template;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mavarazy on 12/30/14.
 */
public class TemplateServiceTest {

    @Test
    public void testTemplate() {
        TemplateService templateService = new MustacheTemplateService();
        String template = templateService.produce("sample", ImmutableMap.<String, String>of("name", "Joe"));
        Assert.assertEquals(template.trim(), "Hello Joe!");
    }

}
