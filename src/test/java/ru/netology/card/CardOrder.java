package ru.netology.card;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardOrder {

    private static WebDriver driver;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        driver.get("http://localhost:9999/");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSuccessfulSendValidForm(){
        driver.findElement(By.cssSelector("[data-test-id= 'name'] input")).sendKeys("Семен Лобанов");
        driver.findElement(By.cssSelector("[data-test-id= 'phone'] input")).sendKeys("+79345174204");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.cssSelector("[type=button]")).click();
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id= 'order-success']")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void shouldSuccessfulSendValidFormWithSmallName(){
        driver.findElement(By.cssSelector("[data-test-id= 'name'] input")).sendKeys("Ян Гу");
        driver.findElement(By.cssSelector("[data-test-id= 'phone'] input")).sendKeys("+78546874251");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.cssSelector("[type=button]")).click();
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id= 'order-success']")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void shouldSuccessfulSendValidFormWithForeignNumber(){
        driver.findElement(By.cssSelector("[type=text]")).sendKeys("Ехан Мюллер");
        driver.findElement(By.cssSelector("[type=tel]")).sendKeys("+49126958458");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.cssSelector("[type=button]")).click();
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id= 'order-success']")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void shouldSuccessfulSendValidFormWithHyphenName(){
        driver.findElement(By.cssSelector("[type=text]")).sendKeys("Семен-Черкас Лобанов");
        driver.findElement(By.cssSelector("[type=tel]")).sendKeys("+79345174277");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.cssSelector("[type=button]")).click();
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id= 'order-success']")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    // Негативные сценарии

    @Test
    public void shouldGetErrorNoName(){
        driver.findElement(By.cssSelector("[type=text]")).sendKeys("");
        driver.findElement(By.cssSelector("[type=tel]")).sendKeys("+79345179977");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.cssSelector("[type=button]")).click();
        String expectedText = "Поле обязательно для заполнения";
        String actualText = driver.findElement(By.cssSelector
                        ("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void shouldGetErrorWithEnglishName(){
        driver.findElement(By.cssSelector("[type=text]")).sendKeys("Harry Potter");
        driver.findElement(By.cssSelector("[type=tel]")).sendKeys("+79345119977");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.cssSelector("[type=button]")).click();
        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualText = driver.findElement(By.cssSelector
                ("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void shouldGetErrorNoNumber(){
        driver.findElement(By.cssSelector("[type=text]")).sendKeys("Семен Лобанов");
        driver.findElement(By.cssSelector("[type=tel]")).sendKeys("");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.cssSelector("[type=button]")).click();
        String expectedText = "Поле обязательно для заполнения";
        String actualText = driver.findElement(By.cssSelector
                ("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void shouldGetErrorWithLongNumber(){
        driver.findElement(By.cssSelector("[type=text]")).sendKeys("Семен Лобанов");
        driver.findElement(By.cssSelector("[type=tel]")).sendKeys("+795487412565");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.cssSelector("[type=button]")).click();
        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = driver.findElement(By.cssSelector
                ("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void shouldGetErrorWithoutAgreement(){
        driver.findElement(By.cssSelector("[type=text]")).sendKeys("Семен Лобанов");
        driver.findElement(By.cssSelector("[type=tel]")).sendKeys("+79548741256");
        driver.findElement(By.cssSelector("[type=button]")).click();
        String expectedText = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actualText = driver.findElement(By.cssSelector
                ("[data-test-id='agreement'].input_invalid .checkbox__text")).getText().trim();
        assertEquals(expectedText, actualText);
    }

}
