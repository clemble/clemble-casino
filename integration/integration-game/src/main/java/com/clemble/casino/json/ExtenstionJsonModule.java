package com.clemble.casino.json;

import com.clemble.casino.integration.game.NumberState;
import com.clemble.casino.integration.game.SelectNumberEvent;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ExtenstionJsonModule implements ClembleJsonModule {

    @Override
    public Module construct() {
        SimpleModule module = new SimpleModule("Integration");
        module.registerSubtypes(new NamedType(NumberState.class, "number"));
        module.registerSubtypes(new NamedType(SelectNumberEvent.class, "selectNumber"));
        return module;
    }

}
