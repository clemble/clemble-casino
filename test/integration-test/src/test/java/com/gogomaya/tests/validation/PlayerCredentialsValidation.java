package com.gogomaya.tests.validation;

import javax.inject.Inject;

import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.springframework.stereotype.Component;

import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.error.GogomayaFailureDescription;
import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.player.security.PlayerCredential;

@Component
public class PlayerCredentialsValidation {

    @Inject
    private GogomayaValidationService gogomayaValidationService;

    private PlayerCredential playerCredential;

    private GogomayaException validationError;

    @Given("user A")
    public void userA() {
        playerCredential = new PlayerCredential().setEmail("test@gmail.com").setPassword("testMe").setPlayerId(-1L);
    }

    @Given("user A has invalid email")
    public void theStockIsTradedAt() {
        playerCredential.setEmail("invalid_email.com");
    }

    @When("user A validated")
    public void validateUserA() {
        try {
            gogomayaValidationService.validate(playerCredential);
        } catch (GogomayaException gogomayaException) {
            this.validationError = gogomayaException;
        }
    }

    @Then("error happened")
    public void test() {
        Assert.assertNotNull(gogomayaValidationService);
    }

    @Then("Then error code returned")
    @Alias("error code returned")
    public void theAlertStatusShouldBe(String status) {
        GogomayaFailureDescription errors = validationError.getFailureDescription();
        Assert.assertEquals(errors.getProblems().size(), 1);
        Assert.assertEquals(errors.getProblems().iterator().next(), "001");
    }

    @When("$user validated")
    @Alias("$user gets validated")
    public GogomayaException gogomayaException(PlayerCredential playerCredential) {
        try {
            gogomayaValidationService.validate(playerCredential);
        } catch (GogomayaException gogomayaException) {
            return gogomayaException;
        }
        return null;
    }
}
