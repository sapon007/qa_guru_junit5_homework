package ru.hh;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;

public class ParametrizedWebTests {
    @BeforeAll
    static void beforeAll() {
        Configuration.pageLoadStrategy = "eager";
        Configuration.baseUrl = "https://hh.ru";
        Configuration.browserSize = "1920x1080";
    }

    @Tag("mainSearch")
    @ValueSource(strings = {
            "Тестировщик",
            "Разработчик",
            "Водитель"
    })
    @ParameterizedTest
    void searchForVacancies(String jobTitle) {
        open("/");
        $(byAttribute("data-qa", "search-input")).setValue(jobTitle);
        $(byAttribute("data-qa", "search-button")).click();
        $(byAttribute("data-qa", "vacancy-serp__results")).shouldHave(text(jobTitle));
    }

    @Tag("companySearch")
    @CsvSource(value = {
            "Т, Т1 Консалтинг",
            "Д, Даблби"
    })
    @ParameterizedTest
    void searchByTheFirstLetterOfTheCompanyName(char letter, String companyName) {
        open("/employers_list?query=&areaId=113&hhtmFrom=vacancy_search_list");
        $(byAttribute("data-qa","alfabeta-list-link-" + letter)).click();
        $(".HH-MainContent").shouldHave(text(companyName));
    }

    // TODO написать 3-ий параметризованный тест с аннотацией @MethodSource


}
