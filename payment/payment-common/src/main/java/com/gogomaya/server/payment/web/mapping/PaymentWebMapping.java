package com.gogomaya.server.payment.web.mapping;

import com.gogomaya.server.web.mapping.WebMapping;

public interface PaymentWebMapping extends WebMapping {

    final public static String ACCOUNT_PREFIX = "/spi";

    /**
     * Supports POST, that can only be used by the server
     */
    final public static String PAYMENT_ACCOUNTS = "/account";

    /**
     * Supports GET, that can only be used by the owner
     */
    final public static String PAYMENT_ACCOUNTS_PLAYER = "/account/{playerId}";
    final public static String PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS = "/account/{playerId}/transaction";
    final public static String PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS_TRANSACTION = "/account/{playerId}/transaction/{source}/{transactionId}";

    /**
     * Supports POST, that can only be used by the server
     */
    final public static String PAYMENT_TRANSACTIONS = "/transaction";
    /**
     * Supports GET, that can only be used by the owners
     */
    final public static String PAYMENT_TRANSACTIONS_TRANSACTION = "/transaction/{source}/{transactionId}";

}
