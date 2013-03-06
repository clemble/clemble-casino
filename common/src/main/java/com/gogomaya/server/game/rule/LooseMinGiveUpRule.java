package com.gogomaya.server.game.rule;

public class LooseMinGiveUpRule extends GiveUpRule {

    final private int minPart;
    
    public LooseMinGiveUpRule(final int minPart) {
        if(minPart > 100)
            throw new IllegalArgumentException("Min part can't exceed 100");
        if(minPart < 0)
            throw new IllegalArgumentException("Min part can't be less than 0");
        this.minPart = minPart;
    }

    public int getMinPart() {
        return minPart;
    }
}
