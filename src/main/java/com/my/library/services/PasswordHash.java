package com.my.library.services;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PasswordHash  {

    /**
     * Encript user password with SHA-256 to store it in Data Base
     * @param  password      HttpServletRequest request with form data
     * @return          String with encrypted password
     * @see             MessageDigest
     * @throws          UnsupportedEncodingException can be thrown during encryption
     * @throws          NoSuchAlgorithmException can be thrown during encryption
     */

    public static String doHash(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
            byte[] data1 = password.getBytes(StandardCharsets.UTF_8);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(data1);
            byte[] digest = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            password = sb.toString();
            return password;
    }
}
