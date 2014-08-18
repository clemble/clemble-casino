package com.clemble.casino.goal.controller;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.goal.GoalJudgeInvitation;
import static com.clemble.casino.goal.GoalJudgeWebMapping.*;
import com.clemble.casino.goal.GoalKey;
import com.clemble.casino.goal.repository.GoalJudgeInvitationRepository;
import com.clemble.casino.goal.service.GoalJudgeInvitationService;
import com.clemble.casino.web.mapping.WebMapping;
import static org.springframework.http.HttpStatus.*;
import org.springframework.web.bind.annotation.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mavarazy on 8/17/14.
 */
@RestController
public class GoalJudgeInvitationServiceController implements GoalJudgeInvitationService {

    final private GoalJudgeInvitationRepository invitationRepository;

    public GoalJudgeInvitationServiceController(GoalJudgeInvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    @Override
    public Collection<GoalJudgeInvitation> myDuties() {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = GET, value = MY_DUTIES,produces = WebMapping.PRODUCES)
    @ResponseStatus(OK)
    public Collection<GoalJudgeInvitation> myDuties(@CookieValue("player") String player) {
        return invitationRepository.findByJudge(player);
    }

    @Override
    public Collection<GoalJudgeInvitation> myInvitations() {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = GET, value = MY_INVITATIONS,produces = WebMapping.PRODUCES)
    @ResponseStatus(OK)
    public Collection<GoalJudgeInvitation> myInvitations(@CookieValue("player") String player) {
        return invitationRepository.findByPlayer(player);
    }

    @Override
    public Collection<GoalJudgeInvitation> myDutiesAndInvitations() {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = GET, value = MY_DUTIES_AND_INVITATIONS,produces = WebMapping.PRODUCES)
    @ResponseStatus(OK)
    public Collection<GoalJudgeInvitation> myDutiesAndInvitations(@CookieValue("player") String player) {
        List<GoalJudgeInvitation> allInvitations = new ArrayList<>();
        allInvitations.addAll(invitationRepository.findByPlayer(player));
        allInvitations.addAll(invitationRepository.findByJudge(player));
        return allInvitations;
    }

    @Override
    public GoalJudgeInvitation reply(GoalKey goalKey, GoalJudgeInvitation response) {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = POST, value = INVITATION_REPLY,produces = WebMapping.PRODUCES)
    @ResponseStatus(OK)
    public GoalJudgeInvitation reply(@CookieValue("player") String requester, @PathVariable("player") String player, @PathVariable("id") String id, @RequestBody GoalJudgeInvitation response) {
        // Step 1. Constructing goal key
        GoalKey goalKey = new GoalKey(player, id);
        // Step 2. Looking up invitation
        GoalJudgeInvitation invitation = invitationRepository.findOne(goalKey);
        // Step 3. In case some one else replies, not a judge, throw exception
        if  (!requester.equals(invitation.getJudge()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalJudgeOnlyJudgeCanReplay);
        // Step 4. Ignore any changes to invitation, except for status, and save
        return invitationRepository.save(new GoalJudgeInvitation(invitation.getPlayer(), invitation.getJudge(), goalKey, invitation.getGoal(), response.getStatus()));
    }

}
