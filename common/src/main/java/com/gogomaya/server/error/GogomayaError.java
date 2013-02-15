package com.gogomaya.server.error;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class GogomayaError {

    final public static String SERVER_CRITICAL_ERROR_CODE = "0A0";

    // Credentials errors
    // Creation problems
    // Email
    final public static String EMAIL_INVALID_CODE = "001";
    final public static String EMAIL_NOT_CONFIRMED_CODE = "002";
    // Password
    final public static String PASSWORD_MISSING_CODE = "010";
    final public static String PASSWORD_TOO_SHORT_CODE = "011";
    final public static String PASSWORD_TOO_WEAK_CODE = "012";
    final public static String PASSWORD_TOO_LONG_CODE = "013";
    // Processing problems
    // Credentials authentication codes
    final public static String EMAIL_OR_PASSWORD_WRONG = "020";

    // Identity errors
    // Processing problems
    final public static String IDENTITY_INVALID_CODE = "030";

    // Gamer Profile management errors
    // Nickname
    final public static String NICK_INVALID_CODE = "040";
    final public static String NICK_TOO_LONG_CODE = "041";
    // First name
    final public static String FIRST_NAME_TOO_LONG_CODE = "050";
    // Last name
    final public static String LAST_NAME_TOO_LONG_CODE = "060";
    // Birth date
    final public static String BIRTH_DATE_INVALID_CODE = "070";
    // Image URL
    final public static String IMAGE_URL_INVALID_CODE = "080";

    // SocialConnectionData
    final public static String SOCIAL_CONNECTION_PROVIDER_NULL_CODE = "090";
    final public static String SOCIAL_CONNECTION_PROVIDER_USER_NULL_CODE = "0A0";
    final public static String SOCIAL_CONNECTION_INVALID_CODE = "0B0";

    final public static Set<String> EXISTING_CODES;
    static {
        EXISTING_CODES = new HashSet<String>();
        try {
            for (Field field : GogomayaError.class.getFields())
                if ((field.getModifiers() & Modifier.STATIC) > 0 && field.getName().endsWith("_CODE")) {
                    String fieldValue = String.valueOf(field.get(GogomayaError.class));
                    if (EXISTING_CODES.contains(fieldValue))
                        throw new IllegalArgumentException();
                    EXISTING_CODES.add(String.valueOf(field.get(GogomayaError.class)));
                }
        } catch (Exception e) {
            throw new RuntimeException("Message processing failed");
        }
    }
}
