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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {

    private WebDriver driver;

    @BeforeAll
    public static void setUpAll () {
        System.setProperty ("webdriver.chrome.driver", "./driver/win/chromedriver.exe");
    }

    @BeforeEach
    public void setUp () {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown () {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSendForm () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text']")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[type = 'tel']")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id = 'order-success']")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldSendFormWithDoubleName () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text']")).sendKeys("Анна-Мария Петрова");
        driver.findElement(By.cssSelector("[type = 'tel']")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id = 'order-success']")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithoutCheckbox () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text']")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[type = 'tel']")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actualText = driver.findElement(By.className("input_invalid")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithEnglish () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text']")).sendKeys("Ivan");
        driver.findElement(By.cssSelector("[type = 'tel']")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualText = driver.findElement(By.cssSelector("span.input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithNumbers () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text']")).sendKeys("123");
        driver.findElement(By.cssSelector("[type = 'tel']")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualText = driver.findElement(By.cssSelector("span.input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithSymbols () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text']")).sendKeys("@#$");
        driver.findElement(By.cssSelector("[type = 'tel']")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("div:nth-child(3) > label")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualText = driver.findElement(By.cssSelector("span.input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithLessThan11Tel () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text']")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[type = 'tel']")).sendKeys("+7999111223");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = driver.findElement(By.cssSelector("div:nth-child(2) > span > span > span.input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithMoreThan11Tel () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text']")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[type = 'tel']")).sendKeys("+799911122334");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = driver.findElement(By.cssSelector("div:nth-child(2) > span > span > span.input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

    @Test
    public void shouldNotSendFormWithoutPlus () {
        driver.get ("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type = 'text']")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[type = 'tel']")).sendKeys("79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = driver.findElement(By.cssSelector("div:nth-child(2) > span > span > span.input__sub")).getText().trim();
        assertEquals (expectedText, actualText);
    }

}

