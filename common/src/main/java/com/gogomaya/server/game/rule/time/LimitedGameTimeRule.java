package com.gogomaya.server.game.rule.time;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleDeseriler;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleSerializer;

@JsonSerialize(using = CustomTimeRuleSerializer.class)
@JsonDeserialize(using = CustomTimeRuleDeseriler.class)
final public class LimitedGameTimeRule extends TimeRule {

    final private int gameTimeLimit;

    private LimitedGameTimeRule(final TimeBreachBehavior timeBreachBehavior, final int timeLimit) {
        super(TimeRuleType.LimitedGameTime, timeBreachBehavior);
        if(timeLimit < 0)
            throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
        this.gameTimeLimit = timeLimit;
    }

    public int getGameTimeLimit() {
        return gameTimeLimit;
    }

    public static LimitedGameTimeRule create(final TimeBreachBehavior timeRuleBreachBehavior, final int timeLimit) {
        return new LimitedGameTimeRule(timeRuleBreachBehavior, timeLimit);
    }

}