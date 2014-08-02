package com.clemble.casino.server.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;

@Repository
public interface PaymentTransactionRepository extends MongoRepository<PaymentTransaction, PaymentTransactionKey> {

    public List<PaymentTransaction> findByPaymentOperationsPlayer(String player);

    public List<PaymentTransaction> findByPaymentOperationsPlayerAndTransactionKeySource(String player, String source);

    public List<PaymentTransaction> findByPaymentOperationsPlayerAndTransactionKeySourceLike(String player, String source);

}
