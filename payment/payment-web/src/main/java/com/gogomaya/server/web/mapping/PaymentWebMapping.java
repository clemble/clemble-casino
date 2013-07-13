package com.gogomaya.server.web.mapping;

public class PaymentWebMapping implements WebMapping {

    final public static String WALLET_PREFIX = "/spi";

    final public static String WALLET_PLAYER = "/wallet/{playerId}";
    final public static String WALLET_TRANSACTION = "/player/wallet/transaction";

    private PaymentWebMapping() {
        throw new IllegalAccessError();
    }

}
