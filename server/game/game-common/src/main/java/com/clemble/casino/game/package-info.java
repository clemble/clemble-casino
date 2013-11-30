/**
 * This {@link org.hibernate.annotations.TypeDefs} had no effect, so specified full class name in {@link org.hibernate.annotations.Type}
 * */
@TypeDefs(value = {
        @TypeDef(name = "game_request", typeClass = com.clemble.casino.game.construct.GameRequestHibernate.class, defaultForType = com.clemble.casino.game.construct.PlayerGameConstructionRequest.class),
        @TypeDef(name = "action_latch", typeClass = com.clemble.casino.base.ActionLatchHibernate.class, defaultForType = com.clemble.casino.base.ActionLatch.class),
        @TypeDef(name = "gameState", typeClass = com.clemble.casino.game.GameStateHibernate.class, defaultForType = com.clemble.casino.game.GameState.class),
        @TypeDef(name = "totalTime", typeClass = com.clemble.casino.game.rule.time.TotalTimeRule.class, defaultForType = com.clemble.casino.game.rule.time.TimeRule.class),
        @TypeDef(name = "moveTime", typeClass = com.clemble.casino.game.rule.time.MoveTimeRuleHibernate.class, defaultForType = com.clemble.casino.game.rule.time.MoveTimeRule.class),
        @TypeDef(name = "money", typeClass = com.clemble.casino.payment.money.MoneyHibernate.class, defaultForType = com.clemble.casino.payment.money.Money.class),
        @TypeDef(name = "betRule", typeClass = com.clemble.casino.game.rule.bet.BetRuleHibernate.class, defaultForType = com.clemble.casino.game.rule.bet.BetRule.class),
        @TypeDef(name = "gameMove", typeClass = com.clemble.casino.event.ClientEventHibernate.class, defaultForType = com.clemble.casino.event.ClientEvent.class) })
package com.clemble.casino.game;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

