package com.clemble.casino.player;

public enum PlayerGender {
    M, W;

    public static PlayerGender parse(String gender) {
        // Step 1. Sanity check
        if (gender == null || gender.length() == 0)
            return null;
        // Step 2. Checking for male
        gender = gender.toLowerCase().trim();
        if (gender.startsWith("m")) {
            return PlayerGender.M;
        } else if (gender.startsWith("w") || gender.startsWith("f")) {
            return PlayerGender.W;
        }
        // Step 3. If you can't parse it consider it unknown
        return null;
    }

}
