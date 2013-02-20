package com.gogomaya.server.error;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GogomayaError {

    final public static String SERVER_ERROR_CODE = "0C0";
    final public static GogomayaError ServerError = new GogomayaError(SERVER_ERROR_CODE, "Server error");
    final public static String SERVER_CRITICAL_ERROR_CODE = "0C1";
    final public static GogomayaError ServerCriticalError = new GogomayaError(SERVER_CRITICAL_ERROR_CODE, "Server critical error");

    // Credentials errors
    // Creation problems
    // Email
    final public static String EMAIL_INVALID_CODE = "001";
    final public static GogomayaError EmailInvalid = new GogomayaError(EMAIL_INVALID_CODE, "Email invalid");
    final public static String EMAIL_NOT_CONFIRMED_CODE = "002";
    final public static GogomayaError EmailNotConfirmed = new GogomayaError(EMAIL_NOT_CONFIRMED_CODE, "Email not confirmed");
    final public static String EMAIL_NOT_REGISTERED_CODE = "003";
    final public static GogomayaError EmailNotRegistered = new GogomayaError(EMAIL_NOT_REGISTERED_CODE, "Email not registered");
    final public static String EMAIL_ALREADY_REGISTERED_CODE = "004";
    final public static GogomayaError EmailAlreadyRegistered = new GogomayaError(EMAIL_ALREADY_REGISTERED_CODE, "Email already registered");
    // Password
    final public static String PASSWORD_MISSING_CODE = "010";
    final public static GogomayaError PasswordMissingCode = new GogomayaError(PASSWORD_MISSING_CODE, "Password is missing");
    final public static String PASSWORD_TOO_SHORT_CODE = "011";
    final public static GogomayaError PasswordTooShort = new GogomayaError(PASSWORD_TOO_SHORT_CODE, "Password too short");
    final public static String PASSWORD_TOO_WEAK_CODE = "012";
    final public static GogomayaError PasswordTooWeak = new GogomayaError(PASSWORD_TOO_WEAK_CODE, "Password too weak");
    final public static String PASSWORD_TOO_LONG_CODE = "013";
    final public static GogomayaError PasswordTooLong = new GogomayaError(PASSWORD_TOO_LONG_CODE, "Password too long");
    final public static String PASSWORD_IS_INCORRECT_CODE = "014";
    final public static GogomayaError PasswordIsIncorrect = new GogomayaError(PASSWORD_IS_INCORRECT_CODE, "Password is incorrect");
    // Processing problems
    // Credentials authentication codes
    final public static String EMAIL_OR_PASSWORD_INCORRECT_CODE = "020";
    final public static GogomayaError EmailOrPasswordIncorrect = new GogomayaError(EMAIL_OR_PASSWORD_INCORRECT_CODE, "Email & password is incorrect");

    // Identity errors
    // Processing problems
    final public static String IDENTITY_INVALID_CODE = "030";
    final public static GogomayaError IdentityInvalid = new GogomayaError(IDENTITY_INVALID_CODE, "Identity invalid");

    // Gamer Profile management errors
    // Nickname
    final public static String NICK_INVALID_CODE = "040";
    final public static GogomayaError NickInvalid = new GogomayaError(NICK_INVALID_CODE, "Nick invalid");
    final public static String NICK_TOO_LONG_CODE = "041";
    final public static GogomayaError NickTooLong = new GogomayaError(NICK_TOO_LONG_CODE, "Nick too long");
    // First name
    final public static String FIRST_NAME_TOO_LONG_CODE = "050";
    final public static GogomayaError FirstNameTooLong = new GogomayaError(FIRST_NAME_TOO_LONG_CODE, "First name too long");
    // Last name
    final public static String LAST_NAME_TOO_LONG_CODE = "060";
    final public static GogomayaError LastNameTooLong = new GogomayaError(LAST_NAME_TOO_LONG_CODE, "Last name too long");
    // Birth date
    final public static String BIRTH_DATE_INVALID_CODE = "070";
    final public static GogomayaError BirthDateInvalid = new GogomayaError(BIRTH_DATE_INVALID_CODE, "Birth date invalid");
    // Image URL
    final public static String IMAGE_URL_INVALID_CODE = "080";
    final public static GogomayaError ImageURLInvalid = new GogomayaError(IMAGE_URL_INVALID_CODE, "Image URL invalid");

    // SocialConnectionData
    final public static String SOCIAL_CONNECTION_PROVIDER_ID_NULL_CODE = "090";
    final public static GogomayaError SocialConnectionProviderIdNull = new GogomayaError(SOCIAL_CONNECTION_PROVIDER_ID_NULL_CODE,
            "Social connection provider ID can't be NULL");
    final public static String SOCIAL_CONNECTION_PROVIDER_USER_NULL_CODE = "0A0";
    final public static GogomayaError SocialConnectionProviderUserNull = new GogomayaError(SOCIAL_CONNECTION_PROVIDER_USER_NULL_CODE,
            "Social connection provider User can't be NULL");

    final public static String SOCIAL_CONNECTION_INVALID_CODE = "0B0";
    final public static GogomayaError SocialConnectionInvalid = new GogomayaError(SOCIAL_CONNECTION_INVALID_CODE, "Social connection is invalid");

    final private static Map<String, GogomayaError> REGISTERED_ERRORS = new HashMap<String, GogomayaError>();

    static {
        Set<String> existingCodes = new HashSet<String>();
        try {
            for (Field field : GogomayaError.class.getFields())
                if ((field.getModifiers() & Modifier.STATIC) > 0 && field.getName().endsWith("_CODE")) {
                    String fieldValue = String.valueOf(field.get(GogomayaError.class));
                    if (existingCodes.contains(fieldValue))
                        throw new IllegalArgumentException();
                    existingCodes.add(String.valueOf(field.get(GogomayaError.class)));
                }
        } catch (Exception e) {
            throw new RuntimeException("Message processing failed");
        }

        try {
            for (Field field : GogomayaError.class.getFields())
                if ((field.getModifiers() & Modifier.STATIC) > 0 && field.getType().equals(GogomayaError.class)) {
                    GogomayaError gogomayaError = (GogomayaError) (field.get(GogomayaError.class));
                    REGISTERED_ERRORS.put(gogomayaError.getCode(), gogomayaError);
                }
        } catch (Exception e) {
            throw new RuntimeException("Message processing failed");
        }
    }

    final private String code;
    final private String description;

    public GogomayaError(final String newCode, final String newDescription) {
        this.code = newCode;
        this.description = newDescription;
    }

    public static boolean isValid(String code) {
        return REGISTERED_ERRORS.containsKey(code);
    }

    public static GogomayaError forCode(String code) {
        return REGISTERED_ERRORS.get(code);
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
