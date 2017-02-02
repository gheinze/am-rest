package com.accounted4.am.rest.security;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility for the generation of ids.
 *
 * @author gheinze
 */
public class GuidGenerator {

    private static final int BITS_PER_BASE64_CHARACTER = 6;
    private static final int BITS_PER_BYTE = 8;
    private static final int DEFAULT_GUID_LENGTH = 8;


    /**
     * Generate a url-safe random id with a default 8 characters in length.
     *
     * @return
     */
    public static String generateRandomId() {
        return generateRandomId(DEFAULT_GUID_LENGTH);
    }


    /**
     * Generate a Base64, url-safe id of a preferred length. The result will be a multiple
     * of 4 characters.
     *
     * @param guidLength preferably a multiple of 4
     * @return
     */
    public static String generateRandomId(int guidLength) {

        int byteBufferSize = guidLength * BITS_PER_BASE64_CHARACTER / BITS_PER_BYTE;

        boolean evenlyDivisible = 0 == (guidLength * BITS_PER_BASE64_CHARACTER) % BITS_PER_BYTE;
        if (!evenlyDivisible) {
            byteBufferSize++;
        }

        SecureRandom random = new SecureRandom();
        byte src[] = new byte[byteBufferSize];
        random.nextBytes(src);

        return Base64.getUrlEncoder().encodeToString(src);

    }

}
