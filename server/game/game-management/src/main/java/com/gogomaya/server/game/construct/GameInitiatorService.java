package com.gogomaya.server.game.construct;

import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameInitiation;

public interface GameInitiatorService {

    public void initiate(GameConstruction construction);

    public boolean initiate(GameInitiation initiation);

}
