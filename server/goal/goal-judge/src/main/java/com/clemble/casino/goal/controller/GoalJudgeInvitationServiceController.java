package com.clemble.casino.goal.controller;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.goal.GoalJudgeInvitation;

import static com.clemble.casino.goal.GoalJudgeInvitationWebMapping.*;

import com.clemble.casino.goal.GoalJudgeInvitationStatus;
import com.clemble.casino.goal.repository.GoalJudgeInvitationRepository;
import com.clemble.casino.goal.service.GoalJudgeInvitationService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.event.goal.SystemGoalJudgeInvitationAcceptedEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.WebMapping;
import static org.springframework.http.HttpStatus.*;
import org.springframework.web.bind.annotation.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.Collection;

/**
 * Created by mavarazy on 8/17/14.
 */
@RestController
public class GoalJudgeInvitationServiceController implements GoalJudgeInvitationService, ExternalController {

    final private SystemNotificationService notificationService;
    final private GoalJudgeInvitationRepository invitationRepository;

    public GoalJudgeInvitationServiceController(GoalJudgeInvitationRepository invitationRepository, SystemNotificationService notificationService) {
        this.notificationService = notificationService;
        this.invitationRepository = invitationRepository;
    }

    @Override
    public Collection<GoalJudgeInvitation> myPending() {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = GET, value = MY_INVITATIONS_PENDING,produces = WebMapping.PRODUCES)
    @ResponseStatus(OK)
    public Collection<GoalJudgeInvitation> myPending(@CookieValue("player") String player) {
        return invitationRepository.findByJudgeAndStatus(player, GoalJudgeInvitationStatus.pending);
    }

    @Override
    public Collection<GoalJudgeInvitation> myAccepted() {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = GET, value = MY_INVITATIONS_ACCEPTED, produces = WebMapping.PRODUCES)
    @ResponseStatus(OK)
    public Collection<GoalJudgeInvitation> myAccepted(@CookieValue("player") String player) {
        return invitationRepository.findByJudgeAndStatus(player, GoalJudgeInvitationStatus.accepted);
    }

    @Override
    public Collection<GoalJudgeInvitation> myDeclined() {
        return null;
    }

    @RequestMapping(method = GET, value = MY_INVITATIONS_DECLINED, produces = WebMapping.PRODUCES)
    @ResponseStatus(OK)
    public Collection<GoalJudgeInvitation> myDeclined(@CookieValue("player") String player) {
        return invitationRepository.findByJudgeAndStatus(player, GoalJudgeInvitationStatus.declined);
    }

    @Override
    public GoalJudgeInvitation reply(String goalKey, GoalJudgeInvitation response) {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = PUT, value = INVITATION_REPLY,produces = WebMapping.PRODUCES)
    @ResponseStatus(OK)
    public GoalJudgeInvitation reply(@CookieValue("player") String requester, @PathVariable("goalKey") String goalKey, @RequestBody GoalJudgeInvitation response) {
        // Step 1. Looking up invitation
        GoalJudgeInvitation invitation = invitationRepository.findOne(goalKey);
        // Step 2. In case some one else replies, not a judge, throw exception
        if  (!requester.equals(invitation.getJudge()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalJudgeOnlyJudgeCanReplay, requester, invitation.getJudge());
        // Step 3. Ignore any changes to invitation, except for status, and save
        GoalJudgeInvitation savedInvitation = invitationRepository.save(invitation.cloneWithStatus(response.getStatus()));
        if (response.getStatus() == GoalJudgeInvitationStatus.accepted)
            notificationService.notify(new SystemGoalJudgeInvitationAcceptedEvent(invitation.getGoalKey(), invitation));
        // Step 4. Save changing invitation
        return savedInvitation;
    }

}
