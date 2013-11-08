package com.clemble.casino.player.client;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.oauth.common.signature.RSAKeySecret;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class RSAKeySecretFormat {

    final private static KeyFactory RSA_KEY_FACTORY;
    static {
        try {
            RSA_KEY_FACTORY = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    final public static String PRIVATE_KEY_TAG = "privateKey";
    final public static String PUBLIC_KEY_TAG = "publicKey";

    final public static String KEY_ALGORITHM_TAG = "algorithm";
    final public static String KEY_FORMAT_TAG = "format";
    final public static String KEY_ENCODING_TAG = "encoded";

    public static class KeySerializer extends JsonSerializer<Key> {

        @Override
        public void serialize(Key value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            if (value == null) {
                jgen.writeNull();
                return;
            }

            jgen.writeStartObject();
            jgen.writeStringField(KEY_ALGORITHM_TAG, value.getAlgorithm());
            jgen.writeStringField(KEY_FORMAT_TAG, value.getFormat());
            jgen.writeBinaryField(KEY_ENCODING_TAG, value.getEncoded());
            jgen.writeEndObject();
        }

    }

    abstract public static class AbstractKeyDeserializer<T extends Key> extends JsonDeserializer<T> {

        @Override
        public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() == JsonToken.VALUE_NULL)
                return null;

            String algorithm = null;
            String format = null;
            byte[] encoded = null;
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                if (jp.getText().equals(KEY_ALGORITHM_TAG)) {
                    algorithm = jp.nextTextValue();
                } else if (jp.getText().equals(KEY_ENCODING_TAG)) {
                    jp.nextToken();
                    encoded = jp.getBinaryValue();
                } else if (jp.getText().equals(KEY_FORMAT_TAG)) {
                    format = jp.nextTextValue();
                }
            }
            T key = null;
            try {
                key = doDeserialize(algorithm, format, encoded);
            } catch (InvalidKeySpecException e) {
                // TODO Error handling
            }
            if (key == null)
                throw new IllegalArgumentException("Failed to deserialize a key");
            return key;
        }

        abstract public T doDeserialize(String algorithm, String format, byte[] encoded) throws InvalidKeySpecException;

    }

    public static class PublicKeyDeserializer extends AbstractKeyDeserializer<PublicKey> {

        @Override
        public PublicKey doDeserialize(String algorithm, String format, byte[] encoded) throws InvalidKeySpecException {
            if (!"RSA".equals(algorithm))
                return null;
            if ("X.509".equals(format)) {
                return RSA_KEY_FACTORY.generatePublic(new X509EncodedKeySpec(encoded));
            } else if ("PKCS#8".equals(format))
                return RSA_KEY_FACTORY.generatePublic(new PKCS8EncodedKeySpec(encoded));
            return null;
        }

    }

    public static class PrivateKeyDeserializer extends AbstractKeyDeserializer<PrivateKey> {

        @Override
        public PrivateKey doDeserialize(String algorithm, String format, byte[] encoded) throws InvalidKeySpecException {
            if (!"RSA".equals(algorithm))
                return null;
            if ("X.509".equals(format)) {
                return RSA_KEY_FACTORY.generatePrivate(new X509EncodedKeySpec(encoded));
            } else if ("PKCS#8".equals(format))
                return RSA_KEY_FACTORY.generatePrivate(new PKCS8EncodedKeySpec(encoded));
            return null;
        }

    }

    public static class SecretKeyDeserializer extends AbstractKeyDeserializer<SecretKey> {

        @Override
        public SecretKey doDeserialize(String algorithm, String format, byte[] encoded) throws InvalidKeySpecException {
            return new SecretKeySpec(encoded, algorithm);
        }

    }

    public static class RSAKeySecretSerializer extends JsonSerializer<RSAKeySecret> {

        final public static JsonSerializer<Key> keySerializer = new KeySerializer();

        @Override
        public void serialize(RSAKeySecret value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            if (value == null) {
                jgen.writeNull();
                return;
            }
            jgen.writeStartObject();
            jgen.writeFieldName(PUBLIC_KEY_TAG);
            keySerializer.serialize(value.getPublicKey(), jgen, provider);
            jgen.writeFieldName(PRIVATE_KEY_TAG);
            keySerializer.serialize(value.getPrivateKey(), jgen, provider);
            jgen.writeEndObject();
        }
    }

    public static class RSAKeySecretDeserializer extends JsonDeserializer<RSAKeySecret> {

        private JsonDeserializer<PublicKey> publicKeyDeserializer = new PublicKeyDeserializer();

        private JsonDeserializer<PrivateKey> privateKeyDeserializer = new PrivateKeyDeserializer();

        @Override
        public RSAKeySecret deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() == JsonToken.VALUE_NULL)
                return null;
            PrivateKey privateKey = null;
            PublicKey publicKey = null;
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                if (jp.getText().equals(PRIVATE_KEY_TAG)) {
                    jp.nextToken();
                    privateKey = privateKeyDeserializer.deserialize(jp, ctxt);
                } else if (jp.getText().equals(PUBLIC_KEY_TAG)) {
                    jp.nextToken();
                    publicKey = publicKeyDeserializer.deserialize(jp, ctxt);
                }
            }
            return new RSAKeySecret(privateKey, publicKey);
        }

    }

}
