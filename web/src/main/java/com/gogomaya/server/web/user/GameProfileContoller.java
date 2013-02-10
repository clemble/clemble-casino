package com.gogomaya.server.web.user;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.user.GamerProfile;
import com.gogomaya.server.user.GamerProfileRepository;

@Controller
public class GameProfileContoller {

    @Inject
    final private GamerProfileRepository gamerProfileRepository;

    public GameProfileContoller(final GamerProfileRepository gamerProfileRepository) {
        this.gamerProfileRepository = checkNotNull(gamerProfileRepository);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/profile", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody GamerProfile createUser(@RequestBody final GamerProfile gamerProfile) {
        return gamerProfileRepository.saveAndFlush(gamerProfile);
    }
}
