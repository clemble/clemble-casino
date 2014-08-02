package com.clemble.casino.server.profile.controller;

import com.clemble.casino.player.service.PlayerImageService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import static com.clemble.casino.web.player.PlayerWebMapping.*;

/**
 * Created by mavarazy on 7/26/14.
 */
@Controller
public class PlayerImageController implements PlayerImageService {

    @Override
    @RequestMapping(value = PROFILE_PLAYER_IMAGE, method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable("player") String player) {
        return new byte[0];
    }

}
