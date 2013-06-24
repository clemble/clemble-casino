package com.gogomaya.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import junit.framework.Assert;

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
import com.gogomaya.server.game.event.client.GiveUpEvent;
import com.gogomaya.server.game.event.schedule.PlayerInvitedEvent;
import com.gogomaya.server.game.event.server.GameEndedEvent;
import com.gogomaya.server.game.event.server.GameStartedEvent;
import com.gogomaya.server.game.event.server.PlayerLostEvent;
import com.gogomaya.server.game.event.server.PlayerMovedEvent;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.game.tictactoe.event.client.TicTacToeBetOnCellEvent;
import com.gogomaya.server.spring.web.CommonWebSpringConfiguration;
import com.gogomaya.server.utils.ReflectionUtils;
import com.stresstest.random.AbstractValueGenerator;
import com.stresstest.random.ObjectGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { CommonWebSpringConfiguration.class })
public class ObjectMapperTest {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    MappingJackson2HttpMessageConverter httpMessageConverter;

    static {
        ObjectGenerator.register(FixedBetRule.class, new AbstractValueGenerator<FixedBetRule>() {
            @Override
            public FixedBetRule generate() {
                return new FixedBetRule(100);
            }

        });
        ObjectGenerator.register(TicTacToeState.class, new AbstractValueGenerator<TicTacToeState>() {
            public TicTacToeState generate() {
                return new TicTacToeState();
            }
        });
        ObjectGenerator.register(ClientEvent.class, new AbstractValueGenerator<ClientEvent>() {

            @Override
            public ClientEvent generate() {
                return new GiveUpEvent(-1L);
            }
        });
        ObjectGenerator.register(TicTacToeBetOnCellEvent.class, new AbstractValueGenerator<TicTacToeBetOnCellEvent>() {

            @Override
            public TicTacToeBetOnCellEvent generate() {
                return new TicTacToeBetOnCellEvent(-1L, 100);
            }
        });
        ObjectGenerator.register(GameStartedEvent.class, new AbstractValueGenerator<GameStartedEvent>() {

            @Override
            public GameStartedEvent generate() {
                GameStartedEvent gameStartedEvent = new GameStartedEvent<>();
                gameStartedEvent.setSession(10L);
                gameStartedEvent.setState(new TicTacToeState());
                return gameStartedEvent;
            }
        });
    }

    @Test
    public void specialCase() {
        ObjectGenerator.generate(GameSpecification.class);
        ObjectGenerator.generate(PlayerInvitedEvent.class);
        ObjectGenerator.generate(TicTacToeState.class);
        ObjectGenerator.generate(GameEndedEvent.class);
    }

    @Test
    public void testSpecialSerialization() {
        Assert.assertNull(checkSerialization(GameEndedEvent.class));
        Assert.assertNull(checkSerialization(TicTacToeBetOnCellEvent.class));
        Assert.assertNull(checkSerialization(PlayerMovedEvent.class));
        Assert.assertNull(checkSerialization(PlayerLostEvent.class));
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
            }
        }
        Assert.assertTrue(errors.toString(), errors.isEmpty());

    }

    private Throwable checkSerialization(Class<?> candidate) {
        Throwable error = null;
        try {
            Object expected = ObjectGenerator.generate(candidate);
            String stringPresentation = objectMapper.writeValueAsString(expected);
            Object actual = objectMapper.readValue(stringPresentation, candidate);

            Assert.assertEquals(expected, actual);

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
