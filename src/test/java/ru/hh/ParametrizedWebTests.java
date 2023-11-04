package ru.hh;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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
    @ParameterizedTest(name = "Проверка основного поиска по вакансиям. Кейс: {0}")
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
    @ParameterizedTest(name = "Проверка поиска по первой букве компаниии. Буква: {0}; Компания: {1}")
    void searchByTheFirstLetterOfTheCompanyName(char letter, String companyName) {
        open("/employers_list?query=&areaId=113&hhtmFrom=vacancy_search_list");
        $(byAttribute("data-qa","alfabeta-list-link-" + letter)).click();
        $(".HH-MainContent").shouldHave(text(companyName));
    }

    @Tag("searchSuggestion")
    @MethodSource("requestAndSuggestions")
    @ParameterizedTest(name = "Проверка подсказок основного поиска")
    void checkSearchSuggest(String request, List<String> suggestions) {
        open("/");
        $(byAttribute("data-qa", "search-input")).setValue(request);
        $(byAttribute("data-qa", "bloko-suggest-list")).$$("li")
                .shouldHave(CollectionCondition.texts(suggestions));
    }

    static Stream<Arguments> requestAndSuggestions() {
        return Stream.of(
                arguments("Тестировщик", List.of("Тестировщик", "Тестировщик по", "Тестировщик игр",
                        "Тестировщик стажер", "Тестировщик удаленно", "Тестировщик игры",
                        "Тестировщик web", "Тестировщик junior", "Тестировщик qa", "Тестировщик 1с")),
                arguments("Психолог", List.of("Психолог", "Психолог-консультант", "Психология",
                        "Психолог-педагог", "Психологическое", "Психолог тренер", "Психологическое образование",
                        "Психологический центр", "Психолог-психотерапевт", "Психологический"))
        );
    }
}
