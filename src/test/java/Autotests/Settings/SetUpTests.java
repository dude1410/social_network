package Autotests.Settings;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeSuite;

public class SetUpTests {

    //URLs
    public static final String REGISTRATION_PAGE = "http://31.40.251.201/registration";
    public static final String SUCCESSFUL_REGISTRATION_PAGE = "http://31.40.251.201/registration-letter-sent";
    public static final String FAILED_REGISTRATION_PAGE = "http://31.40.251.201/registration-failed";

    public static ChromeDriver driver;
    public static WebDriverWait wait;
    public static Actions actions;

    @BeforeSuite
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 5);
        actions = new Actions(driver);
    }

}
