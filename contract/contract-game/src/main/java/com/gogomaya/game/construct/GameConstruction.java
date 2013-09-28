package com.gogomaya.game.construct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.gogomaya.VersionAware;
import com.gogomaya.base.ActionLatch;
import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.SessionAware;
import com.gogomaya.game.event.schedule.InvitationAcceptedEvent;
import com.gogomaya.server.hibernate.JsonHibernateType;

@Entity
@Table(name = "GAME_SESSION_CONSTRUCTION")
@TypeDefs({
        @TypeDef(name = "game_request", typeClass = JsonHibernateType.class, defaultForType = GameRequest.class, parameters = { @Parameter(
                name = JsonHibernateType.CLASS_NAME_PARAMETER,
                value = "com.gogomaya.game.construct.GameRequest") }),
        @TypeDef(name = "action_latch", typeClass = JsonHibernateType.class, defaultForType = ActionLatch.class, parameters = { @Parameter(
                name = JsonHibernateType.CLASS_NAME_PARAMETER,
                value = "com.gogomaya.base.ActionLatch") }) })
public class GameConstruction implements SessionAware, VersionAware {

    /**
     * Generated 10/07/13
     */
    private static final long serialVersionUID = 2712386710995109913L;

    @EmbeddedId
    @GeneratedValue(generator = "gameSessionKeyGenerator")
    @GenericGenerator(
        name = "gameSessionKeyGenerator",
        strategy = "com.gogomaya.game.GameSessionKeyGenerator")
    private GameSessionKey session;

    @Type(type = "game_request")
    @Column(name = "REQUEST", length = 8192, nullable = false)
    private GameRequest request;

    @Column(name = "STATE", nullable = false)
    @Enumerated(EnumType.STRING)
    private GameConstructionState state;

    @Type(type = "action_latch")
    @Column(name = "RESPONSES", length = 8192, nullable = false)
    private ActionLatch responses;

    @Version
    private int version;

    public GameConstruction() {
    }

    public GameConstruction(GameRequest request) {
        this.request = request;
        this.session = new GameSessionKey(request.getSpecification().getName().getGame(), 0);
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
