package com.clemble.casino.server.game.construct;

import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameInitiation;

public interface GameInitiatorService {

    public void initiate(GameConstruction construction);

    public boolean initiate(GameInitiation initiation);

}
