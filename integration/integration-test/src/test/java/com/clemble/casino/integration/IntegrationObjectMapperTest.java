package com.clemble.casino.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import com.clemble.casino.configuration.Configuration;
import com.clemble.casino.event.surrender.GiveUpEvent;
import com.clemble.casino.event.bet.BetEvent;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.game.configuration.MatchGameConfiguration;
import com.clemble.casino.game.rule.GameRule;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.game.rule.construct.PlayerNumberRule;
import com.clemble.casino.game.rule.giveup.GiveUpRule;
import com.clemble.casino.game.rule.match.MatchFillRule;
import com.clemble.casino.game.rule.outcome.DrawRule;
import com.clemble.casino.game.rule.outcome.WonRule;
import com.clemble.casino.game.rule.visibility.VisibilityRule;
import com.clemble.casino.game.unit.GameUnit;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.rule.bet.UnlimitedBetRule;
import com.clemble.casino.rule.breach.LooseBreachPunishment;
import com.clemble.casino.rule.privacy.PrivacyRule;
import com.clemble.casino.rule.time.MoveTimeRule;
import com.clemble.casino.rule.time.TotalTimeRule;
import com.clemble.casino.server.spring.WebJsonSpringConfiguration;
import com.google.common.collect.ImmutableList;
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

import com.clemble.casino.event.Event;
import com.clemble.casino.game.construction.ScheduledGameRequest;
import com.clemble.casino.game.event.schedule.InvitationDeclinedEvent;
import com.clemble.casino.game.event.schedule.PlayerInvitedEvent;
import com.clemble.casino.game.event.server.RoundStartedEvent;
import com.clemble.casino.integration.game.NumberState;
import com.clemble.test.random.ObjectGenerator;
import com.clemble.test.reflection.AnnotationReflectionUtils;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebJsonSpringConfiguration.class })
public class IntegrationObjectMapperTest extends IntegrationObjectTest {

    @Autowired
    public ObjectMapper objectMapper;

//    TODO this is disabled because WebAppContext is not created by default
    @Autowired
    public MappingJackson2HttpMessageConverter httpMessageConverter;

    @Test
    public void specialCase() {
        ObjectGenerator.generate(GameRule.class);

        ObjectGenerator.generate(RoundGameConfiguration.class);
        ObjectGenerator.generate(PlayerInvitedEvent.class);
    }

    @Test
    public void testSpecialSerialization() {
        Assert.assertNull(checkSerialization(InvitationDeclinedEvent.class));
        Assert.assertNull(checkSerialization(ScheduledGameRequest.class));
        Assert.assertNull(checkSerialization(RoundStartedEvent.class));
        Assert.assertNull(checkSerialization(BetEvent.class));
        Assert.assertNull(checkSerialization(NumberState.class));
    }

    @Test
    public void testGiveUpReadWrite() throws JsonParseException, JsonMappingException, IOException {
        GiveUpEvent event = new GiveUpEvent(RandomStringUtils.random(5));
        String stringEvent = objectMapper.writeValueAsString(event);
        GiveUpEvent readEvent = (GiveUpEvent) objectMapper.readValue(stringEvent, Event.class);
        Assert.assertEquals(event, readEvent);
    }

    @Test
    public void testSimpleSerialization() throws IOException {
        List<Class<?>> candidates = AnnotationReflectionUtils.findCandidates("com.clemble.casino", JsonTypeName.class);

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
        Throwable error = null;
        try {
            Object expected = ObjectGenerator.generate(candidate);
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
        } while (superClass != Object.class && !found && superClass != null);

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

    @Test
    public void testArraySerialization() throws IOException {
        GameConfiguration[] configurations = new GameConfiguration[4];
        // Step 1. Creating round configuration
        RoundGameConfiguration roundConfiguration = new RoundGameConfiguration(
            Game.num,
            "low",
            Money.create(Currency.FakeMoney, 50),
            UnlimitedBetRule.INSTANCE,
            GiveUpRule.all,
            new MoveTimeRule(2000, LooseBreachPunishment.getInstance()),
            new TotalTimeRule(4000, LooseBreachPunishment.getInstance()),
            PrivacyRule.everybody,
            PlayerNumberRule.two,
            VisibilityRule.visible,
            DrawRule.owned,
            WonRule.price,
            ImmutableList.of("A", "B"),
            new ArrayList<GameUnit>()
        );
        configurations[2] = roundConfiguration;
        configurations[3] = roundConfiguration;
        // Step 2. Creating match configuration
        MatchGameConfiguration matchConfiguration = new MatchGameConfiguration(
            Game.pot,
            "pot",
            Money.create(Currency.FakeMoney, 200),
            PrivacyRule.everybody,
            PlayerNumberRule.two,
            MatchFillRule.none,
            new MoveTimeRule(50000, LooseBreachPunishment.getInstance()),
            new TotalTimeRule(500000, LooseBreachPunishment.getInstance()),
            WonRule.price,
            DrawRule.owned,
            ImmutableList.of(roundConfiguration, roundConfiguration, roundConfiguration),
            Collections.emptyList());
        configurations[0] = matchConfiguration;
        configurations[1] = matchConfiguration;
        // Step 3. Serializing configurations
        String configurationJson = objectMapper.writeValueAsString(configurations);
        // Step 4. Deserialize configurations
        Configuration[] dConfigurations = objectMapper.readValue(configurationJson, GameConfiguration[].class);
        // Step 5. Checking everything deserialized correctly
        assertEquals(dConfigurations.length, configurations.length);
//        for(Configuration configuration: dConfigurations)
//            assertTrue(configurations.contains(configuration));
    }
}
