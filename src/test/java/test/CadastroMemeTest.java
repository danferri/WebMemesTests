package test;

import io.github.bonigarcia.wdm.WebDriverManager;
import model.Meme;
import model.TipoMeme;
import org.junit.jupiter.api.*;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.CadastroMemePage;


import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CadastroMemeTest {
    private WebDriver driver;
    private WebDriverWait webDriverWait;

    private final String PAGE_URL = "https://webmemes.devhub.dev.br/index.html";
    private CadastroMemePage createMeme;

    @BeforeEach
    void setup() {
        WebDriverManager.firefoxdriver().setup();

        driver = new ChromeDriver();
        driver.get(PAGE_URL);

        createMeme = new CadastroMemePage(driver);
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void destroy() {
        driver.quit();
    }

    @Test
    @DisplayName("shouldSuccessfullyRegisterMeme")
    void shouldSuccessfullyRegisterMeme() {
        try {
            Meme meme = new Meme();
            meme.setTipo(TipoMeme.IMAGE);
            meme.setUrl("https://quatrorodas.abril.com.br/wp-content/uploads/2022/09/Charge-67-Mustang-electric-06.webp?crop=1&resize=1212,909");
            meme.setTitulo("Mustang");
            meme.setDescricao("um carro");

            createMeme.cadastroMemeFromMeme(meme);

            assertTrue(createMeme.checkMessageSuccessful());

        } catch(TimeoutException ignored) {
            Assertions.fail("Meme wasn't register");
        }
    }
}
