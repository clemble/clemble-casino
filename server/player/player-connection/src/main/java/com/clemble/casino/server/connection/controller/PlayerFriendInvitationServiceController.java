package com.clemble.casino.server.connection.controller;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.player.Invitation;
import com.clemble.casino.player.service.PlayerFriendInvitationService;
import com.clemble.casino.server.connection.ServerFriendInvitation;
import com.clemble.casino.server.connection.repository.PlayerFriendInvitationRepository;
import com.clemble.casino.server.connection.service.PlayerGraphService;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static com.clemble.casino.player.PlayerConnectionWebMapping.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mavarazy on 11/11/14.
 */
@RestController
public class PlayerFriendInvitationServiceController implements PlayerFriendInvitationService {

    final private PlayerFriendInvitationRepository invitationRepository;
    final private PlayerGraphService connectionService;

    public PlayerFriendInvitationServiceController(
        PlayerFriendInvitationRepository invitationRepository,
        PlayerGraphService connectionService){
        this.invitationRepository = invitationRepository;
        this.connectionService = connectionService;
    }


    @Override
    public List<Invitation> myInvitations() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = GET, value = MY_INVITATIONS)
    @ResponseStatus(OK)
    public List<Invitation> myInvitations(@CookieValue("player") String me) {
        return invitationRepository.
            findByReceiver(me).
            stream().
            map(invitation -> invitation.toInvitation()).
            collect(Collectors.toList());
    }

    @Override
    public Invitation invite(String player) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = POST, value = MY_INVITATIONS)
    @ResponseStatus(CREATED)
    public Invitation invite(@CookieValue("player") String me, @RequestBody Invitation invitation) {
        if (invitationRepository.findByReceiverAndSender(invitation.getPlayer(), me).isEmpty()) {
            // Case 1. If there is no pending invitation from receiver, add new invitation
            return invitationRepository.save(new ServerFriendInvitation(null, me, invitation.getPlayer())).toInvitation();
        } else {
            // Case 2. If there is a pending invitation, just reply positive
            return reply(me, invitation.getPlayer(), true);
        }
    }

    @Override
    public Invitation reply(String player, boolean accept) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = POST, value = MY_INVITATIONS_REPLY)
    @ResponseStatus(CREATED)
    public Invitation reply(@CookieValue("player") String me, @PathVariable("player") String player, @RequestBody boolean accept) {
        // Step 1. Checking invitation exists
        // Key is actually player - me, since player is the sender
        ServerFriendInvitation invitation = invitationRepository.findOne(ServerFriendInvitation.toKey(player, me));
        if (invitation == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerNoInvitation);
        // Step 2. Removing invitation
        invitationRepository.delete(invitation.getInvitation());
        // Step 3. If it's an accept add connection
        if(accept) {
            connectionService.connect(me, player);
        }
        return invitation.toInvitation();
    }
}
