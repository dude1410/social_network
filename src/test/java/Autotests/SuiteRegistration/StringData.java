package Autotests.SuiteRegistration;

import org.testng.annotations.DataProvider;

public class StringData {

    //Titles
    public static final String REGISTRATION_PAGE_TITLE = "Регистрация";
    public static final String SUCCESSFUL_REGISTRATION_PAGE_TITLE = "Подтвердите регистрацию в письме";

    //Correct
    public static final String CORRECT_NOT_IN_BASE_EMAIL = "bashkovich15+20@yandex.ru";
    public static final String CORRECT_PASSWORD = "TESTtest123";
    public static final String CORRECT_PASSWORD2 = "123testTEST";
    public static final String CORRECT_NAME = "someName";

    //Incorrect
    public static final String INCORRECT_EMAIL1 = "@.";
    public static final String INCORRECT_EMAIL2 = "some@.some";
    public static final String INCORRECT_EMAIL3 = "some@some.";
    public static final String INCORRECT_EMAIL4 = "some@so.me.some";
    public static final String INCORRECT_EMAIL5 = "some@some.123";
    public static final String INCORRECT_PASSWORD = "pass";
    public static final String INCORRECT_NAME_SIZE = "so";
    public static final String INCORRECT_NAME_NUMBERS = "12345";

    @DataProvider(name = "incorrectEmails")
    public static Object[][] getEmails() {
        return new Object[][] {{INCORRECT_EMAIL1}, {INCORRECT_EMAIL2}, {INCORRECT_EMAIL3}, {INCORRECT_EMAIL4},
            {INCORRECT_EMAIL5}};
    }
}
