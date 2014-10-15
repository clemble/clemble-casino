package com.clemble.casino.server.payment.repository;

import com.clemble.casino.payment.PendingTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by mavarazy on 15/10/14.
 */
public interface PendingTransactionRepository extends MongoRepository<PendingTransaction, String>{
}
