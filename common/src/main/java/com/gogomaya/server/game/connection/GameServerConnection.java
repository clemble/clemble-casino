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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((notificationURL == null) ? 0 : notificationURL.hashCode());
        result = prime * result + ((publishURL == null) ? 0 : publishURL.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameServerConnection other = (GameServerConnection) obj;
        if (notificationURL == null) {
            if (other.notificationURL != null)
                return false;
        } else if (!notificationURL.equals(other.notificationURL))
            return false;
        if (publishURL == null) {
            if (other.publishURL != null)
                return false;
        } else if (!publishURL.equals(other.publishURL))
            return false;
        return true;
    }

}
