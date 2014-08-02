package com.clemble.casino.server.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.UUID;

import com.clemble.casino.server.connection.PlayerConnectionKey;
import com.clemble.casino.server.connection.PlayerConnectionNetwork;
import com.clemble.casino.server.connection.spring.PlayerConnectionSpringConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.clemble.casino.server.connection.repository.PlayerConnectionNetworkRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = PlayerConnectionSpringConfiguration.class)
public class PlayerConnectionNetworkRepositoryTest {

    @Autowired
    public PlayerConnectionNetworkRepository relationsRepository;

    @Test
    public void testSimpleSave(){
        // Step 1. Generating simple empty relation
        PlayerConnectionNetwork A = new PlayerConnectionNetwork();
        A.setPlayer(UUID.randomUUID().toString());
        // Step 2. Saving and fetching entity to test a value
        assertNull(A.getId());
        A = relationsRepository.save(A);
        assertNotNull(A.getId());
        // Step 3. Looking up relations
        PlayerConnectionNetwork found = relationsRepository.findByPlayer(A.getPlayer());
        Assert.assertEquals(found, A);
    }

    @Test
    public void testSaveWithOwned(){
        // Step 1. Generating simple empty relation
        PlayerConnectionNetwork relations = new PlayerConnectionNetwork();
        relations.setPlayer(UUID.randomUUID().toString());
        relations.getOwns().add(new PlayerConnectionKey(new ConnectionKey("facebook", "asdsdasdwew")));
        // Step 2. Saving and fetching entity to test a value
        assertNull(relations.getId());
        relations = relationsRepository.save(relations);
        assertNotNull(relations.getId());
        // Step 3. Looking up relations
        PlayerConnectionNetwork found = relationsRepository.findByPlayer(relations.getPlayer());
        Assert.assertEquals(found, relations);
    }

    @Test
    public void testSave2WithOwned(){
        // Step 1. Generating simple empty relation
        PlayerConnectionNetwork relations = new PlayerConnectionNetwork();
        relations.setPlayer(UUID.randomUUID().toString());
        relations.getOwns().add(new PlayerConnectionKey(new ConnectionKey("facebook", "asdsdasdwew")));
        // Step 2. Saving and fetching entity to test a value
        assertNull(relations.getId());
        relations = relationsRepository.save(relations);
        assertNotNull(relations.getId());
        // Step 3. Looking up relations
        PlayerConnectionNetwork relations2 = new PlayerConnectionNetwork();
        relations2.setPlayer(UUID.randomUUID().toString());
        relations2.getOwns().add(new PlayerConnectionKey(new ConnectionKey("facebook", "asdsdasdwew")));
        assertNull(relations2.getId());
        relations2 = relationsRepository.save(relations2);
        assertNotNull(relations2.getId());
        Assert.assertEquals(relations2.getOwns().iterator().next().getId(), relations.getOwns().iterator().next().getId());
    }

    @Test
    @Transactional
    public void testConnectionRealisation() {
        // Step 1. Generating simple empty relation
        PlayerConnectionNetwork A = new PlayerConnectionNetwork();
        A.setPlayer("A");
        A.getOwns().add(new PlayerConnectionKey(new ConnectionKey("f", "A")));
        A.getConnections().add(new PlayerConnectionKey(new ConnectionKey("f", "B")));
        // Step 2. Saving and fetching entity to test a value
        assertNull(A.getId());
        A = relationsRepository.save(A);
        assertNotNull(A.getId());
        // Step 3. Looking up relations
        PlayerConnectionNetwork B = new PlayerConnectionNetwork();
        B.setPlayer("B");
        B.getOwns().add(new PlayerConnectionKey(new ConnectionKey("f", "B")));
        B.getConnections().add(new PlayerConnectionKey(new ConnectionKey("f", "A")));
        assertNull(B.getId());
        B = relationsRepository.save(B);
        assertNotNull(B.getId());
        // Step 4. Checking autodiscovery worked
        Iterator<PlayerConnectionNetwork> iterator = relationsRepository.findRelations("B").iterator();
        assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), A);
        iterator = relationsRepository.findRelations("A").iterator();
        assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), B);
    }
    
}
