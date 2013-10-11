package com.clemble.casino.game.cell;

import java.util.Collection;

import com.clemble.casino.game.event.client.BetEvent;

abstract public class CellStateFactory {

    abstract public CellState create(Collection<BetEvent> bets);

    abstract public CellState create(String owner, Collection<BetEvent> bets);

    static public CellStateFactory create() {
        return new ExposedCellStateFactory();
    }

    public static class SimpleCellStateFactory extends CellStateFactory {

        @Override
        public CellState create(Collection<BetEvent> bets) {
            return new CellState(bets);
        }

        @Override
        public CellState create(String owner, Collection<BetEvent> bets) {
            return new CellState(owner);
        }

    }

    public static class ExposedCellStateFactory extends CellStateFactory {

        @Override
        public CellState create(Collection<BetEvent> bets) {
            return new ExposedCellState(bets);
        }

        @Override
        public CellState create(String owner, Collection<BetEvent> bets) {
            return new ExposedCellState(owner, bets);
        }

    }
}
