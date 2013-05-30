package com.gogomaya.server.error;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cloudfoundry.org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gogomaya.server.error.GogomayaErrorFormat.GogomayaErrorSerializer;

@JsonSerialize(using = GogomayaErrorSerializer.class)
public enum GogomayaError {

    ServerError(Code.SERVER_ERROR_CODE, "Server error"),
    ServerCriticalError(Code.SERVER_CRITICAL_ERROR_CODE, "Server critical error"),
    ServerSessionProcessingError(Code.SERVER_SESSION_PROCESSING_CRITICAL_CODE, "Server session processing error"),
    
    BadRequestPlayerIdHeaderMissing(Code.BAD_REQUEST_PLAYER_ID_HEADER_MISSING, "Player ID missing from the Header"),
    BadRequestSessionIdHeaderMissing(Code.BAD_REQUEST_SESSION_ID_HEADER_MISSING, "Session ID missing from the Header"),
    BadRequestTableIdHeaderMissing(Code.BAD_REQUEST_TABLE_ID_HEADER_MISSING, "Table ID missing from the Header"),

    EmailInvalid(Code.EMAIL_INVALID_CODE, "Email invalid"),
    EmailNotConfirmed(Code.EMAIL_NOT_CONFIRMED_CODE, "Email not confirmed"),
    EmailNotRegistered(Code.EMAIL_NOT_REGISTERED_CODE, "Email not registered"),
    EmailAlreadyRegistered(Code.EMAIL_ALREADY_REGISTERED_CODE, "Email already registered"),
    PasswordMissingCode(Code.PASSWORD_MISSING_CODE, "Password is missing"),
    PasswordTooShort(Code.PASSWORD_TOO_SHORT_CODE, "Password too short"),
    PasswordTooWeak(Code.PASSWORD_TOO_WEAK_CODE, "Password too weak"),
    PasswordTooLong(Code.PASSWORD_TOO_LONG_CODE, "Password too long"),
    EmailOrPasswordIncorrect(Code.EMAIL_OR_PASSWORD_INCORRECT_CODE, "Email or password is incorrect"),

    IdentityInvalid(Code.IDENTITY_INVALID_CODE, "Identity invalid"),

    NickInvalid(Code.NICK_INVALID_CODE, "Nick invalid"),
    NickTooLong(Code.NICK_TOO_LONG_CODE, "Nick too long"),
    FirstNameTooLong(Code.FIRST_NAME_TOO_LONG_CODE, "First name too long"),
    LastNameTooLong(Code.LAST_NAME_TOO_LONG_CODE, "Last name too long"),
    BirthDateInvalid(Code.BIRTH_DATE_INVALID_CODE, "Birth date invalid"),
    ImageURLInvalid(Code.IMAGE_URL_INVALID_CODE, "Image URL invalid"),

    SocialConnectionProviderUserNull(Code.SOCIAL_CONNECTION_PROVIDER_USER_NULL_CODE, "Social connection provider User can't be NULL"),
    SocialConnectionProviderIdNull(Code.SOCIAL_CONNECTION_PROVIDER_ID_NULL_CODE, "Social connection provider ID can't be NULL"),
    SocialConnectionInvalid(Code.SOCIAL_CONNECTION_INVALID_CODE, "Social connection is invalid"),

    ClientJsonInvalidError(Code.CLIENT_JSON_INVALID_ERROR_CODE, "Invalid Json"),
    ClientJsonFormatError(Code.CLIENT_JSON_FORMAT_ERROR_CODE, "Incorrect Json"),

    GameSpecificationInvalid(Code.GAME_SPECIFICATION_INVAID_CODE, "Provided game specification is invalid"),
    
    GamePlayGameEnded(Code.GAMEPLAY_GAME_ENDED_CODE, "Game already ended"),
    GamePlayGameNotStarted(Code.GAMEPLAY_GAME_NOT_STARTED_CODE, "Game has not started yet"),

    GamePlayStayUndefined(Code.GAMEPLAY_STATE_UNDEFINED_CODE, "State is missing or illegal"),
    GamePlayMoveUndefined(Code.GAMEPLAY_MOVE_UNDEFINED_CODE, "Move was not defined properly"),
    GamePlayMoveAlreadyMade(Code.GAMEPLAY_MOVE_ALREADY_MADE_CODE, "Player already made a move, this one will be ignored"),
    GamePlayNoMoveExpected(Code.GAMEPLAY_NO_MOVE_EXPECTED_CODE, "No move expected from the player"),
    GamePlayPlayerNotParticipate(Code.GAMEPLAY_PLAYER_NOT_PARTICIPATING, "Player is not part of the current game"),
    GamePlayWrongMoveType(Code.GAMEPLAY_WRONG_MOVE_TYPE_CODE, "Wrong move type"),
    GamePlayMoveNotSupported(Code.GAMEPLAY_MOVE_NOT_SUPPORTED_CODE, "Game does not support this move"),

