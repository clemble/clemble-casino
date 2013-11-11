package org.springframework.social.connect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

abstract public class ConnectionKeyMixin {

    @JsonCreator
    public ConnectionKeyMixin(@JsonProperty("providerId") String providerId, @JsonProperty("providerUserId") String providerUserId) {
    }

    @JsonProperty("providerId")
    abstract int getProviderId();

    @JsonProperty("providerUserId")
    abstract int getProviderUserId();

    public static class ConnectionKeyModule extends SimpleModule {

        /**
         * Generated
         */
        private static final long serialVersionUID = -364839850090912814L;

        public ConnectionKeyModule() {
            super("connectionKey", new Version(0, 0, 1, "1.1.0-BUILD-SNAPSHOT", "org.springframework.social", "social-core"));
        }

        @Override
        public void setupModule(SetupContext context) {
            context.setMixInAnnotations(ConnectionKey.class, ConnectionKeyMixin.class);
        }
    }
}
