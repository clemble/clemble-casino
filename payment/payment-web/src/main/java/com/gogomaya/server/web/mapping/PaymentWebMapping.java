package com.gogomaya.server.web.mapping;

public class PaymentWebMapping implements WebMapping {

    final public static String WALLET_PREFIX = "/spi";

    final public static String WALLET_PLAYER = "/wallet/{playerId}";
    final public static String WALLET_PLAYER_TRANSACTIONS = "/wallet/{playerId}/transaction";
    final public static String WALLET_PLAYER_TRANSACTIONS_TRANSACTION = "/wallet/{playerId}/transaction/{sourse}/{transaction}";

    final public static String WALLET_TRANSACTIONS = "/transaction/{source}/"; // Not used yet
    final public static String WALLET_TRANSACTIONS_TRANSACTION = "/transaction/{source}/{transaction}";

    private PaymentWebMapping() {
        throw new IllegalAccessError();
    }

}
