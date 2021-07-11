package autotests.SuiteRegistration;

import static autotests.Settings.SetUpTests.*;
import static autotests.SuiteRegistration.Locators.*;
import static autotests.SuiteRegistration.StringData.*;

import autotests.Settings.SetUpTests;
import autotests.util.EmailCounter;
import autotests.util.HibernateUtil;
import javapro.config.Config;
import javapro.model.Person;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestRegistrationWithValidData {

    private ChromeDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private JavascriptExecutor js;

    //Strings
    private String email;
    private String name = CORRECT_NAME;
    private String password = CORRECT_PASSWORD;


    private Person person;
    private BCryptPasswordEncoder bCrypt;

    @BeforeTest
    public void setUpTest() {
        driver = SetUpTests.driver;
        wait = SetUpTests.wait;
        actions = SetUpTests.actions;
        js = driver;
        driver.navigate().to(REGISTRATION_PAGE);
        bCrypt = new BCryptPasswordEncoder(Config.INT_AUTH_BCRYPT_STRENGTH);

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

    @Test(dependsOnMethods = "RegistrationPage_GoToPage_PageOpens")
    public void RegistrationPage_CheckDatabase_DatabaseNotContainCurrentEmail() {
        Assert.assertNull(HibernateUtil.getPersonByEmail(email),
            "Введный email (" + email + ") уже есть в базе");
    }

    @Test(dependsOnMethods = "RegistrationPage_CheckDatabase_DatabaseNotContainCurrentEmail")
    public void RegistrationPage_FillingInAllFieldsValidDataAndRegistration_GoToSuccessfulRegistrationPage() {
        //arrange
        driver.findElement(emailFieldLocator).sendKeys(email);
        driver.findElement(passwordFieldLocator).sendKeys(password);
        driver.findElement(repeatPasswordFieldLocator).sendKeys(password);
        driver.findElement(nameFieldLocator).sendKeys(name);
        driver.findElement(surnameFieldLocator).sendKeys(name);
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
        wait.until(ExpectedConditions.presenceOfElementLocated(successRegistrationElementLocator));
        Assert.assertEquals(driver.getCurrentUrl(), SUCCESSFUL_REGISTRATION_PAGE,
            "Не произошел переход на страницу с подтверждением почты");
    }

    @Test(dependsOnMethods = "RegistrationPage_FillingInAllFieldsValidDataAndRegistration_GoToSuccessfulRegistrationPage")
    public void RegistrationPage_CheckDatabase_DatabaseContainCurrentEmail() {
        Assert.assertNotNull(person = HibernateUtil.getPersonByEmail(email),
            "Зарегистрированного email (" + email + ") нет в базе");
    }

    @Test(dependsOnMethods = "RegistrationPage_CheckDatabase_DatabaseContainCurrentEmail")
    public void RegistrationPage_CheckDatabase_EnteredDataCorrespondsToDataInDatabase() {
        String inDatabaseName = person.getFirstName();
        String inDatabaseSurname = person.getLastName();
        String inDatabasePassword = person.getPassword();

        Assert.assertTrue(bCrypt.matches(CORRECT_PASSWORD, inDatabasePassword),
            "Введенный пользователем пароль не совпадает с сохраненным в базе");
        Assert.assertEquals(inDatabaseName, name, "Ввведеное имя: " + name
            + ", не соответсвует записаному в базе: " + inDatabaseName);
        Assert.assertEquals(inDatabaseSurname, name, "Ввведеная фамилия: " + name
            + ", не соответсвует записаной в базе: " + inDatabaseSurname);
    }



}
