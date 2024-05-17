package cz.cvut.fit.sabirdan.wework.utils;

import java.security.SecureRandom;

public class RandomHexKeyGenerator {
    private static byte[] generateRandomBytes(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return randomBytes;
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            // Convert each byte to its hexadecimal representation
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                // Append leading zero for single-digit hexadecimal values
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public static String generateRandomHexKey(int length) {
        return bytesToHex(generateRandomBytes(length));
    }
}
