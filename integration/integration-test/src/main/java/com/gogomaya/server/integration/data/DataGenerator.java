package com.gogomaya.server.integration.data;

import com.gogomaya.player.PlayerCategory;
import com.gogomaya.player.PlayerGender;
import com.gogomaya.player.PlayerProfile;

public class DataGenerator {

    public static PlayerProfile randomProfile() {
        return new PlayerProfile()
            .setCategory(PlayerCategory.Amateur)
            .setFirstName("Anton")
            .setLastName("Oparin")
            .setGender(PlayerGender.M)
            .setNickName("mavarazy");
    }
}
