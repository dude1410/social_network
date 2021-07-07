package autotests.SuiteRegistration;

import static autotests.Settings.SetUpTests.*;
import static autotests.SuiteRegistration.Locators.*;
import static autotests.SuiteRegistration.StringData.*;

import autotests.Settings.SetUpTests;
import autotests.util.EmailCounter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestRegistrationWithIncorrectPassword {

    private ChromeDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    private String email;


    @BeforeTest
    public void setUpTest() {
        driver = SetUpTests.driver;
        wait = SetUpTests.wait;
        actions = SetUpTests.actions;
        driver.navigate().to(REGISTRATION_PAGE);
    }

    @BeforeMethod(onlyForGroups = "multipleEmail")
    public void setEmail() {
        email = CORRECT_NOT_IN_BASE_EMAIL = EmailCounter.nextEmail(CORRECT_NOT_IN_BASE_EMAIL);
    }

    @Test
    public void RegistrationPage_GoToPage_PageOpens() {
        Assert.assertEquals(driver.getTitle(), REGISTRATION_PAGE_TITLE,
            "Тайтл страницы не соотвествует ожидаемому: " + REGISTRATION_PAGE_TITLE +
                "\nТекущий тайтл: " + driver.getTitle());
        Assert.assertFalse(ExpectedConditions.invisibilityOfElementLocated(pageLocator)
                .apply(driver),
            "На странице отсутствует ожидаемый элемент по локатору: " + pageLocator);
    }

    @Test(dependsOnMethods = "RegistrationPage_GoToPage_PageOpens",
        dataProvider = "incorrectPasswords",
        dataProviderClass = StringData.class,
        groups = "multipleEmail")
    public void RegistrationPage_FillingInAllFieldsValidDataExceptPasswordAndRegistration_ErrorMessageInEmailField(String incorrectPass) {
        //arrange
        driver.findElement(emailFieldLocator).sendKeys(email);
        driver.findElement(passwordFieldLocator).sendKeys(incorrectPass);
        driver.findElement(repeatPasswordFieldLocator).sendKeys(incorrectPass);
        driver.findElement(nameFieldLocator).sendKeys(CORRECT_NAME);
        driver.findElement(surnameFieldLocator).sendKeys(CORRECT_NAME);
        driver.findElement(codeFieldLocator)
            .sendKeys(driver.findElement(codeLocator).getText());

        WebElement checkboxElement = driver.findElement(checkboxLocator);
        driver.executeScript("arguments[0].style.width = '20px';", checkboxElement);
        driver.executeScript("arguments[0].style.height = '20px';", checkboxElement);
        driver.executeScript("arguments[0].style.opacity = '1';", checkboxElement);
        driver.executeScript("arguments[0].style.visibility = 'visible';", checkboxElement);
        driver.findElement(checkboxLocator).click();

        //act
        WebElement submitButton = driver.findElement(submitRegistrationButtonLocator);
        actions.moveToElement(submitButton).click(submitButton).build().perform();

        //assert
        Assert.assertFalse(ExpectedConditions.
            invisibilityOfElementLocated(errorMessageInRepeatPasswordFieldLocator).apply(driver),
            "Нет сообщения об ошибке в поле Повторите пароль");
        Assert.assertEquals(driver.findElement(errorMessageInRepeatPasswordFieldLocator).getText(),
            ERROR_MESSAGE_IN_PASSWORD, "Текст ошибки не соотвествует ожидаемому");
        driver.navigate().refresh();
    }

}
