package autotests;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestSuiteSupport {
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

    private By supportLink = By.cssSelector(".form-layout__footer-support");
    private By fieldEmail = By.id("email");
    private By fieldText = By.cssSelector(".support-contacting-text--textarea");
    private By buttonSend = By.cssSelector(".btn--white");

    @Test
    public void sendingAMessageToSupport() throws InterruptedException
    {
        //arrange
        var email = "zerone200@mail.ru";
        var text = "Текст сообщения, чтобы проверить отправку формы";
        var expectedResult = "Ваше обращение в службу поддержки направлено";

        //act
        driver.findElement(supportLink).click();
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(fieldText).sendKeys(text);
        driver.findElement(buttonSend).click();
        Thread.sleep(2000);

        //assert
        var actualResult = driver.findElement(By.cssSelector(".v-snack__content")).getText();
        Assert.assertEquals("Сообщение не отправлено", expectedResult, actualResult);
    }

    @Test
    public void sendingAMessageToSupportWithAnInvalidEmail()
    {
        //arrange
        var email = "zerone200@mail";
        var text = "Текст сообщения, чтобы проверить отправку формы";
        var expectedResult = "Введите корректный E-mail";

        //act
        driver.findElement(supportLink).click();
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(fieldText).sendKeys(text);
        driver.findElement(buttonSend).click();

        //assert
        var actualResult = driver.findElement(By.cssSelector(".form__error")).getText();
        Assert.assertEquals("Сообщение не отправлено", expectedResult, actualResult);
    }
}
