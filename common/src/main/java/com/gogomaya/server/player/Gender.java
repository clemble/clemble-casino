package com.gogomaya.server.player;

import static com.google.common.base.Strings.isNullOrEmpty;

public enum Gender {
    M, W;

    public static Gender parse(String gender) {
        // Step 1. Sanity check
        if (isNullOrEmpty(gender))
            return null;
        // Step 2. Checking for male
        gender = gender.toLowerCase().trim();
        if (gender.startsWith("m")) {
            return Gender.M;
        } else if (gender.startsWith("w") || gender.startsWith("f")) {
            return Gender.W;
        }
        // Step 3. If you can't parse it consider it unknown
        return null;
    }

}
