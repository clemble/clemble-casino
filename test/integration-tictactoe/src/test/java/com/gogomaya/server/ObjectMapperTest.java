package com.gogomaya.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.account.GameAccount;
import com.gogomaya.server.game.account.InvisibleGameAccount;
import com.gogomaya.server.game.account.VisibleGameAccount;
import com.gogomaya.server.game.cell.CellState;
import com.gogomaya.server.game.cell.ExposedCellState;
import com.gogomaya.server.game.configuration.SelectRuleOptions;
import com.gogomaya.server.game.event.client.BetEvent;
import com.gogomaya.server.game.event.client.surrender.GiveUpEvent;
import com.gogomaya.server.game.event.schedule.PlayerInvitedEvent;
import com.gogomaya.server.game.event.server.GameEndedEvent;
import com.gogomaya.server.game.event.server.PlayerMovedEvent;
import com.gogomaya.server.game.event.server.PlayerSurrenderedEvent;
import com.gogomaya.server.game.rule.GameRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.spring.integration.PicPacPoeTestConfiguration;
import com.gogomaya.server.tictactoe.PicPacPoeState;
import com.gogomaya.server.utils.ReflectionUtils;
import com.stresstest.random.ObjectGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { PicPacPoeTestConfiguration.class })
public class ObjectMapperTest extends ObjectTest {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    MappingJackson2HttpMessageConverter httpMessageConverter;

    @Test
    public void specialCase() {
        checkSerialization(SelectRuleOptions.class);
        ObjectGenerator.generate(GameRule.class);

        checkSerialization(ExposedCellState.class, new ExposedCellState(new BetEvent(1, 10), new BetEvent(1, 10)));
        checkSerialization(CellState.class, new CellState(1));

        ObjectGenerator.generate(GameSpecification.class);
        ObjectGenerator.generate(PlayerInvitedEvent.class);
        ObjectGenerator.generate(PicPacPoeState.class);
        ObjectGenerator.generate(GameEndedEvent.class);
    }

    @Test
    public void testSpecialSerialization() {
        Assert.assertNull(checkSerialization(GameAccount.class));
        Assert.assertNull(checkSerialization(VisibleGameAccount.class));
        Assert.assertNull(checkSerialization(InvisibleGameAccount.class));
        Assert.assertNull(checkSerialization(PicPacPoeState.class));
        Assert.assertNull(checkSerialization(BetEvent.class));
        Assert.assertNull(checkSerialization(PlayerMovedEvent.class));
        Assert.assertNull(checkSerialization(PlayerSurrenderedEvent.class));
        Assert.assertNull(checkSerialization(GameEndedEvent.class));
    }

    @Test
    public void testGiveUpReadWrite() throws JsonParseException, JsonMappingException, IOException {
        GiveUpEvent event = new GiveUpEvent(0L);
        String stringEvent = objectMapper.writeValueAsString(event);
        GiveUpEvent readEvent = (GiveUpEvent) objectMapper.readValue(stringEvent, ClientEvent.class);
        Assert.assertEquals(event, readEvent);
    }

    @Test
    public void testSimpleSerialization() throws IOException {
        List<Class<?>> candidates = ReflectionUtils.findCandidates("com.gogomaya.server", JsonTypeName.class);

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
