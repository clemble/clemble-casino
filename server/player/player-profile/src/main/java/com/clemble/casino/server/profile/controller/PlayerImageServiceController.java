package com.clemble.casino.server.profile.controller;

import com.clemble.casino.player.service.PlayerImageService;
import com.clemble.casino.server.profile.repository.PlayerImageRedirectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.clemble.casino.player.PlayerWebMapping.*;

/**
 * Created by mavarazy on 7/26/14.
 */
@RestController
public class PlayerImageServiceController implements PlayerImageService {

    final private PlayerImageRedirectRepository imageRedirectRepository;

    public PlayerImageServiceController(PlayerImageRedirectRepository imageRedirectRepository) {
        this.imageRedirectRepository = imageRedirectRepository;
    }

    @Override
    public byte[] myImage() {
        throw new IllegalAccessError();
    }

    @RequestMapping(value = MY_IMAGE, method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    public void myImage(@CookieValue("player") String player, HttpServletResponse response) throws IOException {
        getImage(player, response);
    }

    @Override
    public byte[] mySmallImage() {
        throw new IllegalAccessError();
    }

    @RequestMapping(value = MY_IMAGE_SMALL, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    public void mySmallImage(@CookieValue("player") String player, HttpServletResponse response) throws IOException {
        getSmallImage(player, response);
    }

    public byte[] getImage(String player) {
        throw new IllegalAccessError();
    }

    @RequestMapping(value = PLAYER_IMAGE, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    public void getImage(@PathVariable("player") String player, HttpServletResponse response) throws IOException {
        // Step 1. Extracting redirect URL
        String redirect = imageRedirectRepository.findOne(player).getRedirect();
        // Step 2. Sending redirect as a Response
        response.sendRedirect(redirect);
    }

    @Override
    public byte[] getSmallImage(String player) {
        return new byte[0];
    }

    @RequestMapping(value = PLAYER_IMAGE_SMALL, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    public void getSmallImage(@PathVariable("player") String player, HttpServletResponse response) throws IOException {
        // Step 1. Extracting redirect URL
        String redirect = imageRedirectRepository.findOne(player).getSmallImage();
        // Step 2. Sending redirect as a Response
        response.sendRedirect(redirect);
    }

}
