package com.clemble.casino.json;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.clemble.casino.game.cell.CellState;
import com.clemble.casino.game.cell.event.SelectCellEvent;
import com.clemble.casino.game.cell.ExposedCellState;

class GameCellJsonModule implements ClembleJsonModule {

    @Override
    public Module construct() {
        SimpleModule module = new SimpleModule("GameCell");
        module.registerSubtypes(new NamedType(CellState.class, CellState.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(SelectCellEvent.class, SelectCellEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(ExposedCellState.class, ExposedCellState.class.getAnnotation(JsonTypeName.class).value()));
        return module;
    }

}
