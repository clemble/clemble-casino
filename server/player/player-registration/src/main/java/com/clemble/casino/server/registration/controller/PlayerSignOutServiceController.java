package com.clemble.casino.server.registration.controller;

import com.clemble.casino.registration.RegistrationWebMapping;
import com.clemble.casino.registration.service.PlayerSignOutService;
import com.clemble.casino.server.security.PlayerTokenUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by mavarazy on 1/14/15.
 */
@RestController
public class PlayerSignOutServiceController implements PlayerSignOutService {

    final private String host;
    final private PlayerTokenUtils tokenUtils;

    public PlayerSignOutServiceController(String host, PlayerTokenUtils tokenUtils) {
        this.host = host;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void signOut() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = RegistrationWebMapping.REGISTRATION_SIGN_OUT)
    public void signOut(HttpServletResponse signOut) throws IOException {
        // Step 1. Removing cookies
        tokenUtils.signOut(signOut);
        // Step 2. Redirect to parent
        signOut.sendRedirect(host);
    }

}
