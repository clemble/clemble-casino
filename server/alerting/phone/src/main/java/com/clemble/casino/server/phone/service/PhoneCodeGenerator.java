package com.clemble.casino.server.phone.service;

import java.util.Random;

/**
 * Created by mavarazy on 12/8/14.
 */
public interface PhoneCodeGenerator {

    String generateCode();

    public static PhoneCodeGenerator noPhoneCodeGenerator() {
        return new NoOpPhoneCodeGenerator("123456");
    }

    public static PhoneCodeGenerator randomPhoneCodeGenerator() {
        return new RandomPhoneCodeGenerator();
    }

    public static class NoOpPhoneCodeGenerator implements PhoneCodeGenerator {

        final private String code;

        public NoOpPhoneCodeGenerator(String code) {
            this.code = code;
        }

        @Override
        public String generateCode() {
            return code;
        }

    }

    public static class RandomPhoneCodeGenerator implements PhoneCodeGenerator {

        final Random RANDOM  = new Random();

        @Override
        public String generateCode() {
            return String.valueOf(Math.abs(RANDOM.nextLong() % 10_000));
        }
    }
}
