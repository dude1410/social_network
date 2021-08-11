package autotests;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestSuiteInteractingWithPossibleFriends {
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
    private By friendsLink = By.xpath("(//*[@class='main-layout__link'])[2]");
    private By maybeYouKnowThemTitle = By.cssSelector(".friends-possible__title");
    private By addFriendFirstLink = By.xpath("(//*[@class='friends-possible__link'])[1]");
    private By addFriendSecondLink = By.xpath("(//*[@class='friends-possible__link'])[2]");
    private By searchButton = By.cssSelector(".friends-possible__btn");
    private By possibleFriendsTitle = By.cssSelector(".friends__title");
    private By nameField = By.id("friends-search-name");
    private By surnameField = By.id("friends-search-lastname");
    private By userCard = By.cssSelector(".friends-block__info");
    private By popUpMessage = By.cssSelector(".v-snack__content");

    @Test
    public void addingAPossibleFriendAsAFriend()
    {
        //arrange
        var email = "zerone201@mail.ru" ;
        var password = "Zerone201";
        var expectedResult = "Заявка отправлена";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(friendsLink));
        driver.findElement(friendsLink).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(maybeYouKnowThemTitle));
        driver.findElement(addFriendSecondLink).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(popUpMessage));

        //assert
        var actualResult = driver.findElement(popUpMessage).getText();
        Assert.assertEquals("Нет сообщения об отправленной заявке в друзья", expectedResult, actualResult);
    }

    @Test
    public void readdingAPossibleFriendAsAFriend()
    {
        //arrange
        var email = "zerone201@mail.ru" ;
        var password = "Zerone201";
        var expectedResult = "Запрос на добавление в друзья уже существует";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(friendsLink));
        driver.findElement(friendsLink).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(maybeYouKnowThemTitle));
        driver.findElement(addFriendFirstLink).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(popUpMessage));

        //assert
        var actualResult = driver.findElement(popUpMessage).getText();
        Assert.assertEquals("Нет сообщения об отправленной заявке в друзья", expectedResult, actualResult);
    }

    @Test
    public void searchInPossibleFriendsByName() throws InterruptedException {
        //arrange
        var email = "zerone201@mail.ru" ;
        var password = "Zerone201";
        var name = "Марк";
        var expectedResult = "Марк Цукерберг";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(friendsLink));
        driver.findElement(friendsLink).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(maybeYouKnowThemTitle));
        driver.findElement(searchButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(possibleFriendsTitle));
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(searchButton).click();
        Thread.sleep(1000);

        //assert
        var actualResult = driver.findElement(userCard).getText();
        Assert.assertEquals("Возможные друзья не найдены", expectedResult, actualResult);
    }

    @Test
    public void searchInPossibleFriendsBySurname() throws InterruptedException {
        //arrange
        var email = "zerone201@mail.ru" ;
        var password = "Zerone201";
        var surname = "Цукерберг";
        var expectedResult = "Марк Цукерберг";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(friendsLink));
        driver.findElement(friendsLink).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(maybeYouKnowThemTitle));
        driver.findElement(searchButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(possibleFriendsTitle));
        driver.findElement(surnameField).sendKeys(surname);
        driver.findElement(searchButton).click();
        Thread.sleep(1000);

        //assert
        var actualResult = driver.findElement(userCard).getText();
        Assert.assertEquals("Возможные друзья не найдены", expectedResult, actualResult);
    }

    @Test
    public void searchInPossibleFriendsByNameAndSurname() throws InterruptedException {
        //arrange
        var email = "zerone201@mail.ru" ;
        var password = "Zerone201";
        var name = "Марк";
        var surname = "Цукерберг";
        var expectedResult = "Марк Цукерберг";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(friendsLink));
        driver.findElement(friendsLink).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(maybeYouKnowThemTitle));
        driver.findElement(searchButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(possibleFriendsTitle));
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(surnameField).sendKeys(surname);
        driver.findElement(searchButton).click();
        Thread.sleep(1000);

        //assert
        var actualResult = driver.findElement(userCard).getText();
        Assert.assertEquals("Возможные друзья не найдены", expectedResult, actualResult);
    }
}
