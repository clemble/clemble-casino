package com.clemble.casino.server.notification.controller;

import com.clemble.casino.WebMapping;
import com.clemble.casino.notification.PlayerNotification;
import com.clemble.casino.player.PlayerNotificationWebMapping;
import com.clemble.casino.player.service.PlayerNotificationService;
import com.clemble.casino.server.notification.repository.PlayerNotificationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Created by mavarazy on 11/29/14.
 */
@RestController
public class PlayerNotificationServiceController implements PlayerNotificationService {

    final private PlayerNotificationRepository notificationRepository;

    public PlayerNotificationServiceController(PlayerNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public PlayerNotification[] myNotifications() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = PlayerNotificationWebMapping.MY_NOTIFICATIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public PlayerNotification[] myNotifications(@CookieValue("player") String player) {
        return notificationRepository.findByPlayerOrderByCreatedDesc(player).toArray(new PlayerNotification[0]);
    }

    @Override
    public void delete(String key) {
        throw new IllegalArgumentException();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = PlayerNotificationWebMapping.MY_NOTIFICATIONS_DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@CookieValue("player") String player, @PathVariable("key") String key) {
        // Step 1. Fetching and checking notifications
        PlayerNotification notification = notificationRepository.findOne(key);
        if (!notification.getPlayer().equals(player))
            throw new IllegalAccessError();
        // Step 2. Removing by key
        notificationRepository.delete(key);
    }

}
