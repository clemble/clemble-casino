package com.gogomaya.server.web.mapping;

public class PaymentWebMapping implements WebMapping {

    final public static String WALLET_PREFIX = "/spi";

    final public static String WALLET_PLAYER = "/wallet/{playerId}";
    final public static String WALLET_PLAYER_TRANSACTIONS = "/wallet/{playerId}/transaction";
    final public static String WALLET_PLAYER_TRANSACTIONS_TRANSACTION = "/wallet/{playerId}/transaction/{source}/{transactionId}";

    final public static String WALLET_REGISTER = "/register";

    final public static String WALLET_TRANSACTIONS = "/transaction"; // Not used yet
    final public static String WALLET_TRANSACTIONS_TRANSACTION = "/transaction/{source}/{transactionId}";

    private PaymentWebMapping() {
        throw new IllegalAccessError();
    }

}
