package com.gogomaya.server.integration.client;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.login.LoginException;

import net.ser1.stomp.Client;
import net.ser1.stomp.Listener;

public class StompClient {
    
    private static Listener SIMPLE_LISTENER = new Listener() {
        @Override
        public void message(Map arg0, String arg1) {
            System.out.println("RM");
            System.out.println("RM Map " + arg0);
            System.out.println("RM String " + arg1);
        }
    };

    public static void main(String args[]) {
        Client c = null;
        try {
            c = new Client("ec2-50-16-93-157.compute-1.amazonaws.com", 61613, "guest", "guest");
            c.subscribe("/topic/1", SIMPLE_LISTENER);
            c.subscribe("/topic/2", SIMPLE_LISTENER);
            c.subscribe("/topic/3", SIMPLE_LISTENER);
            c.subscribe("/exchange/1", SIMPLE_LISTENER);
            System.out.println("Connected " + c.isConnected());
            Thread.sleep(600000);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (c != null)
                c.disconnect();
        }
        // c.subscribe( "foo-channel", my_listener );
        // c.subscribe( "foo-channel", other_listener );
        // c.unsubscribe( "foo-channel", other_listener ); // Unsubscribe only one listener
        // c.unsubscribe( "foo-channel" ); // Unsubscribe all listeners
    }
}
