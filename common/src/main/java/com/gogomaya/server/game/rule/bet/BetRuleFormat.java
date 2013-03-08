package com.gogomaya.server.game.rule.bet;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.player.wallet.CashType;

public class BetRuleFormat {
    
    final private static SerializableString BET_TYPE_TOKEN = new SerializedString("betType");
    final private static SerializableString CASH_TYPE_TOKEN = new SerializedString("cashType");
    final private static SerializableString PRICE_TOKEN = new SerializedString("price");
    final private static SerializableString MIN_BET_TOKEN = new SerializedString("minBet");
    final private static SerializableString MAX_BET_TOKEN = new SerializedString("maxBet");

    public static class CustomBetRuleDeserializer extends JsonDeserializer<BetRule> {

        @Override
        public BetRule deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.nextFieldName(BET_TYPE_TOKEN))
                throw GogomayaException.create(GogomayaError.ClientJsonFormatError);

            BetType betType = BetType.valueOf(jp.nextTextValue());
            CashType cashType = jp.nextFieldName(CASH_TYPE_TOKEN) ? CashType.valueOf(jp.nextTextValue()) : CashType.FakeMoney;

            switch (betType) {
            case Fixed:
                if (!jp.getCurrentName().equals(PRICE_TOKEN.toString()) && !jp.nextFieldName(PRICE_TOKEN))
                    throw GogomayaException.create(GogomayaError.ClientJsonFormatError);
                return FixedBetRule.create(cashType, jp.nextLongValue(0));
            case Limited:
                if (!jp.getCurrentName().equals(MIN_BET_TOKEN.toString()) && !jp.nextFieldName(MIN_BET_TOKEN))
                    throw GogomayaException.create(GogomayaError.ClientJsonFormatError);
                long minBet = jp.nextLongValue(0);
                if (!jp.nextFieldName(new SerializedString("maxBet")))
                    throw GogomayaException.create(GogomayaError.ClientJsonFormatError);
                return LimitedBetRule.create(cashType, minBet, jp.nextLongValue(0));
            case Unlimited:
                return UnlimitedBetRule.create(cashType);
            }
            return null;
        }

    }

    public static class CustomBetRuleSerializer extends JsonSerializer<BetRule> {

        @Override
        public void serialize(BetRule value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            jgen.writeFieldName(BET_TYPE_TOKEN); jgen.writeString(value.getRuleType().name());
            jgen.writeFieldName(CASH_TYPE_TOKEN); jgen.writeString(value.getCashType().name());
            if (value instanceof FixedBetRule) {
                jgen.writeFieldName(PRICE_TOKEN); jgen.writeNumber(((FixedBetRule) value).getPrice());
            } else if (value instanceof LimitedBetRule) {
                jgen.writeFieldName(MIN_BET_TOKEN); jgen.writeNumber(((LimitedBetRule) value).getMinBet());
                jgen.writeFieldName(MAX_BET_TOKEN); jgen.writeNumber(((LimitedBetRule) value).getMaxBet());
            } else if (!(value instanceof UnlimitedBetRule)) {
                throw GogomayaException.create(GogomayaError.ClientJsonInvalidError);
            }
            jgen.writeEndObject();
        }

    }
}
