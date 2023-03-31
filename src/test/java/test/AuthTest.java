package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.generateLogin;
import static data.DataGenerator.generatePassword;

public class AuthTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @Test
    void registeredValidUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login] .button__content").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    void unregisteredValidUser() {
        var unregisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(unregisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(unregisteredUser.getPassword());
        $("[data-test-id=action-login] .button__content").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
    void registeredBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login] .button__content").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"))
                .shouldBe(Condition.visible);
    }

    @Test
    void registeredUserWithInvalidLogin() {
        var registeredUser = getRegisteredUser("active");
        var invalidUser = generateLogin();
        $("[data-test-id=login] input").setValue(invalidUser);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login] .button__content").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
    void registeredUserWithInvalidPassword() {
        var registeredUser = getRegisteredUser("active");
        var invalidPassword = generatePassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(invalidPassword);
        $("[data-test-id=action-login] .button__content").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }
}
