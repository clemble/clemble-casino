package com.clemble.casino.json;

import java.util.Map.Entry;

import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionKeyMixin;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class GenericJsonModule implements ClembleJsonModule {

    @Override
    public Module construct() {
        SimpleModule genericModule = new SimpleModule("Generic") {
            private static final long serialVersionUID = 2966716509403546212L;

            @Override
            public void setupModule(SetupContext context) {
                context.setMixInAnnotations(ConnectionKey.class, ConnectionKeyMixin.class);
            }

        };
        genericModule.addDeserializer(Entry.class, new CustomEntryDeserializer());
        return genericModule;
    }

}
