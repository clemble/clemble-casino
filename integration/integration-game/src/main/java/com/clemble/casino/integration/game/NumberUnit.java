package com.clemble.casino.integration.game;

import java.util.List;

import com.clemble.casino.game.lifecycle.management.unit.GameUnit;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("num_unit")
public class NumberUnit implements GameUnit {

    final private List<GameUnit> children;

    @JsonCreator
    public NumberUnit(@JsonProperty("children") List<GameUnit> children) {
        this.children = children;
    }

    public List<? extends GameUnit> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumberUnit fakeUnit = (NumberUnit) o;

        if (children != null ? !children.equals(fakeUnit.children) : fakeUnit.children != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return children != null ? children.hashCode() : 0;
    }

}
