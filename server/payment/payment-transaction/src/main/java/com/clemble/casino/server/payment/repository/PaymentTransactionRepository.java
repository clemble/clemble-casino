package com.clemble.casino.server.payment.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.payment.PaymentTransaction;

@Repository
public interface PaymentTransactionRepository extends MongoRepository<PaymentTransaction, String> {

    public List<PaymentTransaction> findByOperationsPlayer(String player);

    public List<PaymentTransaction> findByOperationsPlayerAndTransactionKey(String player, String transactionKey);

    public List<PaymentTransaction> findByOperationsPlayerAndTransactionKeyLike(String player, String transactionKey);

}
