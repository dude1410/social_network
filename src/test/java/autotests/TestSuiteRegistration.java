package autotests;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestSuiteRegistration {
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

    private By registrationButton = By.cssSelector(".form-layout__btn");
    private By accountTitle = By.cssSelector(".form__subtitle");
    private By fieldEmail = By.id("register-email");
    private By fieldPassword = By.id("register-password");
    private By fieldConfirmPassword = By.id("register-repeat-password");
    private By fieldName = By.id("register-firstName");
    private By fieldSurname  = By.id("register-lastName");
    private By code = By.cssSelector(".form__code");
    private By fieldCode = By.id("register-number");
    private By checkboxIAgree = By.cssSelector(".form__checkbox-label");
    private By backToRegistrationButton = By.cssSelector(".modal_button");
    private By registerButton = By.cssSelector(".btn--white");

    @Test
    public void registrationWithCorrectData()
    {
        //arrange
        var email = "zerone1015@mail.ru";
        var password = "Zerone1000";
        var name = "Андрей";
        var surname = "Михайлов";
        var expectedResultTitle = "Подтвердите регистрацию";
        var expectedResultText = "Ваш аккаунт успешно создан. На указанную почту выслано письмо со ссылкой подтверждения.";

        //act
        driver.findElement(registrationButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountTitle));
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(fieldPassword).sendKeys(password);
        driver.findElement(fieldConfirmPassword).sendKeys(password);
        driver.findElement(fieldName).sendKeys(name);
        driver.findElement(fieldSurname).sendKeys(surname);
        String numbersOfCode = driver.findElement(code).getText();
        driver.findElement(fieldCode).click();
        driver.findElement(fieldCode).sendKeys(numbersOfCode);
        driver.findElement(checkboxIAgree).click();
        driver.findElement(backToRegistrationButton).click();
        driver.findElement(registerButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".v-snack__content")));

        //assert
        var actualResultTitle = driver.findElement(By.cssSelector(".form-layout__title")).getText();
        Assert.assertEquals("Пользователь незарегистрирован", expectedResultTitle, actualResultTitle);
        var actualResultText = driver.findElement(By.cssSelector(".form-layout__text")).getText();
        Assert.assertEquals("Аккаунт не создан", expectedResultText, actualResultText);
    }

    @Test
    public void registrationWithInvalidEmail()
    {
        //arrange
        var invalidEmail = "zerone1010@mail";
        var password = "Zerone1000";
        var name = "Андрей";
        var surname = "Михайлов";
        var expectedResult = "Введите корректный E-mail";

        //act
        driver.findElement(registrationButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountTitle));
        driver.findElement(fieldEmail).sendKeys(invalidEmail);
        driver.findElement(fieldPassword).sendKeys(password);
        driver.findElement(fieldConfirmPassword).sendKeys(password);
        driver.findElement(fieldName).sendKeys(name);
        driver.findElement(fieldSurname).sendKeys(surname);
        String numbersOfCode = driver.findElement(code).getText();
        driver.findElement(fieldCode).click();
        driver.findElement(fieldCode).sendKeys(numbersOfCode);
        driver.findElement(checkboxIAgree).click();
        driver.findElement(backToRegistrationButton).click();
        driver.findElement(registerButton).click();

        //assert
        var actualResult = driver.findElement(By.cssSelector(".form__error")).getText();
        Assert.assertEquals("Пользователь зарегистрирован", expectedResult, actualResult);
    }

    @Test
    public void RegistrationWithRegisteredEmail() throws InterruptedException {
        //arrange
        var email = "zerone1000@mail.ru";
        var password = "Zerone1000";
        var name = "Андрей";
        var surname = "Михайлов";
        var expectedResultTitle = "Не удалось зарегистрироваться";
        var expectedResultText = "Такой пользователь уже есть. Попробуйте авторизоваться.";

        //act
        driver.findElement(registrationButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountTitle));
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(fieldPassword).sendKeys(password);
        driver.findElement(fieldConfirmPassword).sendKeys(password);
        driver.findElement(fieldName).sendKeys(name);
        driver.findElement(fieldSurname).sendKeys(surname);
        String numbersOfCode = driver.findElement(code).getText();
        driver.findElement(fieldCode).click();
        driver.findElement(fieldCode).sendKeys(numbersOfCode);
        driver.findElement(checkboxIAgree).click();
        driver.findElement(backToRegistrationButton).click();
        driver.findElement(registerButton).click();
        Thread.sleep(5000);

        //assert
        var actualResultTitle = driver.findElement(By.cssSelector(".form-layout__title")).getText();
        Assert.assertEquals("Пользователь зарегистрирован", expectedResultTitle, actualResultTitle);
        var actualResultText = driver.findElement(By.cssSelector(".form-layout__text")).getText();
        Assert.assertEquals("Аккаунт создан", expectedResultText, actualResultText);
    }

    @Test
    public void registrationWithDifferentPasswords()
    {
        //arrange
        var email = "zerone1000@mail.ru";
        var password = "Zerone1000";
        var invalidPassword = "Zerone2000";
        var name = "Андрей";
        var surname = "Михайлов";
        var expectedResult = "Пароли должны совпадать";

        //act
        driver.findElement(registrationButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountTitle));
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(fieldPassword).sendKeys(password);
        driver.findElement(fieldConfirmPassword).sendKeys(invalidPassword);
        driver.findElement(fieldName).sendKeys(name);
        driver.findElement(fieldSurname).sendKeys(surname);
        String numbersOfCode = driver.findElement(code).getText();
        driver.findElement(fieldCode).click();
        driver.findElement(fieldCode).sendKeys(numbersOfCode);
        driver.findElement(checkboxIAgree).click();
        driver.findElement(backToRegistrationButton).click();
        driver.findElement(registerButton).click();

        //assert
        var actualResult = driver.findElement(By.xpath("(//*[@class='form__error'])[2]")).getText();
        Assert.assertEquals("Пользователь зарегистрирован", expectedResult, actualResult);
    }

    @Test
    public void registrationWithoutRepeatingPassword()
    {
        //arrange
        var email = "zerone1000@mail.ru";
        var password = "Zerone1000";
        var name = "Андрей";
        var surname = "Михайлов";
        var expectedResult = "Введите пароль";

        //act
        driver.findElement(registrationButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountTitle));
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(fieldPassword).sendKeys(password);
        driver.findElement(fieldName).sendKeys(name);
        driver.findElement(fieldSurname).sendKeys(surname);
        String numbersOfCode = driver.findElement(code).getText();
        driver.findElement(fieldCode).click();
        driver.findElement(fieldCode).sendKeys(numbersOfCode);
        driver.findElement(checkboxIAgree).click();
        driver.findElement(backToRegistrationButton).click();
        driver.findElement(registerButton).click();

        //assert
        var actualResult = driver.findElement(By.xpath("(//*[@class='form__error'])[2]")).getText();
        Assert.assertEquals("Пользователь зарегистрирован", expectedResult, actualResult);
    }

    @Test
    public void registrationWithInvalidPassword()
    {
        //arrange
        var email = "zerone1000@mail.ru";
        var invalidPassword = "erone1000";
        var name = "Андрей";
        var surname = "Михайлов";
        var expectedResult = "Пароль должен состоять из латинских букв, цифр и знаков. Обязательно содержать одну заглавную букву, одну цифру и состоять из 8 символов.";

        //act
        driver.findElement(registrationButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountTitle));
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(fieldPassword).sendKeys(invalidPassword);
        driver.findElement(fieldName).sendKeys(name);
        driver.findElement(fieldSurname).sendKeys(surname);
        String numbersOfCode = driver.findElement(code).getText();
        driver.findElement(fieldCode).click();
        driver.findElement(fieldCode).sendKeys(numbersOfCode);
        driver.findElement(checkboxIAgree).click();
        driver.findElement(backToRegistrationButton).click();
        driver.findElement(registerButton).click();

        //assert
        var actualResult = driver.findElement(By.cssSelector(".form__password-info")).getText();
        Assert.assertEquals("Пользователь зарегистрирован", expectedResult, actualResult);
    }

    @Test
    public void registrationWithoutEnteringACode()
    {
        //arrange
        var email = "zerone1000@mail.ru";
        var password = "Zerone1000";
        var name = "Андрей";
        var surname = "Михайлов";
        var expectedResult = "Обязательно поле";

        //act
        driver.findElement(registrationButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountTitle));
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(fieldPassword).sendKeys(password);
        driver.findElement(fieldConfirmPassword).sendKeys(password);
        driver.findElement(fieldName).sendKeys(name);
        driver.findElement(fieldSurname).sendKeys(surname);
        driver.findElement(checkboxIAgree).click();
        driver.findElement(backToRegistrationButton).click();
        driver.findElement(registerButton).click();

        //assert
        var actualResult = driver.findElement(By.xpath("(//*[@class='form__error'])[2]")).getText();
        Assert.assertEquals("Пользователь зарегистрирован", expectedResult, actualResult);
    }

    @Test
    public void registrationWithAnIncorrectCode()
    {
        //arrange
        var email = "zerone1000@mail.ru";
        var password = "Zerone1000";
        var name = "Андрей";
        var surname = "Михайлов";
        var codeNumbers = "0000";
        var expectedResult = "Цифры не совпадают";

        //act
        driver.findElement(registrationButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountTitle));
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(fieldPassword).sendKeys(password);
        driver.findElement(fieldConfirmPassword).sendKeys(password);
        driver.findElement(fieldName).sendKeys(name);
        driver.findElement(fieldSurname).sendKeys(surname);
        driver.findElement(fieldCode).sendKeys(codeNumbers);
        driver.findElement(checkboxIAgree).click();
        driver.findElement(backToRegistrationButton).click();
        driver.findElement(registerButton).click();

        //assert
        var actualResult = driver.findElement(By.xpath("(//*[@class='form__error'])[2]")).getText();
        Assert.assertEquals("Пользователь зарегистрирован", expectedResult, actualResult);
    }

    @Test
    public void registrationWithoutCheckbox()
    {
        //arrange
        var email = "zerone1000@mail.ru";
        var password = "Zerone1000";
        var name = "Андрей";
        var surname = "Михайлов";
        var expectedResult = "[[ChromeDriver: chrome on WINDOWS (b6306992ffcf9b7a4addea27bb838fd2)] -> css selector: .invalid]";

        //act
        driver.findElement(registrationButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountTitle));
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(fieldPassword).sendKeys(password);
        driver.findElement(fieldConfirmPassword).sendKeys(password);
        driver.findElement(fieldName).sendKeys(name);
        driver.findElement(fieldSurname).sendKeys(surname);
        String numbersOfCode = driver.findElement(code).getText();
        driver.findElement(fieldCode).click();
        driver.findElement(fieldCode).sendKeys(numbersOfCode);
        driver.findElement(registerButton).click();

        //assert
        driver.findElement(By.cssSelector(".invalid"));
    }
}

