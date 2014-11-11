package com.clemble.casino.server.connection.repository;

import com.clemble.casino.server.connection.PlayerGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.social.connect.ConnectionKey;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mavarazy on 11/11/14.
 */
public interface PlayerGraphRepository<T extends PlayerGraph, ID extends Serializable> extends CrudRepository<T, ID> {

    T findByPlayer(String player);

    List<T> findByOwnedIn(Iterable<ConnectionKey> connectionKey);

}
