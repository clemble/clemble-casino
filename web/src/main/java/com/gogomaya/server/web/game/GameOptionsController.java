package com.gogomaya.server.web.game;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.game.GameOptions;

@Controller
public class GameOptionsController {

    @RequestMapping(method = RequestMethod.GET, value = "/active/options", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody GameOptions create(@RequestHeader("playerId") final long playerId) {
        return GameOptions.DEFAULT;
    }

}
