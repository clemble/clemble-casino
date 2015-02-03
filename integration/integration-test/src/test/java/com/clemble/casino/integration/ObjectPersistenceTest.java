package com.clemble.casino.integration;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.server.game.construction.repository.GameConstructionRepository;
import com.clemble.test.random.ObjectGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class ObjectPersistenceTest extends IntegrationObjectTest implements ApplicationContextAware {

    @SuppressWarnings("rawtypes")
    public Map<String, JpaRepository> repositories;

    @Autowired
    public GameConstructionRepository constructionRepository;

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
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
                    if((field.getModifiers() & Modifier.STATIC) > 0)
                            continue;
                    JpaRepository relatedEntityRepository = domainClassToRepository.get(field.getType());
                    if (relatedEntityRepository != null) {
                        field.setAccessible(true);
                        Object relatedEntity = field.get(objectToSave);
                        Object savedEntity = relatedEntityRepository.saveAndFlush(relatedEntity);
                        field.set(objectToSave, savedEntity);
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

    private <T, ID extends Serializable> void check(JpaRepository<T, ID> relatedEntityRepository, Class<T> domainClass) {
        T objectToSave = ObjectGenerator.generate(domainClass);
        relatedEntityRepository.saveAndFlush(objectToSave);
    }

    public Class<?> getDomainClass(JpaRepository<?,?> repository) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
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
