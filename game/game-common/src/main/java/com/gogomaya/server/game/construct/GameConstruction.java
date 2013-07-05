package com.gogomaya.server.game.construct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.event.ExpectedAction;
import com.gogomaya.server.game.ConstructionAware;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.game.event.schedule.InvitationAcceptedEvent;
import com.gogomaya.server.hibernate.JsonHibernateType;

@Entity
@Table(name = "GAME_CONSTRUCTION")
@TypeDefs({
    @TypeDef(name = "game_request", typeClass = JsonHibernateType.class, defaultForType = GameRequest.class, parameters = { @Parameter(name = JsonHibernateType.CLASS_NAME_PARAMETER, value = "com.gogomaya.server.game.construct.GameRequest")}),
    @TypeDef(name = "action_latch", typeClass = JsonHibernateType.class, defaultForType = ActionLatch.class, parameters = { @Parameter(name = JsonHibernateType.CLASS_NAME_PARAMETER, value = "com.gogomaya.server.ActionLatch")})
})
public class GameConstruction implements SessionAware, ConstructionAware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONSTRUCTION_ID")
    private long construction;

    @Type(type = "game_request")
    @Column(name = "REQUEST", length = 4096, nullable = false)
    private GameRequest request;

    @Column(name = "STATE", nullable = false)
    @Enumerated(EnumType.STRING)
    private GameConstructionState state;

    @Type(type = "action_latch")
    @Column(name = "RESPONSES", length = 4096, nullable = false)
    private ActionLatch responces;

    @Column(name = "SESSION_ID")
    private long session;

    public GameConstruction() {
    }

    public GameConstruction(GameRequest request) {
        this.request = request;
        this.responces = new ActionLatch(((GameOpponentsAware) request).getParticipants(), "response");
    }

    @Override
    public long getConstruction() {
        return construction;
    }

    public void setConstruction(long construction) {
        this.construction = construction;
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

    public void setState(GameConstructionState state) {
        this.state = state;
    }

    public long getSession() {
        return session;
    }

    public void setSession(long session) {
        this.session = session;
    }

    public ActionLatch getResponces() {
        return responces;
    }

    public void setResponces(ActionLatch responces) {
        this.responces = responces;
    }

    public List<Long> fetchAcceptedParticipants() {
        List<Long> acceptedParticipants = new ArrayList<Long>(responces.fetchParticipants().size());

        for (Entry<Long, ExpectedAction> responceEntry : responces.fetchActionsMap().entrySet()) {
            if (responceEntry.getValue() instanceof InvitationAcceptedEvent)
                acceptedParticipants.add(responceEntry.getKey());
        }

        return acceptedParticipants;
    }

}
