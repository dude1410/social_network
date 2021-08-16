package autotests;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Profile;

@Profile("test")
public class TestSuiteComments {

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

    private By deleteBtn = By.xpath("(//button[contains(@class, 'edit__icon')])[2]");
    private By editBtn = By.xpath("(//button[contains(@class, 'edit__icon')])[1]");

    private By commentInputField = By.xpath("//input[@class = 'comment-add__input']");

    private By postTitleField = By.xpath("//h3[@class = 'news-block__content-title']");

    private final String postTitle = "Новый тайтл поста";
    private final String postBody = "Тело поста, какой-то текст";


    @Test
    public void createComment() throws InterruptedException {
        generateDefaultPost(postTitle, postBody);

        driver.navigate().refresh();
        Thread.sleep(2000);

        for (WebElement post : driver.findElements(findPostsBlock)) {
            //Получим созданный пост
            if (post.findElement(postTitleField).getText().equals(postTitle)) {
                //Создадим коммент на посте
                post.findElement(commentInputField).sendKeys("Текст комментария");
                post.findElement(commentInputField).sendKeys(Keys.ENTER);
                break;
            }
        }

        //assert
        boolean isError = false;
        for (WebElement post : driver.findElements(findPostsBlock)) {
            if (post.findElement(postTitleField).getText().equals(postTitle)) {

                isError = true;
                break;
            }
        }

        Assert.assertFalse(isError);

    }

    @Test
    public void editComment()
    {
        generateDefaultPost(postTitle, postBody);


        for (WebElement post : driver.findElements(findPostsBlock)) {
            //Получим созданный пост
            if (post.findElement(postTitleField).getText().equals(postTitle)) {
                //Создадим коммент на посте
                post.findElement(commentInputField).sendKeys("Текст комментария");
                post.findElement(commentInputField).sendKeys(Keys.ENTER);



                break;
            }
        }

    }

    @Test
    public void deleteComment()
    {
        generateDefaultPost(postTitle, postBody);

        for (WebElement post : driver.findElements(findPostsBlock)) {
            //Получим созданный пост
            if (post.findElement(postTitleField).getText().equals(postTitle)) {
                //Создадим коммент на посте
                post.findElement(commentInputField).sendKeys("Текст комментария");
                post.findElement(commentInputField).sendKeys(Keys.ENTER);

                break;
            }
        }

    }


    private void generateDefaultPost(String postTitle, String postBody) {
        wait.until(ExpectedConditions.elementToBeClickable(addPostBtn));
        driver.findElement(addPostBtn).click();
        driver.findElement(inputPostTitleField).sendKeys(postTitle);
        driver.findElement(inputPostBody).sendKeys(postBody);
        driver.findElement(createPostBtn).click();
    }


}
