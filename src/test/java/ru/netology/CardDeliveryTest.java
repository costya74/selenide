package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.Condition;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;


import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {
    public String generateDate(int planningDate) {
        return LocalDate.now().plusDays(planningDate).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfullyOrderONCard() {
        String planningDate = generateDate(3);
        $("[data-test-id='city'] input").setValue("Пермь");
        $("[data-test-id='date'] input").setValue(String.valueOf(planningDate));
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79021234567");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    public void shouldEmptyFieldIsCity() {
        String planningDate = generateDate(3);
        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(planningDate));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79021234567");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldDeliveryToTheLocalityIsNotAvailable() {
        String planningDate = generateDate(3);
        $("[data-test-id='city'] input").setValue("Чуваки");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(planningDate));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79021234567");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldEmptyDateField() {
        String planningDate = generateDate(3);
        $("[data-test-id='city'] input").setValue("Пермь");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79021234567");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    public void shoulDateEarlierThanThreeDays() {
        String planningDate = generateDate(2);
        $("[data-test-id='city'] input").setValue("Пермь");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(planningDate));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79021234567");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldLastNameAndFirstNameFieldsAreNotFilledIn() {
        String planningDate = generateDate(3);
        $("[data-test-id='city'] input").setValue("Пермь");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(planningDate));
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id='phone'] input").setValue("+79021234567");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldNoCyrillicAndInvalidCharactersInTheSurnameAndFirstNameField() {
        String planningDate = generateDate(3);
        $("[data-test-id='city'] input").setValue("Пермь");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(planningDate));
        $("[data-test-id='name'] input").setValue("Ivanov Ivan");
        $("[data-test-id='phone'] input").setValue("+79021234567");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldTheInvalidFormatForEntering() {
        String planningDate = generateDate(3);
        $("[data-test-id='city'] input").setValue("Пермь");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(planningDate));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("89021234567");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldConsenToDataProcessing() {
        String planningDate = generateDate(3);
        $("[data-test-id='city'] input").setValue("Пермь");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(planningDate));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79021234567");
        $(byText("Забронировать")).click();
        $("[data-test-id=agreement].input_invalid .checkbox__text").should(visible);
    }
}