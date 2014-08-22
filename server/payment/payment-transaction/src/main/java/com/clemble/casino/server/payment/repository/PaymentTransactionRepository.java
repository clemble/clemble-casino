package com.clemble.casino.server.payment.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.payment.PaymentTransaction;

@Repository
public interface PaymentTransactionRepository extends MongoRepository<PaymentTransaction, String> {

    public List<PaymentTransaction> findByPaymentOperationsPlayer(String player);

    public List<PaymentTransaction> findByPaymentOperationsPlayerAndTransactionKey(String player, String transactionKey);

    public List<PaymentTransaction> findByPaymentOperationsPlayerAndTransactionKeyLike(String player, String transactionKey);

}
