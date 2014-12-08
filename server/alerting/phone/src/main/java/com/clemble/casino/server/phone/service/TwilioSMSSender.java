package com.clemble.casino.server.phone.service;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mavarazy on 12/8/14.
 */
public class TwilioSMSSender implements ServerSMSSender {

    final private Logger LOG = LoggerFactory.getLogger(ServerSMSSender.class);

    final private TwilioRestClient client;

    public TwilioSMSSender(TwilioRestClient twilioRestClient) {
        this.client = twilioRestClient;
    }

    @Override
    public void send(String phone, String sms) {
        try {
            // Step 1. Create a message
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Body", sms));
            params.add(new BasicNameValuePair("To", phone));
            params.add(new BasicNameValuePair("From", "+12562748591"));
            // Step 2. Send message, using message factory
            MessageFactory messageFactory = client.getAccount().getMessageFactory();
            Message message = messageFactory.create(params);
            // Step 3. Log JIC
            LOG.debug("Send a message with SID {}", message.getSid());
         } catch (TwilioRestException e) {
            LOG.error("Failed to send message", e);
        }
    }
}
