package com.gogomaya.server.integration.data;

import com.gogomaya.server.player.PlayerCategory;
import com.gogomaya.server.player.PlayerGender;
import com.gogomaya.server.player.PlayerProfile;

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
