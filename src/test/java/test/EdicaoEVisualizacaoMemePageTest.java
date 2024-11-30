package test;

import faker.MemeFaker;
import io.github.bonigarcia.wdm.WebDriverManager;
import model.Meme;
import model.TipoMeme;
import org.junit.jupiter.api.*;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.CadastroMemePage;
import page.EdicaoEVisualizacaoMemePage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class EdicaoEVisualizacaoMemePageTest {
    private WebDriver driver;
    private WebDriverWait webDriverWait;

    private final String PAGE_URL = "https://webmemes.devhub.dev.br/visualizar.html";
    private EdicaoEVisualizacaoMemePage updateAndViewMeme;

    @BeforeEach
    void setup() {
        WebDriverManager.firefoxdriver().setup();

        driver = new ChromeDriver();
        driver.get(PAGE_URL);

        updateAndViewMeme = new EdicaoEVisualizacaoMemePage(driver);
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterEach
    void destroy() {
        driver.quit();
    }

    //16
    @Test
    @DisplayName("Should change page when click return to registration")
    void shoundChangePageWhenClickReturnToRegitration(){
        updateAndViewMeme.goToRegistrationPage();

        assertEquals("https://webmemes.devhub.dev.br/index.html", driver.getCurrentUrl());
    }

    //17
    @Test
    @DisplayName("Should change page when click proximo")
    void shouldChangePageWhenClickProximo(){
        String firstPage = updateAndViewMeme.identifyNumberOfPage();
        updateAndViewMeme.goToNextPage();
        String secondPage = updateAndViewMeme.identifyNumberOfPage();

        boolean res = !firstPage.equals(secondPage);

        assertTrue(res);
    }


}
