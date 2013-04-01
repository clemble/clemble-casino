package com.gogomaya.server.game.connection;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GameServerConnection {

    @Column(name = "NOTIFICATION_URL")
    private String notificationURL;

    @Column(name = "PUBLISH_URL")
    private String publishURL;

    public GameServerConnection() {
    }

    public GameServerConnection(String notificationURL, String publishURL) {
        this.notificationURL = checkNotNull(notificationURL);
        this.publishURL = checkNotNull(publishURL);
    }

    public String getNotificationURL() {
        return notificationURL;
    }

    public void setNotificationURL(String notificationURL) {
        this.notificationURL = notificationURL;
    }

    public String getPublishURL() {
        return publishURL;
    }

    public void setPublishURL(String publishURL) {
        this.publishURL = publishURL;
    }

}
