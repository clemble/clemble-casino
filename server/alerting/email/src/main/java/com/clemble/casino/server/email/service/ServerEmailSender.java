package com.clemble.casino.server.email.service;

/**
 * Created by mavarazy on 12/8/14.
 */
public interface ServerEmailSender {

    void send(String email, String text);

}
