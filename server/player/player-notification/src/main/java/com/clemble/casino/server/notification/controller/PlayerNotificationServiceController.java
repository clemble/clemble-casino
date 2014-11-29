package com.clemble.casino.server.notification.controller;

import com.clemble.casino.WebMapping;
import com.clemble.casino.notification.PlayerNotification;
import com.clemble.casino.player.PlayerNotificationWebMapping;
import com.clemble.casino.player.service.PlayerNotificationService;
import com.clemble.casino.server.notification.ServerPlayerNotification;
import com.clemble.casino.server.notification.repository.PlayerNotificationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<ServerPlayerNotification> serverPlayerNotifications = notificationRepository.findByPlayerOrderByCreatedDesc(player);
        PlayerNotification[] notifications = new PlayerNotification[serverPlayerNotifications.size()];
        for(int i = 0; i < notifications.length; i++) {
            notifications[i] = serverPlayerNotifications.get(i).getNotification();
        }
        return notifications;
    }

}
