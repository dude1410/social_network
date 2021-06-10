package Autotests.SuiteRegistration;

import Autotests.Settings.SetUpTests;
import Autotests.util.HibernateUtil;
import org.openqa.selenium.By;
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

    //Locators
    public final By pageLocator = By.xpath("//div[@class = 'registration']");
    public final By emailFieldLocator = By.xpath("//input[@id = 'register-email']");
    public final By passwordFieldLocator = By.xpath("//input[@id = 'register-password']");
    public final By repeatPasswordFieldLocator = By
        .xpath("//input[@id = 'register-repeat-password']");
    public final By nameFieldLocator = By.xpath("//input[@id = 'register-firstName']");
    public final By surnameFieldLocator = By.xpath("//input[@id = 'register-lastName']");
    public final By codeFieldLocator = By.xpath("//input[@id = 'register-number']");
    public final By codeLocator = By.xpath("//span[@class = 'form__code']");
    public final By checkboxLocator = By.xpath("//input[@id = 'register-confirm']");
    public final By submitRegistrationButtonLocator = By.xpath("//button[@type = 'submit']");
    public final By successRegistrationElementLocator = By
        .xpath("//div[@class = 'success-register']");


    @BeforeTest
    public void setUpTest() {
        driver = SetUpTests.driver;
        wait = SetUpTests.wait;
        actions = SetUpTests.actions;
        driver.navigate().to(SetUpTests.REGISTRATION_PAGE);
    }

    @Test
    public void RegistrationPage_GoToPage_PageOpens() {
        Assert.assertEquals(driver.getTitle(), StringData.REGISTRATION_PAGE_TITLE,
            "Тайтл страницы не соотвествует ожидаемому: " + StringData.REGISTRATION_PAGE_TITLE +
                "\nТекущий тайтл: " + driver.getTitle());
        Assert.assertFalse(ExpectedConditions.invisibilityOfElementLocated(pageLocator)
                .apply(driver),
            "На странице отсутствует ожидаемый элемент по локатору: " + pageLocator);
    }

    @Test(dependsOnMethods = "RegistrationPage_GoToPage_PageOpens", dataProvider = "incorrectEmails",
        dataProviderClass = StringData.class)
    public void RegistrationPage_FillingInAllFieldsValidDataExceptEmailAndRegistration_ErrorMessageInEmailField(String incorrectEmail) {
        //arrange
        driver.findElement(emailFieldLocator).clear();
        driver.findElement(emailFieldLocator).sendKeys(incorrectEmail);
        driver.findElement(passwordFieldLocator).sendKeys(StringData.CORRECT_PASSWORD);
        driver.findElement(repeatPasswordFieldLocator).sendKeys(StringData.CORRECT_PASSWORD);
        driver.findElement(nameFieldLocator).sendKeys(StringData.CORRECT_NAME);
        driver.findElement(surnameFieldLocator).sendKeys(StringData.CORRECT_NAME);
        driver.findElement(codeFieldLocator)
            .sendKeys(driver.findElement(codeLocator).getText());

        WebElement element = driver.findElement(checkboxLocator);
        driver.executeScript("arguments[0].style.width = '20px';", element);
        driver.executeScript("arguments[0].style.height = '20px';", element);
        driver.executeScript("arguments[0].style.opacity = '1';", element);
        driver.executeScript("arguments[0].style.visibility = 'visible';", element);
        driver.findElement(checkboxLocator).click();

        WebElement submitButton = driver.findElement(submitRegistrationButtonLocator);
        actions.moveToElement(submitButton).click(submitButton).build().perform();

        wait.until(ExpectedConditions.presenceOfElementLocated(successRegistrationElementLocator));
        String currentURL = driver.getCurrentUrl();
        Assert.assertEquals(currentURL, SetUpTests.SUCCESSFUL_REGISTRATION_PAGE,
            "Не произошел переход на страницу с подтверждением почты");
    }
}
