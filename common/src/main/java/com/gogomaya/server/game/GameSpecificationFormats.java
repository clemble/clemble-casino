package com.gogomaya.server.game;

import java.nio.ByteBuffer;

import com.gogomaya.server.buffer.ByteBufferStream;
import com.gogomaya.server.game.rule.GameRuleSpecification;
import com.gogomaya.server.game.rule.GameRuleSpecificationFormat;
import com.gogomaya.server.game.rule.GameRuleSpecificationFormat.GameRuleSpecificationByteBufferStream;
import com.gogomaya.server.game.table.GameTableSpecification;
import com.gogomaya.server.game.table.GameTableSpecificationFormats;
import com.gogomaya.server.game.table.GameTableSpecificationFormats.GameTableSpecificationByteBufferStream;
import com.gogomaya.server.hibernate.CombinableImmutableUserType;
import com.gogomaya.server.hibernate.ImmutableHibernateType;

public class GameSpecificationFormats {

    final private static ByteBufferStream<GameSpecification> GAME_SPECIFICATION_STREAM = new GameSpecificationByteBufferStream();

    public static GameSpecification fromByteArray(byte[] buffer) {
        ByteBuffer readBuffer = ByteBuffer.wrap(buffer);
        return GAME_SPECIFICATION_STREAM.read(readBuffer);
    }

    public static byte[] toByteArray(GameSpecification gameSpecification) {
        ByteBuffer writeBuffer = ByteBuffer.allocate(32);
        GAME_SPECIFICATION_STREAM.write(gameSpecification, writeBuffer);
        return writeBuffer.array();
    }

    public static class GameSpecificationHibernateType extends CombinableImmutableUserType<GameSpecification> {

        final private static ImmutableHibernateType<GameTableSpecification> HIBERNATE_TABLE_SPECIFICATION = new GameTableSpecificationFormats.GameTableSpecificationHybernateType();
        final private static ImmutableHibernateType<GameRuleSpecification> HIBERNATE_RULE_SPECIFICATION = new GameRuleSpecificationFormat.GameRuleSpecificatinHybernateType();

        public GameSpecificationHibernateType() {
            super(HIBERNATE_TABLE_SPECIFICATION, HIBERNATE_RULE_SPECIFICATION);
        }

        @Override
        public GameSpecification construct(Object[] readValues) {
            return GameSpecification.create((GameTableSpecification) readValues[0], (GameRuleSpecification) readValues[1]);
        }

        @Override
        public Object[] deConstruct(GameSpecification writeValue) {
            return new Object[] { writeValue.getTableSpecification(), writeValue.getRuleSpecification() };
        }

    }

    public static class GameSpecificationByteBufferStream implements ByteBufferStream<GameSpecification> {

        final private static ByteBufferStream<GameRuleSpecification> GAME_RULE_SPECIFICATION_STREAM = new GameRuleSpecificationByteBufferStream();
        final private static ByteBufferStream<GameTableSpecification> GAME_TABLE_SPECIFICATION_STREAM = new GameTableSpecificationByteBufferStream();

        @Override
        public ByteBuffer write(GameSpecification gameSpecification, ByteBuffer writeBuffer) {
            GAME_RULE_SPECIFICATION_STREAM.write(gameSpecification.getRuleSpecification(), writeBuffer);
            GAME_TABLE_SPECIFICATION_STREAM.write(gameSpecification.getTableSpecification(), writeBuffer);
            return writeBuffer;
        }

        @Override
        public GameSpecification read(ByteBuffer readBuffer) {
            GameRuleSpecification ruleSpecification = GAME_RULE_SPECIFICATION_STREAM.read(readBuffer);
            GameTableSpecification tableSpecification = GAME_TABLE_SPECIFICATION_STREAM.read(readBuffer);

            return GameSpecification.create(tableSpecification, ruleSpecification);
        }

    }

}
