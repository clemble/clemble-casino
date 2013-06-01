package com.gogomaya.server.test;

import java.util.Collection;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class RedisCleaner implements TestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        clean(testContext);
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        clean(testContext);
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
    }

    @SuppressWarnings("rawtypes")
    private void clean(TestContext testContext) {
        Collection<RedisTemplate> contextTemplates = testContext.getApplicationContext().getBeansOfType(RedisTemplate.class).values();
        for (RedisTemplate template : contextTemplates) {
            Object randomKey = template.randomKey();
            while (randomKey != null) {
                template.delete(randomKey);
                randomKey = template.randomKey();
            }
        }

    }

}
