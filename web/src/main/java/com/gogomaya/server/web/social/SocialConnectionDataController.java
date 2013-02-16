package com.gogomaya.server.web.social;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.SocialConnectionData;
import com.gogomaya.server.social.SocialConnectionDataAdapter;

@Controller
public class SocialConnectionDataController {

    final private SocialConnectionDataAdapter socialConnectionDataAdapter;

    public SocialConnectionDataController(final SocialConnectionDataAdapter socialConnectionDataAdapter) {
        this.socialConnectionDataAdapter = socialConnectionDataAdapter;
    }

    @RequestMapping(method = RequestMethod.POST, value="/social", produces = "application/json")
    @ResponseStatus(value= HttpStatus.CREATED)
    public @ResponseBody PlayerProfile createUser(@RequestBody SocialConnectionData socialConnectionData) {
        return socialConnectionDataAdapter.adapt(socialConnectionData);
    }

}
