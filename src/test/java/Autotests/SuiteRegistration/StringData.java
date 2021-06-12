package Autotests.SuiteRegistration;

import org.testng.annotations.DataProvider;

public class StringData {

    //Titles
    public static final String REGISTRATION_PAGE_TITLE = "Регистрация";
    public static final String SUCCESSFUL_REGISTRATION_PAGE_TITLE = "Подтвердите регистрацию в письме";

    //Correct
    public static String CORRECT_NOT_IN_BASE_EMAIL = "bashkovich15+31@yandex.ru";
    public static final String CORRECT_PASSWORD = "TESTtest123";
    public static final String CORRECT_PASSWORD2 = "123testTEST";
    public static final String CORRECT_NAME = "someName";

    //Incorrect
    public static final String INCORRECT_EMAIL1 = "@.";
    public static final String INCORRECT_EMAIL2 = "some@.some";
    public static final String INCORRECT_EMAIL3 = "some@some.";
    public static final String INCORRECT_EMAIL4 = "some@№!\".some";
    public static final String INCORRECT_EMAIL5 = "some@some.123";
    @DataProvider(name = "incorrectEmails")
    public static Object[][] getEmails() {
        return new Object[][] {{INCORRECT_EMAIL1}, {INCORRECT_EMAIL2}, {INCORRECT_EMAIL3},
            {INCORRECT_EMAIL4}, {INCORRECT_EMAIL5}};
    }

    public static final String INCORRECT_PASSWORD1 = "pass";
    public static final String INCORRECT_PASSWORD2 = "passpass";
    public static final String INCORRECT_PASSWORD3 = "12345678";
    public static final String INCORRECT_PASSWORD4 = "        ";
    public static final String INCORRECT_PASSWORD5 = "pass1234";
    @DataProvider(name = "incorrectPasswords")
    public static Object[][] getPasswords() {
        return new Object[][] {{INCORRECT_PASSWORD1}, {INCORRECT_PASSWORD2}, {INCORRECT_PASSWORD3},
            {INCORRECT_PASSWORD4}, {INCORRECT_PASSWORD5}};
    }

    public static final String INCORRECT_NAME_SIZE = "so";
    public static final String INCORRECT_NAME_NUMBERS = "12345";
    @DataProvider(name = "incorrectNames")
    public static Object[][] getNames() {
        return new Object[][] {{INCORRECT_NAME_SIZE}, {INCORRECT_NAME_NUMBERS}};
    }

    //ErrorMessages
    public static final String ERROR_MESSAGE_IN_EMAIL = "Введите корректный Email";
    public static final String ERROR_MESSAGE_IN_PASSWORD = "Пароль должен содержать одну заглавную букву и одну цифру";


}
