package autotests;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestSuitePasswordReset {
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

    private By forgotLink = By.cssSelector(".login__link");
    private By fieldEmail = By.id("forgot-email");
    private By buttonSend = By.cssSelector(".btn--white");

    @Test
    public void resetPasswordForRegisteredEmail()
    {
        //arrange
        var email = "zerone201@mail.ru";
        var expectedResultTitle = "На ваш e-mail отправлена ссылка для восстановления пароля";
        var expectedResultText = "В течение 5 минут, на указанную вами почту, придёт письмо со ссылкой. Перейдите по ней, чтобы восстановить пароль.";

        //act
        driver.findElement(forgotLink).click();
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(buttonSend).click();

        //assert
        var actualResultTitle = driver.findElement(By.cssSelector(".form-layout__title")).getText();
        Assert.assertEquals("Пароль не сброшен", expectedResultTitle, actualResultTitle);
        var actualResultText = driver.findElement(By.cssSelector(".form-layout__text")).getText();
        Assert.assertEquals("Не отправленна ссылка для восстановления пароля", expectedResultText, actualResultText);
    }

    @Test
    public void resetPasswordForInvalidEmail()
    {
        //arrange
        var email = "zerone200@mail";
        var expectedResult = "Введите корректный E-mail";

        //act
        driver.findElement(forgotLink).click();
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(buttonSend).click();

        //assert
        var actualResult = driver.findElement(By.cssSelector(".form__error")).getText();
        Assert.assertEquals("Пароль сброшен", expectedResult, actualResult);
    }

    @Test
    public void resetPasswordForUnregisteredEmail()
    {
        //arrange
        var email = "greargeag@gagaega.wq";
        var expectedResultTitle = "Не удалось воссттановить пароль";
        var expectedResultText = "Проверьте правильность e-mail или попробуйте позже.";

        //act
        driver.findElement(forgotLink).click();
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(buttonSend).click();

        //assert
        var actualResultTitle = driver.findElement(By.cssSelector(".form-layout__title")).getText();
        Assert.assertEquals("Пароль сброшен", expectedResultTitle, actualResultTitle);
        var actualResultText = driver.findElement(By.cssSelector(".form-layout__text")).getText();
        Assert.assertEquals("Отправленна ссылка для восстановления пароля", expectedResultText, actualResultText);
    }
}
