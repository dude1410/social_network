package Autotests.SuiteRegistration;

import org.openqa.selenium.By;

public class Locators {

    public static final By pageLocator = By.xpath("//div[@class = 'registration']");
    public static final By emailFieldLocator = By.xpath("//input[@id = 'register-email']");
    public static final By passwordFieldLocator = By.xpath("//input[@id = 'register-password']");
    public static final By repeatPasswordFieldLocator = By.xpath("//input[@id = 'register-repeat-password']");
    public static final By nameFieldLocator = By.xpath("//input[@id = 'register-firstName']");
    public static final By surnameFieldLocator = By.xpath("//input[@id = 'register-lastName']");
    public static final By codeFieldLocator = By.xpath("//input[@id = 'register-number']");
    public static final By codeLocator = By.xpath("//span[@class = 'form__code']");
    public static final By checkboxLocator = By.xpath("//input[@id = 'register-confirm']");
    public static final By submitRegistrationButtonLocator = By.xpath("//button[@type = 'submit']");
    public static final By errorMessageInEmailFieldLocator = By.xpath("//input[@id = 'register-email']/../span");
    public static final By successRegistrationElementLocator = By.xpath("//div[@class = 'success-register']");
    public static final By errorMessageInRepeatPasswordFieldLocator = By.xpath("//input[@id = 'register-repeat-password']/../span");
    public static final By errorMessageInNameFieldLocator = By.xpath("//input[@id = 'register-firstName']/../span");
    public static final By errorMessageInSurnameFieldLocator = By.xpath("//input[@id = 'register-lastName']/../span");
}
