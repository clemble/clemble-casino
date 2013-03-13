package com.gogomaya.server.game.rule.time;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleDeserializer;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleSerializer;

@JsonSerialize(using = CustomTimeRuleSerializer.class)
@JsonDeserialize(using = CustomTimeRuleDeserializer.class)
final public class LimitedGameTimeRule extends TimeRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 7452918511506230595L;

    final private int gameTimeLimit;

    private LimitedGameTimeRule(final TimeBreachBehavior timeBreachBehavior, final int timeLimit) {
        super(TimeRuleType.LimitedGameTime, timeBreachBehavior);
        if (timeLimit < 0)
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