package com.clemble.casino.server.email.controller;

import com.clemble.casino.player.PlayerEmailWebMapping;
import com.clemble.casino.player.service.PlayerEmailService;
import com.clemble.casino.server.email.service.ServerPlayerEmailService;
import org.apache.http.HttpResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by mavarazy on 12/8/14.
 */
@RestController
public class PlayerEmailServiceController implements PlayerEmailService {

    final private String redirect;
    final private ServerPlayerEmailService playerEmailService;

    public PlayerEmailServiceController(String redirect, ServerPlayerEmailService playerEmailService) {
        this.redirect = redirect;
        this.playerEmailService = playerEmailService;
    }

    @Override
    public boolean verify(@RequestParam("code") String verificationCode) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = PlayerEmailWebMapping.VERIFY)
    public void verify(@RequestParam("code") String verificationCode, HttpServletResponse response) throws IOException {
        if (playerEmailService.verify(verificationCode)) {
            response.sendRedirect(redirect);
        } else {
            response.getWriter().append("Can't verify this code, request again");
        }
    }

}
