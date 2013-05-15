package com.gogomaya.server.web.error;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        if (ex instanceof GogomayaException) {
            try {
                objectMapper.writeValue(response.getOutputStream(), ((GogomayaException) ex).getFailure());
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                objectMapper.writeValue(response.getOutputStream(), GogomayaFailure.ServerError);
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
