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
import javax.persistence.Version;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.event.ExpectedAction;
import com.gogomaya.server.game.ConstructionAware;
import com.gogomaya.server.game.event.schedule.InvitationAcceptedEvent;
import com.gogomaya.server.hibernate.JsonHibernateType;

@Entity
@Table(name = "GAME_CONSTRUCTION")
@TypeDefs({
        @TypeDef(name = "game_request", typeClass = JsonHibernateType.class, defaultForType = GameRequest.class, parameters = { @Parameter(
                name = JsonHibernateType.CLASS_NAME_PARAMETER,
                value = "com.gogomaya.server.game.construct.GameRequest") }),
        @TypeDef(name = "action_latch", typeClass = JsonHibernateType.class, defaultForType = ActionLatch.class, parameters = { @Parameter(
                name = JsonHibernateType.CLASS_NAME_PARAMETER,
                value = "com.gogomaya.server.ActionLatch") }) })
public class GameConstruction implements ConstructionAware {

    /**
     * Generated 10/07/13
     */
    private static final long serialVersionUID = 2712386710995109913L;

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

    public ActionLatch getResponses() {
        return responses;
    }

    public void setResponses(ActionLatch responses) {
        this.responses = responses;
    }

    public List<Long> fetchAcceptedParticipants() {
        List<Long> acceptedParticipants = new ArrayList<Long>(responses.fetchParticipants().size());

        for (Entry<Long, ExpectedAction> responseEntry : responses.fetchActionsMap().entrySet()) {
            if (responseEntry.getValue() instanceof InvitationAcceptedEvent)
                acceptedParticipants.add(responseEntry.getKey());
        }

        return acceptedParticipants;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
