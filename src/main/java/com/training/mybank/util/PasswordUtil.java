package com.training.mybank.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public static boolean matches(String raw, String hashed) {
        return BCrypt.checkpw(raw, hashed);
    }
}
