package Autotests.SuiteRegistration;

import static Autotests.Settings.SetUpTests.*;
import static Autotests.SuiteRegistration.Locators.*;
import static Autotests.SuiteRegistration.StringData.*;

import Autotests.Settings.SetUpTests;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestRegistrationWithIncorrectEmail {

    private ChromeDriver driver;
    private WebDriverWait wait;
    private Actions actions;


    @BeforeTest
    public void setUpTest() {
        driver = SetUpTests.driver;
        wait = SetUpTests.wait;
        actions = SetUpTests.actions;
        driver.navigate().to(REGISTRATION_PAGE);
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

    @Test(dependsOnMethods = "RegistrationPage_GoToPage_PageOpens", dataProvider = "incorrectEmails",
        dataProviderClass = StringData.class, groups = "multipleEmail")
    public void RegistrationPage_FillingInAllFieldsValidDataExceptEmailAndRegistration_ErrorMessageInEmailField(
        String incorrectEmail) {
        //arrange
        driver.findElement(emailFieldLocator).sendKeys(incorrectEmail);
        driver.findElement(passwordFieldLocator).sendKeys(CORRECT_PASSWORD);
        driver.findElement(repeatPasswordFieldLocator).sendKeys(CORRECT_PASSWORD);
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
            invisibilityOfElementLocated(errorMessageInEmailFieldLocator).apply(driver),
            "Нет сообщения об ошибке в поле email");
        Assert.assertEquals(driver.findElement(errorMessageInEmailFieldLocator).getText(), ERROR_MESSAGE_IN_EMAIL,
            "Текст ошибки не соотвествует ожидаемому");
        driver.navigate().refresh();
    }
}
