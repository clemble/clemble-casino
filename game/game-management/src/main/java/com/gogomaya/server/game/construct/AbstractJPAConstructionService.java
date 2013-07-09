package com.gogomaya.server.game.construct;

import org.springframework.dao.ConcurrencyFailureException;

import com.gogomaya.server.game.event.schedule.InvitationResponceEvent;

abstract public class AbstractJPAConstructionService implements GameConstructionService {

    @Override
    public GameConstruction construct(GameRequest request) {
        try {
            return null;
        } catch (ConcurrencyFailureException concurrencyFailureException) {
            return construct(request);
        }
    }
    
    

    @Override
    public GameConstruction invitationResponsed(InvitationResponceEvent response) {
        // TODO Auto-generated method stub
        return null;
    }

}
