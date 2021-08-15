package autotests;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Profile;

import java.util.Calendar;
import java.util.Date;

@Profile("test")
public class TestSuitePosts {

    public static WebDriver driver;
    public static WebDriverWait wait;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        driver = new ChromeDriver(chromeOptions);
        driver.navigate().to("http://31.40.251.201");
        wait = new WebDriverWait(driver, 5);

        //arrange
        var email = "anton-van@pepegamail.com";
        var password = "Pepega11";

        By emailField = By.id("login-email");
        By passwordField = By.id("login-password");
        By loginButton = By.cssSelector(".btn--white");

        //login
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);

        driver.findElement(loginButton).click();

    }

    @AfterClass
    public static void tearDownClass() {
        driver.quit();
    }

    @After
    public void tearDown() {
        driver.navigate().to("http://31.40.251.201");
    }

    private By addPostBtn = By.xpath("//div[@class = 'news-add__block add']");
    private By inputPostTitleField = By.xpath("//input[@class = 'news-add__text-title']");
    private By inputPostBody = By.xpath("//div[@class = 'ProseMirror']");

    private By findPostsBlock = By.xpath("//div[@class = 'news-block']");

    private By createPostBtn = By.xpath("//a[@class = 'btn']");
    private By createDelayedPostBtn = By.xpath("//a[@class = 'btn news-add__planning btn--white btn--bordered']");

    private By deletePostBtn = By.xpath("(//button[contains(@class, 'edit__icon')])[2]");
    private By editPostBtn = By.xpath("(//button[contains(@class, 'edit__icon')])[1]");

    private By selectDateBtn = By.xpath("(//select[contains(@class, 'day')])");
    private By selectMonthBtn = By.xpath("(//select[contains(@class, 'month')])");
    private By selectYearBtn = By.xpath("(//select[contains(@class, 'year')])");
    private By selectTimeBtn = By.xpath("(//select[contains(@class, 'time')])");

    private By postTitleField = By.xpath("//h3[@class = 'news-block__content-title']");

    private final String postTitle = "Новый тайтл поста";
    private final String postBody = "Тело поста, какой-то текст";

    @Test
    public void createPost() throws InterruptedException {
        //act
        generateDefaultPost(postTitle, postBody);

        driver.navigate().refresh();
        Thread.sleep(2000);

        //assert
        boolean isCorrect = false;
        for (WebElement post : driver.findElements(findPostsBlock)) {
            if (post.findElement(postTitleField).getText().equals(postTitle)) {
                isCorrect = true;
                break;
            }
        }

        Assert.assertTrue(isCorrect);
    }

    @Test
    public void createDelayedPost() throws InterruptedException {
        Date dateDelay = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateDelay);
        calendar.add(Calendar.DATE, 1);


        //act
        generateDelayedPost("Delayed Post", postBody, calendar);

        driver.navigate().refresh();
        Thread.sleep(2000);

        //assert
        boolean isCorrect = false;
        for (WebElement post : driver.findElements(findPostsBlock)) {
            if (post.findElement(postTitleField).getText().equals("Delayed Post")) {
                isCorrect = true;
                break;
            }
        }

        Assert.assertTrue(isCorrect);
    }

    @Test
    public void editPost() throws InterruptedException {

        //arrange
        String addToTitle = "Updated";

        generateDefaultPost("PostForUpdate", postBody);

        driver.navigate().refresh();
        Thread.sleep(2000);

        //act
        for (WebElement post : driver.findElements(findPostsBlock)) {
            if (post.findElement(postTitleField).getText().equals("PostForUpdate")) {
                post.findElement(editPostBtn).click();
                driver.findElement(inputPostTitleField).sendKeys(addToTitle);
                driver.findElement(createPostBtn).click();
                break;
            }
        }

        driver.navigate().refresh();
        Thread.sleep(2000);

        //assert
        boolean isCorrect = false;
        for (WebElement post : driver.findElements(findPostsBlock)) {
            if (post.findElement(postTitleField).getText().equals("PostForUpdateUpdated")) {
                isCorrect = true;
                break;
            }
        }

        Assert.assertTrue(isCorrect);
    }

    @Test
    public void deletePost() throws InterruptedException {

        //arrange
        generateDefaultPost("PostForDelete", postBody);

        //act
        for (WebElement post : driver.findElements(findPostsBlock)) {
            if (post.findElement(postTitleField).getText().equals("PostForDelete")) {
                post.findElement(deletePostBtn).click();
                break;
            }
        }

        //assert
        driver.navigate().refresh();
        Thread.sleep(2000);

        boolean isDeleted = true;

        for (WebElement post : driver.findElements(findPostsBlock)) {
            if (post.findElement(postTitleField).getText().equals("PostForDelete")) {
                isDeleted = false;
                break;
            }
        }

        Assert.assertTrue(isDeleted);
    }

    private boolean isElementPresent(By locatorKey) {
        try {
            driver.findElement(locatorKey);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }


    private void generateDefaultPost(String postTitle, String postBody) {
        wait.until(ExpectedConditions.elementToBeClickable(addPostBtn));
        driver.findElement(addPostBtn).click();
        driver.findElement(inputPostTitleField).sendKeys(postTitle);
        driver.findElement(inputPostBody).sendKeys(postBody);
        driver.findElement(createPostBtn).click();
    }

    private void generateDelayedPost(String postTitle, String postBody, Calendar time) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(addPostBtn));
        driver.findElement(addPostBtn).click();
        driver.findElement(inputPostTitleField).sendKeys(postTitle);
        driver.findElement(inputPostBody).sendKeys(postBody);
        driver.findElement(createDelayedPostBtn).click();
        driver.findElement(selectDateBtn).click();

        System.out.println(time.get(Calendar.DATE)
                + " " + time.get(Calendar.MONTH)
                + " " + time.get(Calendar.YEAR));
        Thread.sleep(3000);
    }

}
