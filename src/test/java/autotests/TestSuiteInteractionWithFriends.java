package autotests;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestSuiteInteractionWithFriends {
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
    private By searchField = By.cssSelector(".main-layout__search-input");
    private By friendsLink = By.xpath("(//*[@class='main-layout__link'])[2]");
    private By friendCard = By.cssSelector(".friends-block");
    private By addToFriendButton = By.cssSelector(".profile-info__add");
    private By acceptFriendRequestButton = By.cssSelector(".add");
    private By deleteButton = By.xpath("(//*[contains(@class, 'delete')])[3]");
    private By confirmationOfDeletionButton = By.xpath("(//*[contains(@class, 'btn--bordered')])[1]");
    private By blockedLink = By.cssSelector(".profile-info__blocked");
    private By blockedButton = By.xpath("(//*[@class='friends-block__actions-block'])[1]");
    private By popUpMessage = By.cssSelector(".v-snack__content");

    @Test
    public void addToFriends()
    {
        //arrange
        var email = "zerone201@mail.ru" ;
        var password = "Zerone201";
        var userName = "Илон";
        var expectedResult = "Заявка отправлена";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(searchField));
        driver.findElement(searchField).click();
        driver.findElement(searchField).sendKeys(userName);
        driver.findElement(searchField).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.presenceOfElementLocated(friendCard));
        driver.findElement(friendCard).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(addToFriendButton));
        driver.findElement(addToFriendButton).click();

        //assert
        var actualResult = driver.findElement(popUpMessage).getText();
        Assert.assertEquals("Нет сообщения об отправленной заявке в друзья", expectedResult, actualResult);
    }

    @Test
    public void resendingAFriendRequest()
    {
        //arrange
        var email = "zerone201@mail.ru" ;
        var password = "Zerone201";
        var userName = "Илон";
        var expectedResult = "Запрос на добавление в друзья уже существует";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(searchField));
        driver.findElement(searchField).click();
        driver.findElement(searchField).sendKeys(userName);
        driver.findElement(searchField).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.presenceOfElementLocated(friendCard));
        driver.findElement(friendCard).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(addToFriendButton));
        driver.findElement(addToFriendButton).click();

        //assert
        var actualResult = driver.findElement(popUpMessage).getText();
        Assert.assertEquals("Запрос в друзья отправлен повторно", expectedResult, actualResult);
    }

    @Test
    public void acceptFriendRequest()
    {
        //arrange
        var email = "zerone117@mail.ru" ;
        var password = "Zerone117";
        var expectedResult = "Заявка принята";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(friendsLink));
        driver.findElement(friendsLink).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(acceptFriendRequestButton));
        driver.findElement(acceptFriendRequestButton).click();

        //assert
        var actualResult = driver.findElement(popUpMessage).getText();
        Assert.assertEquals("Заявка в друзья не принята", expectedResult, actualResult);
    }

    @Test
    public void addingAFriendOfAUserWhoIsAlreadyAFriend()
    {
        //arrange
        var email = "zerone201@mail.ru" ;
        var password = "Zerone201";
        var userName = "Илон";
        var expectedResult = "Этот пользователь уже в списке ваших друзей";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(searchField));
        driver.findElement(searchField).click();
        driver.findElement(searchField).sendKeys(userName);
        driver.findElement(searchField).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.presenceOfElementLocated(friendCard));
        driver.findElement(friendCard).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(addToFriendButton));
        driver.findElement(addToFriendButton).click();

        //assert
        var actualResult = driver.findElement(popUpMessage).getText();
        Assert.assertEquals("Запрос в друзья отправлен повторно", expectedResult, actualResult);
    }

    @Test
    public void blockAUserWhoIsNotInFriends()
    {
        //arrange
        var email = "zerone201@mail.ru" ;
        var password = "Zerone201";
        var userName = "Марк";
        var expectedResult = "Пользователь заблокирован";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(searchField));
        driver.findElement(searchField).click();
        driver.findElement(searchField).sendKeys(userName);
        driver.findElement(searchField).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.presenceOfElementLocated(friendCard));
        driver.findElement(friendCard).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(blockedLink));
        driver.findElement(blockedLink).click();

        //assert
        var actualResult = driver.findElement(popUpMessage).getText();
        Assert.assertEquals("Пользователь не заблокирован", expectedResult, actualResult);
    }

    @Test
    public void blockAUserWhoIsAFriend()
    {
        //arrange
        var email = "zerone201@mail.ru" ;
        var password = "Zerone201";
        var expectedResult = "Пользователь заблокирован";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(friendsLink));
        driver.findElement(friendsLink).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(friendCard));
        driver.findElement(friendCard).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(blockedButton));
        driver.findElement(blockedButton).click();

        //assert
        var actualResult = driver.findElement(popUpMessage).getText();
        Assert.assertEquals("Пользователь не заблокирован", expectedResult, actualResult);
    }

    @Test
    public void removeFromFriends()
    {
        //arrange
        var email = "zerone117@mail.ru" ;
        var password = "Zerone117";
        var expectedResult = "Пользователь удален из друзей";

        //act
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(friendsLink));
        driver.findElement(friendsLink).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(deleteButton));
        driver.findElement(deleteButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(confirmationOfDeletionButton));
        driver.findElement(confirmationOfDeletionButton).click();

        //assert
        var actualResult = driver.findElement(popUpMessage).getText();
        Assert.assertEquals("Пользователь не удален из друзей", expectedResult, actualResult);
    }
}
