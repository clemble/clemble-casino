package com.gogomaya.server.game.rule.time;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleDeserializer;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleSerializer;

@JsonSerialize(using = CustomTimeRuleSerializer.class)
@JsonDeserialize(using = CustomTimeRuleDeserializer.class)
final public class UnlimitedTimeRule extends TimeRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 1731476218744551572L;

    private UnlimitedTimeRule() {
        super(TimeRuleType.Unlimited, TimeBreachBehavior.DoNothing);
    }

    public static UnlimitedTimeRule INSTANCE = new UnlimitedTimeRule();

}