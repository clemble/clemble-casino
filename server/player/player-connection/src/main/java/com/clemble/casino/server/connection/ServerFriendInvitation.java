package com.clemble.casino.server.connection;

import com.clemble.casino.player.Invitation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

/**
 * Created by mavarazy on 11/11/14.
 */
public class ServerFriendInvitation {

    @Id
    final private String invitation;
    final private String sender;
    final private String receiver;

    @JsonCreator
    public ServerFriendInvitation(@JsonProperty("invitation") String invitation, @JsonProperty("sender") String sender, @JsonProperty("receiver") String receiver) {
        this.invitation = toKey(sender, receiver);
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getInvitation() {
        return invitation;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public Invitation toInvitation() {
        return new Invitation(sender);
    }

    public static String toKey(String sender, String receiver) {
        return sender + ":" + receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerFriendInvitation that = (ServerFriendInvitation) o;

        if (!invitation.equals(that.invitation)) return false;
        if (!receiver.equals(that.receiver)) return false;
        if (!sender.equals(that.sender)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = invitation.hashCode();
        result = 31 * result + sender.hashCode();
        result = 31 * result + receiver.hashCode();
        return result;
    }
}
