package com.clemble.casino.server.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.player.PlayerManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(SpringConfiguration.UNIT_TEST)
@ContextConfiguration(classes = PlayerManagementSpringConfiguration.class)
public class PlayerSocialNetworkRepositoryTest {

    @Autowired
    public PlayerSocialNetworkRepository relationsRepository;

    @Test
    public void testSimpleSave(){
        // Step 1. Generating simple empty relation
        PlayerSocialNetwork A = new PlayerSocialNetwork();
        A.setPlayer(UUID.randomUUID().toString());
        // Step 2. Saving and fetching entity to test a value
        assertNull(A.getId());
        A = relationsRepository.save(A);
        assertNotNull(A.getId());
        // Step 3. Looking up relations
        PlayerSocialNetwork found = relationsRepository.findByPropertyValue("player", A.getPlayer());
        assertEquals(found, A);
    }

    @Test
    public void testSaveWithOwned(){
        // Step 1. Generating simple empty relation
        PlayerSocialNetwork relations = new PlayerSocialNetwork();
        relations.setPlayer(UUID.randomUUID().toString());
        relations.getOwns().add(new PlayerConnectionKey(new ConnectionKey("facebook", "asdsdasdwew")));
        // Step 2. Saving and fetching entity to test a value
        assertNull(relations.getId());
        relations = relationsRepository.save(relations);
        assertNotNull(relations.getId());
        // Step 3. Looking up relations
        PlayerSocialNetwork found = relationsRepository.findByPropertyValue("player", relations.getPlayer());
        assertEquals(found, relations);
    }

    @Test
    public void testSave2WithOwned(){
        // Step 1. Generating simple empty relation
        PlayerSocialNetwork relations = new PlayerSocialNetwork();
        relations.setPlayer(UUID.randomUUID().toString());
        relations.getOwns().add(new PlayerConnectionKey(new ConnectionKey("facebook", "asdsdasdwew")));
        // Step 2. Saving and fetching entity to test a value
        assertNull(relations.getId());
        relations = relationsRepository.save(relations);
        assertNotNull(relations.getId());
        // Step 3. Looking up relations
        PlayerSocialNetwork relations2 = new PlayerSocialNetwork();
        relations2.setPlayer(UUID.randomUUID().toString());
        relations2.getOwns().add(new PlayerConnectionKey(new ConnectionKey("facebook", "asdsdasdwew")));
        assertNull(relations2.getId());
        relations2 = relationsRepository.save(relations2);
        assertNotNull(relations2.getId());
        assertEquals(relations2.getOwns().iterator().next().getId(), relations.getOwns().iterator().next().getId());
    }

    @Test
    @Transactional
    public void testConnectionRealisation() {
        // Step 1. Generating simple empty relation
        PlayerSocialNetwork A = new PlayerSocialNetwork();
        A.setPlayer("A");
        A.getOwns().add(new PlayerConnectionKey(new ConnectionKey("f", "A")));
        A.getConnections().add(new PlayerConnectionKey(new ConnectionKey("f", "B")));
        // Step 2. Saving and fetching entity to test a value
        assertNull(A.getId());
        A = relationsRepository.save(A);
        assertNotNull(A.getId());
        // Step 3. Looking up relations
        PlayerSocialNetwork B = new PlayerSocialNetwork();
        B.setPlayer("B");
        B.getOwns().add(new PlayerConnectionKey(new ConnectionKey("f", "B")));
        B.getConnections().add(new PlayerConnectionKey(new ConnectionKey("f", "A")));
        assertNull(B.getId());
        B = relationsRepository.save(B);
        assertNotNull(B.getId());
        // Step 4. Checking autodiscovery worked
        Iterator<PlayerSocialNetwork> iterator = relationsRepository.findRelations("B").iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), A);
        iterator = relationsRepository.findRelations("A").iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), B);
    }
    
}
