package org.tinyurl.service;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class UrlShortener {
    public String hash(String longUrl) throws Exception {
        String base64Hash = "";
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Update input string in message digest
            md.update(longUrl.getBytes());

            // Generate the hash
            byte[] hashBytes = md.digest();

            // Convert the byte array to Base64
            base64Hash = Base64.getEncoder().encodeToString(hashBytes);

            // Trim the Base64 string to a fixed length (e.g., 20 characters)
            int fixedLength = 7;
            if (base64Hash.length() > fixedLength) {
                base64Hash = base64Hash.substring(0, fixedLength);
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception: " + e);
        }
        return base64Hash;
    }
}
