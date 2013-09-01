package com.gogomaya.server;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.ScheduledGame;
import com.gogomaya.server.repository.game.GameConstructionRepository;
import com.gogomaya.server.repository.game.GameScheduleRepository;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.stresstest.random.ObjectGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
public class ObjectPersistenceTest extends ObjectTest implements ApplicationContextAware {

    @SuppressWarnings("rawtypes")
    Map<String, JpaRepository> repositories;

    @Inject
    public GameScheduleRepository gameScheduleRepository;

    @Inject
    public GameConstructionRepository constructionRepository;

    @Test
    public void testSpecialCase() {
        check(constructionRepository, GameConstruction.class);

        ScheduledGame scheduledGame = ObjectGenerator.generate(ScheduledGame.class);

        ScheduledGame savedScheduledGame = gameScheduleRepository.saveAndFlush(scheduledGame);
        Assert.assertEquals(scheduledGame, savedScheduledGame);

        ScheduledGame foundScheduledGame = gameScheduleRepository.findOne(savedScheduledGame.getSession());
        Assert.assertEquals(foundScheduledGame, savedScheduledGame);
        Assert.assertEquals(foundScheduledGame, scheduledGame);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPersistence() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // Step 1. Making class to repositoyr association
        Map<Class<?>, JpaRepository> domainClassToRepository = new HashMap<>();
        for (JpaRepository repository : repositories.values()) {
            Class<?> domainClass = getDomainClass(repository);
            domainClassToRepository.put(domainClass, repository);
        }
        // Step 2. Checking serialization
        Map<Class<?>, Throwable> errors = new HashMap<>();
        for (Class<?> domainClass : domainClassToRepository.keySet()) {
            try {
                Object objectToSave = ObjectGenerator.generate(domainClass);
                for (Field field : domainClass.getDeclaredFields()) {
                    @SuppressWarnings("rawtypes")
                    JpaRepository relatedEntityRepository = domainClassToRepository.get(field.getType());
                    if (relatedEntityRepository != null) {
                        field.setAccessible(true);
                        Object relatedEntity = field.get(objectToSave);
                        relatedEntityRepository.saveAndFlush(relatedEntity);
                    }
                }

                domainClassToRepository.get(domainClass).saveAndFlush(objectToSave);
            } catch (Throwable throwable) {
                errors.put(domainClass, throwable);
            }
        }

        if (errors.size() > 0) {
            for (Class<?> domain : errors.keySet()) {
                System.out.println("Failed " + domain.getSimpleName());
                System.out.println("Failed with " + errors.get(domain));
            }

        }

        Assert.assertTrue(String.valueOf(errors), errors.isEmpty());
    }

    private <T> void check(JpaRepository<T, Long> relatedEntityRepository, Class<T> domainClass) {
        T objectToSave = ObjectGenerator.generate(domainClass);
        relatedEntityRepository.saveAndFlush(objectToSave);
    }

    public Class<?> getDomainClass(JpaRepository repository) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        RepositoryMetadata repositoryMetadata = fetchInterfaces(repository.getClass().getInterfaces());
        return repositoryMetadata.getDomainType();
    }

    public RepositoryMetadata fetchInterfaces(Class<?>[] interfaces) {
        for (Class<?> repositoryInterface : interfaces) {
            if (Repository.class.isAssignableFrom(repositoryInterface))
                return new DefaultRepositoryMetadata(repositoryInterface);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // Step 1. Fetching all repositories
        repositories = applicationContext.getBeansOfType(JpaRepository.class);
    }

}
