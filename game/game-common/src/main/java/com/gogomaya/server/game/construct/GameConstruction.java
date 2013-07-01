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
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.game.GameConstuctionAware;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.game.event.schedule.InvitationAcceptedEvent;
import com.gogomaya.server.game.event.schedule.InvitationResponceEvent;
import com.gogomaya.server.hibernate.JsonHibernateType;

@Entity
@Table(name = "GAME_CONSTRUCTION")
@TypeDefs(value = { @TypeDef(name = "game_request", typeClass = JsonHibernateType.class, defaultForType = GameRequest.class, parameters = { @Parameter(name = JsonHibernateType.CLASS_NAME_PARAMETER, value = "com.gogomaya.server.game.construct.GameRequest") }) })
public class GameConstruction implements SessionAware, GameConstuctionAware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONSTRUCTION_ID")
    private long construction;

    @Type(type = "game_request")
    @Column(name = "REQUEST", length = 4096)
    private GameRequest request;

    @Enumerated(EnumType.STRING)
    private GameConstructionState state;

    @Transient
    private ActionLatch<InvitationResponceEvent> responces;

    @Column(name = "SESSION_ID")
    private long session;

    public GameConstruction() {
    }

    public GameConstruction(GameRequest request) {
        this.request = request;
        this.responces = new ActionLatch<InvitationResponceEvent>(((GameOpponentsAware) request).getParticipants());
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

    public ActionLatch<InvitationResponceEvent> getResponces() {
        return responces;
    }

    public void setResponces(ActionLatch<InvitationResponceEvent> responces) {
        this.responces = responces;
    }

    public List<Long> fetchAcceptedParticipants() {
        List<Long> acceptedParticipants = new ArrayList<Long>(responces.getParticipants().size());

        for (Entry<Long, InvitationResponceEvent> responceEntry : responces.get().entrySet()) {
            if (responceEntry.getValue() instanceof InvitationAcceptedEvent)
                acceptedParticipants.add(responceEntry.getKey());
        }

        return acceptedParticipants;
    }

}
