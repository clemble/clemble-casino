package com.gogomaya.server.web.error;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.error.GogomayaFailure;

public class GogomayaHandlerExceptionResolver implements HandlerExceptionResolver {

    final ObjectMapper objectMapper;

    public GogomayaHandlerExceptionResolver(ObjectMapper objectMapper) {
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public @ResponseBody
    ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        GogomayaFailure gogomayaFailure = null;
        if (ex instanceof GogomayaException) {
            gogomayaFailure = ((GogomayaException) ex).getFailure();
        } else if (ex instanceof ServletRequestBindingException) {
            ServletRequestBindingException bindingException = (ServletRequestBindingException) ex;
            if (bindingException.getMessage().contains("playerId")) {
                gogomayaFailure = new GogomayaFailure(GogomayaError.BadRequestPlayerIdHeaderMissing);
            } else if (bindingException.getMessage().contains("sessionId")) {
                gogomayaFailure = new GogomayaFailure(GogomayaError.BadRequestSessionIdHeaderMissing);
            } else if (bindingException.getMessage().contains("tableId")) {
                gogomayaFailure = new GogomayaFailure(GogomayaError.BadRequestTableIdHeaderMissing);
            }
        }

        gogomayaFailure = gogomayaFailure == null ? GogomayaFailure.ServerError : gogomayaFailure;

        try {
            objectMapper.writeValue(response.getOutputStream(), gogomayaFailure);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
