package com.gogomaya.server.game.rule.bet;


final public class UnlimitedBetRule extends BetRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 6788161410535376939L;

    private UnlimitedBetRule() {
    }

    public static UnlimitedBetRule INSTANCE = new UnlimitedBetRule();

    public UnlimitedBetRule create() {
        return INSTANCE;
    }

}