package ru.netology.delivery.test;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.conditions.Disabled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class DeliveryTest {

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        open("http://localhost:9999");
        var validUser = DataGenerator.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        SelenideElement form = $(new By.ByTagName("form"));
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(new By.ByClassName("button")).click();

        if (form.$(new By.ByTagName("fieldset")).is(new Disabled())) {
            fail("Форма должна быть в состоянии загрузки");
        }

        SelenideElement successNotification = $("[data-test-id=success-notification]");
        successNotification.$("[class=notification__content]").shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
        assertTrue(successNotification.isDisplayed(), "Уведомление должно быть показано");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(secondMeetingDate);
        form.$(new By.ByClassName("button")).click();

        SelenideElement replanNotification = $("[data-test-id=replan-notification]");
        replanNotification.$("[class=notification__title]").shouldHave(exactText("Необходимо подтверждение"), Duration.ofSeconds(15));
        assertTrue(successNotification.isDisplayed(), "Уведомление о перепланировании должно быть показано");
        replanNotification.$(new By.ByClassName("button")).click();

        successNotification.$("[class=notification__content]").shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15));
        assertTrue(successNotification.isDisplayed(), "Уведомление должно быть показано");
    }
}
