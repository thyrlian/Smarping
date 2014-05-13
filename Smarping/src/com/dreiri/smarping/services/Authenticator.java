package com.dreiri.smarping.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;

import com.dreiri.smarping.persistence.PersistenceManager;

public class Authenticator {
    private static PersistenceManager persistenceManager;
    private static String KEY_PASSWORD = "PASSWORD";

    public static void setup(Activity activity) {
        Authenticator.persistenceManager = new PersistenceManager(activity);
    }

    public static boolean isNewUser() {
        return getHashedPassword() == null;
    }

    public static boolean isPasswordMatching(String password) {
        String storedPassword = getHashedPassword();
        String givenPassword = hashPassword(password);
        return storedPassword.equals(givenPassword);
    }

    public static void hashAndStorePassword(String password) {
        String hashedPassword = hashPassword(password);
        storePassword(hashedPassword);
    }

    private static String hashPassword(String password) {
        MessageDigest messageDigest;
        StringBuffer sb = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            byte byteDigest[] = messageDigest.digest();
            for (int i = 0; i < byteDigest.length; i++) {
                sb.append(Integer.toString((byteDigest[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static void storePassword(String hashedPassword) {
        persistenceManager.saveString(KEY_PASSWORD, hashedPassword);
    }

    private static String getHashedPassword() {
        return persistenceManager.getString(KEY_PASSWORD);
    }
}
