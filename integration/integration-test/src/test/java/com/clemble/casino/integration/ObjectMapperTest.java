package com.clemble.casino.integration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.account.GameAccount;
import com.clemble.casino.game.account.InvisibleGameAccount;
import com.clemble.casino.game.account.VisibleGameAccount;
import com.clemble.casino.game.configuration.SelectRuleOptions;
import com.clemble.casino.game.event.client.BetEvent;
import com.clemble.casino.game.event.client.surrender.GiveUpEvent;
import com.clemble.casino.game.event.schedule.PlayerInvitedEvent;
import com.clemble.casino.game.event.server.GameStartedEvent;
import com.clemble.casino.game.rule.GameRule;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.game.NumberState;
import com.clemble.casino.integration.spring.TestConfiguration;
import com.clemble.casino.server.player.notification.SimplePlayerNotificationRegistry;
import com.clemble.casino.utils.ReflectionUtils;
import com.clemble.test.random.ObjectGenerator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
public class ObjectMapperTest extends ObjectTest {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public MappingJackson2HttpMessageConverter httpMessageConverter;

    @Test
    public void specialCase() {
        checkSerialization(SelectRuleOptions.class);
        ObjectGenerator.generate(GameRule.class);

        ObjectGenerator.generate(GameSpecification.class);
        ObjectGenerator.generate(PlayerInvitedEvent.class);
    }

    @Test
    public void testSpecialSerialization() {
        Assert.assertNull(checkSerialization(GameStartedEvent.class));
        Assert.assertNull(checkSerialization(SimplePlayerNotificationRegistry.class));
        Assert.assertNull(checkSerialization(StubGameState.class));
        Assert.assertNull(checkSerialization(GameAccount.class));
        Assert.assertNull(checkSerialization(VisibleGameAccount.class));
        Assert.assertNull(checkSerialization(InvisibleGameAccount.class));
        Assert.assertNull(checkSerialization(BetEvent.class));
        Assert.assertNull(checkSerialization(NumberState.class));
    }

    @Test
    public void testGiveUpReadWrite() throws JsonParseException, JsonMappingException, IOException {
        GiveUpEvent event = new GiveUpEvent(RandomStringUtils.random(5));
        String stringEvent = objectMapper.writeValueAsString(event);
        GiveUpEvent readEvent = (GiveUpEvent) objectMapper.readValue(stringEvent, ClientEvent.class);
        Assert.assertEquals(event, readEvent);
    }

    @Test
    public void testSimpleSerialization() throws IOException {
        List<Class<?>> candidates = ReflectionUtils.findCandidates("com.clemble.casino.server", JsonTypeName.class);

        Map<Class<?>, Throwable> errors = new HashMap<Class<?>, Throwable>();

        for (Class<?> candidate : candidates) {
            Throwable error = checkSerialization(candidate);
            if (error != null) {
                errors.put(candidate, error);
            }
        }

        if (errors.size() != 0) {
            for (Entry<Class<?>, Throwable> problem : errors.entrySet()) {
                System.out.println("Problem " + problem.getKey().getSimpleName() + " > " + problem.getValue().getClass().getSimpleName());
                System.out.println("Sample: " + objectMapper.writeValueAsString(ObjectGenerator.generate(problem.getKey())));
            }
        }
        Assert.assertTrue(errors.toString(), errors.isEmpty());

    }

    private Throwable checkSerialization(Class<?> candidate) {
        return checkSerialization(candidate, ObjectGenerator.generate(candidate));
    }

    private Throwable checkSerialization(Class<?> candidate, Object expected) {
        Throwable error = null;
        try {
            String stringPresentation = objectMapper.writeValueAsString(expected);
            Object actual = objectMapper.readValue(stringPresentation, candidate);

            assertEquals(stringPresentation, expected, actual);

            Class<?> originalClass = getOriginal(candidate);
            Assert.assertNotNull(originalClass);
            actual = objectMapper.readValue(stringPresentation, originalClass);

            Assert.assertEquals(expected, actual);

            MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
            httpMessageConverter.write(expected, MediaType.APPLICATION_JSON, outputMessage);

            HttpInputMessage inputMessage = new MockHttpInputMessage(outputMessage.getBodyAsBytes());
            actual = httpMessageConverter.read(candidate, inputMessage);

            Assert.assertEquals(expected, actual);

            inputMessage = new MockHttpInputMessage(outputMessage.getBodyAsBytes());
            actual = httpMessageConverter.read(originalClass, inputMessage);
            Assert.assertEquals(expected, actual);
        } catch (Throwable throwable) {
            error = throwable;
        }
        return error;
    }

    final public Class<?> getOriginal(final Class<?> source) {
        Class<?> superClass = source;
        boolean found = false;
        Set<Class<?>> interfaces = new HashSet<Class<?>>();

        do {
            found = superClass.getAnnotation(JsonTypeInfo.class) != null;
            if (!found) {
                interfaces.addAll(Arrays.asList(superClass.getInterfaces()));
                superClass = superClass.getSuperclass();
            }
        } while (superClass != Object.class && !found);

        if (!found) {
            Set<Class<?>> interfaceParents = new HashSet<Class<?>>();
            do {
                for (Class<?> interfaceClass : interfaces) {
                    if (interfaceClass.getAnnotation(JsonTypeInfo.class) != null) {
                        superClass = interfaceClass;
                        found = true;
                        break;
                    } else {
                        if (interfaceClass.getSuperclass() != null && interfaceClass.getSuperclass() != Object.class) {
                            interfaceParents.add(interfaceClass);
                        }
                        interfaceParents.addAll(Arrays.asList(interfaceClass.getInterfaces()));
                    }
                }
                interfaces = interfaceParents;
                interfaceParents = new HashSet<Class<?>>();
            } while (!found && !interfaces.isEmpty());
        }

        return superClass != Object.class ? superClass : null;
    }
}
