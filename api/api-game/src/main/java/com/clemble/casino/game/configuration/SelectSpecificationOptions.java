package com.clemble.casino.game.configuration;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.utils.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("selectSpecification")
public class SelectSpecificationOptions implements GameSpecificationOptions {

    /**
     * Generated 13/04/13
     */
    private static final long serialVersionUID = 8784325146021105406L;

    final private Game game;

    final private List<? extends GameSpecification> specifications;

    @JsonCreator
    public SelectSpecificationOptions(@JsonProperty("game") Game game, @JsonProperty("specifications") List<? extends GameSpecification> gameSpecifications) {
        this.game = game;
        this.specifications = CollectionUtils.<GameSpecification>immutableList(checkNotNull(gameSpecifications));
    }

    public Game getGame() {
        return game;
    }

    public List<? extends GameSpecification> getSpecifications() {
        return specifications;
    }

    @Override
    public boolean valid(GameSpecification specification) {
        return specifications.contains(specification);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((game == null) ? 0 : game.hashCode());
        result = prime * result + ((specifications == null) ? 0 : specifications.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SelectSpecificationOptions other = (SelectSpecificationOptions) obj;
        if (game != other.game)
            return false;
        if (specifications == null) {
            if (other.specifications != null)
                return false;
        } else if (!specifications.equals(other.specifications))
            return false;
        return true;
    }

}
