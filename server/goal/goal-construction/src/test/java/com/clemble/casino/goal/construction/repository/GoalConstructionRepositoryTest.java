package com.clemble.casino.goal.construction.repository;

import com.clemble.casino.lifecycle.construction.ConstructionState;
import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.construction.spring.GoalConstructionSpringConfiguration;
import com.clemble.casino.lifecycle.configuration.rule.bet.BetRule;
import com.clemble.casino.lifecycle.configuration.rule.bet.FixedBetRule;
import com.clemble.casino.lifecycle.configuration.rule.bet.LimitedBetRule;
import com.clemble.test.random.AbstractValueGenerator;
import com.clemble.test.random.ObjectGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.EnumMap;
import java.util.List;

import static com.clemble.test.random.ObjectGenerator.generate;

/**
 * Created by mavarazy on 9/10/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GoalConstructionSpringConfiguration.class)
public class GoalConstructionRepositoryTest {

    @Autowired
    public GoalConstructionRepository constructionRepository;

    @Test
    public void simpleCRUDtest() {
        ObjectGenerator.register(BetRule.class, new AbstractValueGenerator<BetRule>() {
            @Override
            public BetRule generate() {
                return LimitedBetRule.create(0, 40);
            }
        });
        ObjectGenerator.register(FixedBetRule.class, new AbstractValueGenerator<FixedBetRule>() {
            @Override
            public FixedBetRule generate() {
                return FixedBetRule.create(10);
            }
        });
        // Step 1. Generating random construction
        GoalConstruction construction = ObjectGenerator.generate(GoalConstruction.class);
        // Step 3. Checking saved matches original
        Assert.assertEquals(construction, constructionRepository.save(construction));
        // Step 4. Checking read matches original
        Assert.assertEquals(construction, constructionRepository.findOne(construction.getGoalKey()));
    }

    @Test
    public void testByStatus() {
        // Step 1. Generating random constructions
        GoalConstruction construction = ObjectGenerator.generate(GoalConstruction.class);
        EnumMap<ConstructionState, GoalConstruction> savedConstructions = new EnumMap<ConstructionState, GoalConstruction>(ConstructionState.class);
        for(ConstructionState state: ConstructionState.values()) {
            GoalConstruction newConstruction = constructionRepository.save(construction.clone(generate(String.class), state));
            savedConstructions.put(state, newConstruction);
        }
        // Step 3. Checking queried matches expected
        for(ConstructionState state: ConstructionState.values()) {
            List<GoalConstruction> relatedConstructions = constructionRepository.findByPlayerAndState(construction.getPlayer(), state);
            Assert.assertEquals(relatedConstructions.size(), 1);
            Assert.assertEquals(relatedConstructions.get(0), savedConstructions.get(state));
        }
    }

}