    TicTacToeCellOwned(Code.TIC_TAC_TOE_CELL_OWNED_CODE, "Cell already Owned"),

    GameStateReCreationFailure(Code.GAME_STATE_RECREATION_FAILURE_CODE, "Can't create state from provided session"),

    PaymentTransactionInvalid(Code.PAYMENT_TRANSACTION_INVALID_CODE, "Payment transaction invalid"),
    PaymentTransactionEmpty(Code.PAYMENT_TRANSACTION_EMPTY_CODE, "Payment transaction empty");

    final private static Map<String, GogomayaError> REGISTERED_ERRORS = new HashMap<String, GogomayaError>();

    static {
        Set<String> existingCodes = new HashSet<String>();
        try {
            for (Field field : GogomayaError.Code.class.getFields())
                if ((field.getModifiers() & Modifier.STATIC) > 0 && field.getName().endsWith("_CODE")) {
                    String fieldValue = String.valueOf(field.get(GogomayaError.Code.class));
                    if (existingCodes.contains(fieldValue))
                        throw new IllegalArgumentException();
                    existingCodes.add(fieldValue);
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

    @JsonProperty("code")
    final private String code;
    @JsonProperty("description")
    final private String description;

    private GogomayaError(final String newCode, final String newDescription) {
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

    public static class Code {
        final public static String SERVER_ERROR_CODE = "0C0";
        final public static String SERVER_CRITICAL_ERROR_CODE = "0C1";
        final public static String SERVER_SESSION_PROCESSING_CRITICAL_CODE = "0C2";
        // Credentials errors
        // Creation problems
        // Email
        final public static String EMAIL_INVALID_CODE = "001";
        final public static String EMAIL_NOT_CONFIRMED_CODE = "002";
        final public static String EMAIL_NOT_REGISTERED_CODE = "003";
        final public static String EMAIL_ALREADY_REGISTERED_CODE = "004";
        // Password
        final public static String PASSWORD_MISSING_CODE = "010";
        final public static String PASSWORD_TOO_SHORT_CODE = "011";
        final public static String PASSWORD_TOO_WEAK_CODE = "012";
        final public static String PASSWORD_TOO_LONG_CODE = "013";
        // Credentials authentication codes
        final public static String EMAIL_OR_PASSWORD_INCORRECT_CODE = "020";
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
        final public static String SOCIAL_CONNECTION_PROVIDER_ID_NULL_CODE = "090";
        final public static String SOCIAL_CONNECTION_INVALID_CODE = "091";
        final public static String SOCIAL_CONNECTION_PROVIDER_USER_NULL_CODE = "092";
        // Generic Client errors
        final public static String CLIENT_JSON_INVALID_ERROR_CODE = "0D0";
        final public static String CLIENT_JSON_FORMAT_ERROR_CODE = "0D1";
        // Generic Game configuration errors
        final public static String GAME_SPECIFICATION_INVAID_CODE = "0E0";
        // Generic Game play errors
        final public static String GAMEPLAY_MOVE_ALREADY_MADE_CODE = "0F0";
        final public static String GAMEPLAY_NO_MOVE_EXPECTED_CODE = "0F1";
        final public static String GAMEPLAY_WRONG_MOVE_TYPE_CODE = "0F2";
        final public static String GAMEPLAY_MOVE_UNDEFINED_CODE = "0F3";
        final public static String GAMEPLAY_STATE_UNDEFINED_CODE = "0F4";
        final public static String GAMEPLAY_MOVE_NOT_SUPPORTED_CODE = "0F5";
        final public static String GAMEPLAY_PLAYER_NOT_PARTICIPATING = "0F6";
        final public static String GAMEPLAY_GAME_NOT_STARTED_CODE = "0F7";
        final public static String GAMEPLAY_GAME_ENDED_CODE = "0F8";
        // Errors specific for TicTacToe
        final public static String TIC_TAC_TOE_CELL_OWNED_CODE = "0G0";
        // Session recreation failure
        final public static String GAME_STATE_RECREATION_FAILURE_CODE = "0H0";
        // Wallet transaction related errors
        final public static String PAYMENT_TRANSACTION_EMPTY_CODE = "0I0";
        final public static String PAYMENT_TRANSACTION_INVALID_CODE = "0I1";
        // Player id description
        final public static String BAD_REQUEST_PLAYER_ID_HEADER_MISSING = "0J0";
        final public static String BAD_REQUEST_SESSION_ID_HEADER_MISSING = "0J1";
        final public static String BAD_REQUEST_TABLE_ID_HEADER_MISSING = "0J2";
    }
}
