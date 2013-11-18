package com.clemble.casino.web.payment;

import com.clemble.casino.web.mapping.WebMapping;

public interface PaymentWebMapping extends WebMapping {

    /**
     * Supports POST, that can only be used by the server
     */
    final public static String PAYMENT_ACCOUNTS = "/account";

    /**
     * Supports GET, that can only be used by the owner
     */
    final public static String PAYMENT_ACCOUNTS_PLAYER = "/account/{player}";
    final public static String PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS = "/account/{player}/transaction";

    /**
     * Supports POST, that can only be used by the server
     */
    final public static String PAYMENT_TRANSACTIONS = "/transaction";
    /**
     * Supports GET, that can only be used by the owners
     */
    final public static String PAYMENT_TRANSACTIONS_TRANSACTION = "/transaction/{source}/{transaction}";

}
