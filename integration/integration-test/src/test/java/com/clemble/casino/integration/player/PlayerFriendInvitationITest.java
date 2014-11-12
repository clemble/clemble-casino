package com.clemble.casino.integration.player;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.player.Invitation;
import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by mavarazy on 11/12/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class PlayerFriendInvitationITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void testInvitationAdded() {
        // Step 1. Creating player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        ClembleCasinoOperations B = playerScenarios.createPlayer();
        // Step 2. Requesting A to connect to B
        A.friendInvitationService().invite(B.getPlayer());
        // Step 3. Checking B received invitation
        List<Invitation> pending = B.friendInvitationService().myInvitations();
        Assert.assertFalse(pending.isEmpty());
        Assert.assertEquals(pending.iterator().next().getPlayer(), A.getPlayer());
    }

    @Test
    public void testInvitationAccept() {
        // Step 1. Creating player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        ClembleCasinoOperations B = playerScenarios.createPlayer();
        // Step 2. Requesting A to connect to B
        A.friendInvitationService().invite(B.getPlayer());
        // Step 3. Checking B received invitation
        List<Invitation> pending = B.friendInvitationService().myInvitations();
        Assert.assertFalse(pending.isEmpty());
        Assert.assertEquals(pending.iterator().next().getPlayer(), A.getPlayer());
        // Step 4. Accepting invitation
        B.friendInvitationService().reply(A.getPlayer(), true);
        // Step 4.1 Checking both players now connected
        Assert.assertEquals(B.connectionOperations().myConnections(), ImmutableSet.of(A.getPlayer()));
        Assert.assertEquals(A.connectionOperations().myConnections(), ImmutableSet.of(B.getPlayer()));
    }

    @Test
    public void testInvitationDecline() {
        // Step 1. Creating player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        ClembleCasinoOperations B = playerScenarios.createPlayer();
        // Step 2. Requesting A to connect to B
        A.friendInvitationService().invite(B.getPlayer());
        // Step 3. Checking B received invitation
        List<Invitation> pending = B.friendInvitationService().myInvitations();
        Assert.assertFalse(pending.isEmpty());
        Assert.assertEquals(pending.iterator().next().getPlayer(), A.getPlayer());
        // Step 4. Accepting invitation
        B.friendInvitationService().reply(A.getPlayer(), false);
        // Step 4.1 Checking both players now connected
        Assert.assertEquals(B.connectionOperations().myConnections().isEmpty(), true);
        Assert.assertEquals(A.connectionOperations().myConnections().isEmpty(), true);
    }
}
