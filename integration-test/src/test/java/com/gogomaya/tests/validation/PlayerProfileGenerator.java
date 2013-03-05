package com.gogomaya.tests.validation;

import java.util.Date;

import org.jbehave.core.annotations.Given;

import com.gogomaya.server.player.Gender;
import com.gogomaya.server.player.PlayerCategory;
import com.gogomaya.server.player.PlayerProfile;

public class PlayerProfileGenerator {

    @Given("$user")
    public PlayerProfile player(String name) {
        return new PlayerProfile()
            .setBirthDate(new Date(0))
            .setCategory(PlayerCategory.Novice)
            .setFirstName(name)
            .setLastName(name + "Surname")
            .setGender(Gender.M)
            .setImageUrl("http://test.com/test.png")
            .setNickName(name)
            .setPlayerId(3);
    }
    
}
