package com.clemble.casino.server.email.service;

import com.google.common.collect.ImmutableList;
import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.*;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;

import java.io.IOException;

/**
 * Created by mavarazy on 12/6/14.
 */
public class MandrillPlayerEmailService implements PlayerEmailService {

    final private MandrillApi mandrillApi;

    public MandrillPlayerEmailService(MandrillApi mandrillApi) {
        this.mandrillApi = mandrillApi;
    }
    @Override
    public void requestVerification(String email) {
        // Step 1. Creating message
        MandrillMessage message = new MandrillMessage();
        message.setSubject("Please verify your email address");
        message.setHtml("<h1>Hi pal!</h1><br />Really, I'm just saying hi!");
        message.setAutoText(true);
        message.setFromEmail("notification@clemble.com");
        message.setFromName("Clemble Notification");
        // Step 2. Adding recipient
        Recipient recipient = new Recipient();
        recipient.setEmail(email);
        message.setTo(ImmutableList.of(recipient));
        // Step 3. Add verification tag
        message.setPreserveRecipients(true);
        message.setTags(ImmutableList.of("verification"));
        // Step 4. Sending message to mandrill API
        try {
            MandrillMessageStatus[] messageStatusReports = mandrillApi.messages().send(message, false);
        } catch (MandrillApiError mandrillApiError) {
            mandrillApiError.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
