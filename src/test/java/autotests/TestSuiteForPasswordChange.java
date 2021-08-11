package autotests;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestSuiteForPasswordChange {
    public static WebDriver driver;
    public static WebDriverWait wait;

    @BeforeClass
    public static void setUpClass()
    {
        System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        driver = new ChromeDriver(chromeOptions);
        driver.navigate().to("http://31.40.251.201/change-password");
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
        driver.navigate().to("http://31.40.251.201/change-password");
    }

    private By fieldEmail = By.id("change-password");
    private By fieldConfirmPassword = By.id("change-repeat-password");
    private By buttonSend = By.cssSelector(".btn--white");

    @Test
    public void changingPasswordToValid() throws InterruptedException {
        //arrange
        var password = "Qwertyui1";
        var expectedResultTitle = "Пароль успешно изменён!";
        var expectedResultText = "Пароль был успешно изменён, Используйте данный пароль для последующих процессов авторизации.";

        //act
        driver.findElement(fieldEmail).sendKeys(password);
        driver.findElement(fieldConfirmPassword).sendKeys(password);
        driver.findElement(buttonSend).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".form-layout__title")));

        //assert
        var actualResultTitle = driver.findElement(By.cssSelector(".form-layout__title")).getText();
        Assert.assertEquals("Пароль не изменен", expectedResultTitle, actualResultTitle);
        var actualResultText = driver.findElement(By.cssSelector(".form-layout__text")).getText();
        Assert.assertEquals("Пароль не изменен", expectedResultText, actualResultText);
    }

    @Test
    public void changingThePasswordToNonCompliant()
    {
        //arrange
        var password = "qwertyui1";
        var expectedResult = "Пароль должен состоять из латинских букв, цифр и знаков. Обязательно содержать одну заглавную букву, одну цифру и состоять из 8 символов.";

        //act
        driver.findElement(fieldEmail).sendKeys(password);
        driver.findElement(buttonSend).click();

        //assert
        var actualResult = driver.findElement(By.cssSelector(".form__password-info")).getText();
        Assert.assertEquals("Пароль изменен", expectedResult, actualResult);
    }

    @Test
    public void enteringDifferentPasswords()
    {
        //arrange
        var password = "Qwertyui1";
        var confirmPassword = "Qwertyui";
        var expectedResult = "Пароли должны совпадать";

        //act
        driver.findElement(fieldEmail).sendKeys(password);
        driver.findElement(fieldConfirmPassword).sendKeys(confirmPassword);
        driver.findElement(buttonSend).click();

        //assert
        var actualResult = driver.findElement(By.xpath("(//*[@class='form__error'])[2]")).getText();
        Assert.assertEquals("Пароль изменен", expectedResult, actualResult);
    }
}
