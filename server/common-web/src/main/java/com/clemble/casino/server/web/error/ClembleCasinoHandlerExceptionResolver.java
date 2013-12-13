package com.clemble.casino.server.web.error;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.error.ClembleCasinoFailureDescription;
import com.clemble.casino.server.error.ClembleCasinoServerException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ClembleCasinoHandlerExceptionResolver implements HandlerExceptionResolver {
    
    final private Logger LOGGER = LoggerFactory.getLogger(ClembleCasinoHandlerExceptionResolver.class);

    final private ObjectMapper objectMapper;

    public ClembleCasinoHandlerExceptionResolver(ObjectMapper objectMapper) {
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LOGGER.error("Error while processing {} with {}", request, handler);
        LOGGER.error("Log trace ", ex);
        ClembleCasinoFailureDescription clembleFailure = null;
        if (ex instanceof ClembleCasinoException) {
            clembleFailure = ((ClembleCasinoException) ex).getFailureDescription();
        } else if(ex instanceof ClembleCasinoServerException) {
            clembleFailure = ((ClembleCasinoServerException) ex).getCasinoException().getFailureDescription();
        } else if (ex instanceof ServletRequestBindingException) {
            Collection<ClembleCasinoError> errors = new ArrayList<ClembleCasinoError>();
            ServletRequestBindingException bindingException = (ServletRequestBindingException) ex;
            if (!bindingException.getMessage().contains("playerId")) {
                errors.add(ClembleCasinoError.BadRequestPlayerIdHeaderMissing);
            }
            if (!bindingException.getMessage().contains("sessionId")) {
                errors.add(ClembleCasinoError.BadRequestSessionIdHeaderMissing);
            }
            if (!bindingException.getMessage().contains("tableId")) {
                errors.add(ClembleCasinoError.BadRequestTableIdHeaderMissing);
            }
            if(errors.size() > 0)
                clembleFailure = new ClembleCasinoFailureDescription().setErrors(errors);
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setHeader("Content-Type", "application/json");
        clembleFailure = clembleFailure == null ? ClembleCasinoFailureDescription.SERVER_ERROR : clembleFailure;

        try {
            objectMapper.writeValue(response.getOutputStream(), clembleFailure);
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }

        return new ModelAndView();
    }
}
