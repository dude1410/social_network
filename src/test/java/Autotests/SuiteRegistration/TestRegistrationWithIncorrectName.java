package Autotests.SuiteRegistration;

import static Autotests.Settings.SetUpTests.*;
import static Autotests.SuiteRegistration.Locators.*;
import static Autotests.SuiteRegistration.StringData.*;

import Autotests.Settings.SetUpTests;
import Autotests.util.EmailCounter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestRegistrationWithIncorrectName {

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

    @Test(dependsOnMethods = "RegistrationPage_GoToPage_PageOpens", dataProvider = "incorrectNames",
        dataProviderClass = StringData.class, groups = "multipleEmail")
    public void RegistrationPage_FillingInAllFieldsValidDataExceptNameAndSurnameAndRegistration_ErrorMessageInNameAndSurnameField(
        String incorrectName) {
        //arrange
        driver.findElement(emailFieldLocator).sendKeys(email);
        driver.findElement(passwordFieldLocator).sendKeys(CORRECT_PASSWORD);
        driver.findElement(repeatPasswordFieldLocator).sendKeys(CORRECT_PASSWORD);
        driver.findElement(nameFieldLocator).sendKeys(incorrectName);
        driver.findElement(surnameFieldLocator).sendKeys(incorrectName);
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
            invisibilityOfElementLocated(errorMessageInNameFieldLocator).apply(driver),
            "Нет сообщения об ошибке в поле Имя");
        Assert.assertFalse(ExpectedConditions.
            invisibilityOfElementLocated(errorMessageInSurnameFieldLocator).apply(driver),
            "нет сообщения об ошибке в поле Фамилия");
        driver.navigate().refresh();
    }
}
