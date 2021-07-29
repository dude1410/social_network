package autotests;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class testSuiteAuthorization {
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

    @Test
    public void authorizationWithCorrectData()
    {
        //arrange
        var email = "zerone114@mail.ru";
        var password = "Zerone114";
        var expectedResult = " Моя страница";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".main-layout__logo")));

        //assert
        var actualResult = driver.findElement(By.xpath("(//*[@class='main-layout__link'])[1]")).getText();
        Assert.assertEquals("Пользователь неавторизован", expectedResult, actualResult);
    }

    @Test
    public void authorizationWithAnUnregisteredEmail() throws InterruptedException {
        //arrange
        var email = "zerone0114@mail.ru";
        var password = "Zerone114";
        var expectedResult = "Пользователь с таким email не найден";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        Thread.sleep(2000);

        //assert
        var actualResult = driver.findElement(By.cssSelector(".v-snack__content")).getText();
        Assert.assertEquals("Пользователь авторизован", expectedResult, actualResult);
    }

    @Test
    public void authorizationWithWrongPassword() throws InterruptedException {
        //arrange
        var email = "zerone114@mail.ru";
        var password = "erone114";
        var expectedResult = "Пароль указан неверно.";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        Thread.sleep(2000);

        //assert
        var actualResult = driver.findElement(By.cssSelector(".v-snack__content")).getText();
        Assert.assertEquals("Пользователь авторизован", expectedResult, actualResult);
    }
}