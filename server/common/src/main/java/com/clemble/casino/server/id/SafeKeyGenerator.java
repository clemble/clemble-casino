package com.clemble.casino.server.id;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mavarazy on 8/16/14.
 */
public class SafeKeyGenerator<T> implements KeyGenerator {

    // TODO Check SecurityRandom as alternative

    final private int size;
    final private CrudRepository<T, String> targetRepository;

    public SafeKeyGenerator(int size, CrudRepository<T, String> targetRepository) {
        this.size = size;
        this.targetRepository = targetRepository;
    }

    @Override
    public String generate() {
        // Step 1. Generating random alpha numeric string
        String id = RandomStringUtils.randomAlphanumeric(size);
        while (targetRepository.exists(id))
            id = RandomStringUtils.randomAlphanumeric(size);
        // Step 2. Returning random id
        return id;
    }

}
