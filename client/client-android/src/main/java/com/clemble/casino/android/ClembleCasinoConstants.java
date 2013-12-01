package com.clemble.casino.android;

import javax.validation.Validation;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.json.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClembleCasinoConstants {

    final public static ObjectMapper OBJECT_MAPPER = ObjectMapperUtils.createObjectMapper();

    final public static ClembleCasinoValidationService VALIDATION_SERVICE =  new ClembleCasinoValidationService(Validation.buildDefaultValidatorFactory());
}
