package com.gogomaya.server.web.error;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;

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
                objectMapper.writeValue(response.getOutputStream(), ((GogomayaException) ex).getErrorCodes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                objectMapper.writeValue(response.getOutputStream(), GogomayaError.ServerError);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
