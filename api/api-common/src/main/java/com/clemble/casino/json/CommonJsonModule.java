package com.clemble.casino.json;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.clemble.casino.base.ExpectedAction;
import com.clemble.casino.DNSBasedServerRegistry;
import com.clemble.casino.player.GuestPlayerProfile;
import com.clemble.casino.player.NativePlayerProfile;
import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.RelocatedPlayerProfile;
import com.clemble.casino.player.SocialPlayerProfile;

class CommonJsonModule implements ClembleJsonModule {

    @Override
    public Module construct() {
        SimpleModule module = new SimpleModule("Common");
        module.registerSubtypes(new NamedType(ExpectedAction.class, ExpectedAction.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(DNSBasedServerRegistry.class, DNSBasedServerRegistry.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(GuestPlayerProfile.class, GuestPlayerProfile.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(NativePlayerProfile.class, NativePlayerProfile.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(PlayerPresence.class, PlayerPresence.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(RelocatedPlayerProfile.class, RelocatedPlayerProfile.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(SocialPlayerProfile.class, SocialPlayerProfile.class.getAnnotation(JsonTypeName.class).value()));
        return module;
    }

}
