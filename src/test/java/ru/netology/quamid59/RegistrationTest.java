package ru.netology.quamid59;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.holdBrowserOpen;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class RegistrationTest {
    public static String getLocalDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("ru")));
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
        holdBrowserOpen = true;
    }

    @Test
    void NotValidCity() {
        String planningDate = getLocalDate(27);
        $x("//input[@placeholder=\"Город\"]").val("Огре");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей Попов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79217777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("Доставка в выбранный город недоступна")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void CityWithSpecSymbols() {
        String planningDate = getLocalDate(8);
        $x("//input[@placeholder=\"Город\"]").val("Москва!");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей Попов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79217777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("недоступна")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void NameWithSecondName() {
        String planningDate = getLocalDate(27);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей Сергеевич");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79217777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").
                shouldHave(text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void DoubleSecondName() {
        String planningDate = getLocalDate(28);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей Попов-Семенов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79217777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").
                shouldHave(text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void NameWithNum() {
        String planningDate = getLocalDate(28);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей 75834");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79217777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("только русские буквы, пробелы и дефисы")).should(visible);
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void NameEnglish() {
        String planningDate = getLocalDate(28);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Sergey Popov");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79217777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("только русские буквы, пробелы и дефисы")).should(visible);
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void NameWithSpecSymbols() {
        String planningDate = getLocalDate(28);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей : Попов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79217777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("только русские буквы, пробелы и дефисы")).should(visible);
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void PhoneWithoutPlus() {
        String planningDate = getLocalDate(40);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей Попов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("89217777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void PhoneWithOneNumber() {
        String planningDate = getLocalDate(40);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей Попов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+7921777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void PhoneWithForm() {
        String planningDate = getLocalDate(40);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей Попов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+7(921)777-77-77");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("11 цифр")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void PhoneEmpty() {
        String planningDate = getLocalDate(40);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей Попов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("обязательно для заполнения")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void NextDayMeeting() {
        String planningDate = getLocalDate(1);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей Попов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79217777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("дату невозможен")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void Plus0days() {
        String planningDate = getLocalDate(0);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей Попов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79217777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("дату невозможен")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void Minus7Days() {
        String planningDate = getLocalDate(-7);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей Попов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79217777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("дату невозможен")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void FebruaryDays() {
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys("30.02.2023");
        $x("//input[@name=\"name\"]").val("Сергей Попов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79217777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $(withText("Неверно введена дата")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void UncheckedBox() {
        String planningDate = getLocalDate(3);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей Попов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79217777777");
        $x("//*[@class=\"checkbox__text\"]").doubleClick();
        $x("//*[@class=\"button__text\"]").click();
        $(".input_invalid[data-test-id=\"agreement\"]").should(exist);
        $x("//*[@data-test-id=\"notification\"]").shouldNot(visible, Duration.ofSeconds(15));

    }

    @Test
    void CheckedBox() {
        String planningDate = getLocalDate(3);
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(planningDate);
        $x("//input[@name=\"name\"]").val("Сергей Попов");
        $x("//*[@data-test-id=\"phone\"]/span/span/input").val("+79217777777");
        $x("//*[@class=\"checkbox__text\"]").click();
        $x("//*[@class=\"button__text\"]").click();
        $("[data-test-id=agreement].checkbox_checked").should(exist);
        $x("//*[@data-test-id=\"notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").
                shouldHave(text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15));
    }

}
