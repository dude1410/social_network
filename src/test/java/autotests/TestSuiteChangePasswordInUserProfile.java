package autotests;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestSuiteChangePasswordInUserProfile {
    public static WebDriver driver;
    public static WebDriverWait wait;

    @BeforeClass
    public static void setUpClass()
    {
        System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        driver = new ChromeDriver(chromeOptions);
        driver.navigate().to("http://31.40.251.201");
        wait = new WebDriverWait(driver, 5);
    }

    @AfterClass
    public static void tearDownClass()
    {
        driver.quit();
    }

    @After
    public void tearDown()
    {
        driver.navigate().to("http://31.40.251.201");
    }

    private By emailField = By.id("login-email");
    private By passwordField = By.id("login-password");
    private By loginButton = By.cssSelector(".btn--white");
    private By profileLink = By.cssSelector(".main-layout__user-name");
    private By editProfileButton = By.cssSelector(".edit");
    private By safety = By.xpath("(//*[@class='aside-filter__item'])[2]");
    private By changeButtonInSafety = By.xpath("(//*[@class='btn'])[2]");
    private By passwordFieldInChangeForm = By.xpath("(//*[@class='form__input'])[1]");
    private By repeatPasswordFieldInSChangeForm = By.xpath("(//*[@class='form__input'])[2]");
    private By changeButtonInChangeForm = By.cssSelector(".modal__button");
    private By popUpMessage = By.cssSelector(".v-snack__content");
    private By titleMainPage = By.cssSelector(".form-layout__title");
    private By title = By.xpath("(//*[@class='main-layout__link'])[1]");
    private By logoutButton = By.xpath("(//*[@class='main-layout__link'])[4]");

    @Test
    public void changePasswordToValid() throws InterruptedException {
        //arrange
        var email = "zerone116@mail.ru";
        var password = "Zerone1166";
        var newPassword = "Zerone116";
        var expectedResultMessage = "Пароль успешно изменён. Авторизуйтесь с новым паролем";
        var expectedResultTitle = " Моя страница";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        Thread.sleep(5000);
        driver.findElement(profileLink).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(editProfileButton));
        driver.findElement(editProfileButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(safety));
        driver.findElement(safety).click();
        driver.findElement(changeButtonInSafety).click();
        driver.findElement(passwordFieldInChangeForm).sendKeys(newPassword);
        driver.findElement(repeatPasswordFieldInSChangeForm).sendKeys(newPassword);
        driver.findElement(changeButtonInChangeForm).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(titleMainPage));

        //assert
        var actualResultMessage = driver.findElement(popUpMessage).getText();
        Assert.assertEquals("Нет сообщения о смене пароля", expectedResultMessage, actualResultMessage);

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(newPassword);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(title));

        //assert
        var actualResultTitle = driver.findElement(title).getText();
        Assert.assertEquals("Пользователь не авторизован с новым email", expectedResultTitle, actualResultTitle);
        driver.findElement(logoutButton).click();
    }

    @Test
    public void changePasswordWithEmptyFields()
    {
        //arrange
        var email = "zerone116@mail.ru";
        var password = "Zerone116";
        var expectedResult = "Введите пароль";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileLink));
        driver.findElement(profileLink).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(editProfileButton));
        driver.findElement(editProfileButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(safety));
        driver.findElement(safety).click();
        driver.findElement(changeButtonInSafety).click();
        driver.findElement(changeButtonInChangeForm).click();

        //assert
        var actualResultEmailFieldError = driver.findElement(By.xpath("(//*[@class='form__error'])[1]")).getText();
        Assert.assertEquals("Пароль сменился", expectedResult, actualResultEmailFieldError);
        var actualResultRepeatEmailFieldError = driver.findElement(By.xpath("(//*[@class='form__error'])[2]")).getText();
        Assert.assertEquals("Пароль сменился", expectedResult, actualResultRepeatEmailFieldError);
    }

    @Test
    public void changePasswordToAnInappropriate() {
        //arrange
        var email = "zerone116@mail.ru";
        var password = "Zerone116";
        var newPassword = "zerone116";
        var expectedResult = "Пароль должен состоять из латинских букв, цифр и знаков. Обязательно содержать одну заглавную букву, одну цифру и состоять из 8 символов.";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(profileLink));
        driver.findElement(profileLink).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(editProfileButton));
        driver.findElement(editProfileButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(safety));
        driver.findElement(safety).click();
        driver.findElement(changeButtonInSafety).click();
        driver.findElement(passwordFieldInChangeForm).sendKeys(newPassword);

        //assert
        var actualResult = driver.findElement(By.cssSelector(".form__password-info")).getText();
        Assert.assertEquals("Не отображается сообщение о требованиях к паролю", expectedResult, actualResult);
    }
}
