package com.clemble.casino.game.construct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Type;

import com.clemble.casino.VersionAware;
import com.clemble.casino.base.ActionLatch;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.SessionAware;
import com.clemble.casino.game.event.schedule.InvitationAcceptedEvent;

@Entity
@Table(name = "GAME_SESSION_CONSTRUCTION")
public class GameConstruction implements SessionAware, VersionAware {

    /**
     * Generated 10/07/13
     */
    private static final long serialVersionUID = 2712386710995109913L;

    @EmbeddedId
    private GameSessionKey session;

    @Type(type = "com.clemble.casino.game.construct.GameRequestHibernate")
    @Column(name = "REQUEST", length = 8192, nullable = false)
    private GameRequest request;

    @Column(name = "STATE", nullable = false)
    @Enumerated(EnumType.STRING)
    private GameConstructionState state;

    @Type(type = "com.clemble.casino.base.ActionLatchHibernate")
    @Column(name = "RESPONSES", length = 8192, nullable = false)
    private ActionLatch responses;

    @Version
    private int version;

    public GameConstruction() {
    }

    public GameConstruction(GameRequest request) {
        this.request = request;
        this.state = GameConstructionState.pending;
        this.responses = new ActionLatch(((GameOpponentsAware) request).getParticipants(), "response");
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }

    public GameConstruction setSession(GameSessionKey session) {
        this.session = session;
        return this;
    }

    public GameRequest getRequest() {
        return request;
    }

    public GameConstruction setRequest(GameRequest gameRequest) {
        this.request = gameRequest;
        return this;
    }

    public GameConstructionState getState() {
        return state;
    }

    public GameConstruction setState(GameConstructionState state) {
        this.state = state;
        return this;
    }

    public ActionLatch getResponses() {
        return responses;
    }

    public GameConstruction setResponses(ActionLatch responses) {
        this.responses = responses;
        return this;
    }

    public List<String> fetchAcceptedParticipants() {
        List<String> acceptedParticipants = new ArrayList<>(responses.fetchParticipants().size());

        for (Entry<String, ClientEvent> responseEntry : responses.fetchActionsMap().entrySet()) {
            if (responseEntry.getValue() instanceof InvitationAcceptedEvent)
                acceptedParticipants.add(responseEntry.getKey());
        }

        return acceptedParticipants;
    }

    @Override
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
