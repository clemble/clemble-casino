package com.gogomaya.server.game.session;

public interface GameSessionCurrentStateAware<T extends GameSessionCurrentStateAware<T>> {

    public long getCurrentPlayerId();

    public T setCurrentPlayerId(long activePlayerId);

    public long getCurrentStep();

    public T setCurrentStep(long activeStep);

}
