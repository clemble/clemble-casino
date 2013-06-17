package com.gogomaya.server.web.error;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.error.GogomayaFailureDescription;

@Controller
public class GogomayaHandlerExceptionResolver implements HandlerExceptionResolver {

    final private ObjectMapper objectMapper;

    public GogomayaHandlerExceptionResolver(ObjectMapper objectMapper) {
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        GogomayaFailureDescription gogomayaFailure = null;
        if (ex instanceof GogomayaException) {
            gogomayaFailure = ((GogomayaException) ex).getFailureDescription();
        } else if (ex instanceof ServletRequestBindingException) {
            Collection<GogomayaError> errors = new ArrayList<GogomayaError>();
            ServletRequestBindingException bindingException = (ServletRequestBindingException) ex;
            if (!bindingException.getMessage().contains("playerId")) {
                errors.add(GogomayaError.BadRequestPlayerIdHeaderMissing);
            }
            if (!bindingException.getMessage().contains("sessionId")) {
                errors.add(GogomayaError.BadRequestSessionIdHeaderMissing);
            }
            if (!bindingException.getMessage().contains("tableId")) {
                errors.add(GogomayaError.BadRequestTableIdHeaderMissing);
            }
            if(errors.size() > 0)
                gogomayaFailure = GogomayaFailureDescription.create(errors);
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setHeader("Content-Type", "application/json");
        gogomayaFailure = gogomayaFailure == null ? GogomayaFailureDescription.SERVER_ERROR : gogomayaFailure;

        try {
            objectMapper.writeValue(response.getOutputStream(), gogomayaFailure);
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }

        return new ModelAndView();
    }
}
