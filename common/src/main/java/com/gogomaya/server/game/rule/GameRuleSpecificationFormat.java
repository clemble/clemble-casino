package com.gogomaya.server.game.rule;

import java.nio.ByteBuffer;

import com.gogomaya.server.buffer.ByteBufferStream;
import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.bet.BetRuleFormat;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat;
import com.gogomaya.server.game.rule.participant.ParticipantRule;
import com.gogomaya.server.game.rule.participant.ParticipantRuleFormat;
import com.gogomaya.server.game.rule.time.TimeRule;
import com.gogomaya.server.game.rule.time.TimeRuleFormat;
import com.gogomaya.server.player.wallet.CashType;

public class GameRuleSpecificationFormat {

    final private static ByteBufferStream<GameRuleSpecification> BUFFER_STREAM = new CustomGameRuleSpecificationByteBufferStream();

    public static GameRuleSpecification fromByteArray(byte[] buffer) {
        ByteBuffer readBuffer = ByteBuffer.wrap(buffer);
        return BUFFER_STREAM.read(readBuffer);
    }

    public static byte[] toByteArray(GameRuleSpecification gameRuleSpecification) {
        ByteBuffer writeBuffer = ByteBuffer.allocate(32);
        BUFFER_STREAM.write(gameRuleSpecification, writeBuffer);
        return writeBuffer.array();
    }

    public static class CustomGameRuleSpecificationByteBufferStream implements ByteBufferStream<GameRuleSpecification> {

        private ByteBufferStream<ParticipantRule> participantBufferStream = new ParticipantRuleFormat.CustomParticipantRuleByteBufferStream();
        private ByteBufferStream<TimeRule> timeRuleBufferStream = new TimeRuleFormat.CustomTimeRuleByteBufferStream();
        private ByteBufferStream<GiveUpRule> giveUpBufferStream = new GiveUpRuleFormat.CustomGiveUpRuleByteBufferStream();
        private ByteBufferStream<BetRule> betBufferStream = new BetRuleFormat.CustomBetRuleByteBufferStream();

        @Override
        public ByteBuffer write(GameRuleSpecification value, ByteBuffer writeBuffer) {
            writeBuffer.put((byte) value.getCashType().ordinal());

            participantBufferStream.write(value.getParticipationRule(), writeBuffer);
            timeRuleBufferStream.write(value.getTimeRule(), writeBuffer);
            giveUpBufferStream.write(value.getGiveUpRule(), writeBuffer);
            betBufferStream.write(value.getBetRule(), writeBuffer);

            return writeBuffer;
        }

        @Override
        public GameRuleSpecification read(ByteBuffer readBuffer) {
            byte cash = readBuffer.get();
            CashType cashType = cash == CashType.FakeMoney.ordinal() ? CashType.FakeMoney : null;

            ParticipantRule participationRule = participantBufferStream.read(readBuffer);
            TimeRule timeRule = timeRuleBufferStream.read(readBuffer);
            GiveUpRule giveUpRule = giveUpBufferStream.read(readBuffer);
            BetRule betRule = betBufferStream.read(readBuffer);

            return new GameRuleSpecification(cashType, betRule, giveUpRule, timeRule, participationRule);
        }

    }
}
