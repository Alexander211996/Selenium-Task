package card.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {

    WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
       void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    public void shouldSendForm () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text'].input__control")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[type = 'tel'].input__control")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id = 'order-success']")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldSendFormWithDoubleName () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text'].input__control")).sendKeys("Анна-Мария Петрова");
        driver.findElement(By.cssSelector("[type = 'tel'].input__control")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id = 'order-success']")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithoutCheckbox () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text'].input__control")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[type = 'tel'].input__control")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actualText = driver.findElement(By.className("input_invalid")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithEnglish () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text'].input__control")).sendKeys("Ivan");
        driver.findElement(By.cssSelector("[type = 'tel'].input__control")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithNumbers () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text'].input__control")).sendKeys("123");
        driver.findElement(By.cssSelector("[type = 'tel'].input__control")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithSymbols () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text'].input__control")).sendKeys("@#$");
        driver.findElement(By.cssSelector("[type = 'tel'].input__control")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("div:nth-child(3) > label")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithLessThan11Tel () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text'].input__control")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[type = 'tel'].input__control")).sendKeys("+7999111223");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithMoreThan11Tel () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text'].input__control")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[type = 'tel'].input__control")).sendKeys("+799911122334");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithoutPlus () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text'].input__control")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[type = 'tel'].input__control")).sendKeys("79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithoutName () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'tel'].input__control")).sendKeys("79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Поле обязательно для заполнения";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithoutPhone () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text'].input__control")).sendKeys("Иван Петров");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Поле обязательно для заполнения";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

}

