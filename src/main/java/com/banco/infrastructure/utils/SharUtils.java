package com.banco.infrastructure.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SharUtils {
   
    public static String shar(String value) {

        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Falha ao gerar o hash"); 
        }

        messageDigest.update(value.getBytes(StandardCharsets.UTF_8));

        return new BigInteger(1, messageDigest.digest()).toString(16);
    }

}
