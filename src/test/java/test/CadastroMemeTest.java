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
import org.assertj.core.api.SoftAssertions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

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

    @Nested
    @DisplayName("Register meme page tests")
    class RegisterMemes {

        @Nested
        @DisplayName("Meme images tests")
        class RegisterImagesMemes {
            @Test
            @DisplayName("shouldSuccessfullyRegisterMeme")
            void shouldSuccessfullyRegisterMemeImage() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl("https://quatrorodas.abril.com.br/wp-content/uploads/2022/09/Charge-67-Mustang-electric-06.webp?crop=1&resize=1212,909");
                    meme.setTitulo(MemeFaker.getValidTitle());
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageSuccessful());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }

            @Test
            @DisplayName("Should not register meme image without a valid URL")
            void shouldNotRegisterMemeImageWithoutValidUrl() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl("https://www.google.com");
                    meme.setTitulo(MemeFaker.getValidTitle());
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageErrorUrlType());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }

            @Test
            @DisplayName("Should not register meme image with a empty URL")
            void shouldNotRegisterMemeImageWithEmptydUrl() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl("");
                    meme.setTitulo(MemeFaker.getValidTitle());
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageErrorUrl());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }

            @Test
            @DisplayName("Should not register meme image with a video URL")
            void shouldNotRegisterMemeImageWithVideoUrl() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl("https://www.youtube.com/watch?v=PFooIMCTXG4&t=5s&ab_channel=CoisadeNerd");
                    meme.setTitulo(MemeFaker.getValidTitle());
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageErrorUrlType());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }
        }

        @Nested
        @DisplayName("Meme videos tests")
        class RegisterVideosMemes {
            @Test
            @DisplayName("Should not register meme video with a image URL")
            void shouldNotRegisterMemeVideoWithImageUrl() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.VIDEO);
                    meme.setUrl("https://quatrorodas.abril.com.br/wp-content/uploads/2022/09/Charge-67-Mustang-electric-06.webp?crop=1&resize=1212,909");
                    meme.setTitulo(MemeFaker.getValidTitle());
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageErrorUrlType());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }

            @Test
            @DisplayName("Should not register meme video with a empty URL")
            void shouldNotRegisterMemeVideoWithEmptydUrl() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.VIDEO);
                    meme.setUrl("");
                    meme.setTitulo(MemeFaker.getValidTitle());
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageErrorUrl());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }

            @Test
            @DisplayName("shouldSuccessfullyRegisterMeme")
            void shouldSuccessfullyRegisterMemeVideo() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.VIDEO);
                    meme.setUrl("https://www.youtube.com/watch?v=PFooIMCTXG4&t=5s&ab_channel=CoisadeNerd");
                    meme.setTitulo(MemeFaker.getValidTitle());
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageSuccessful());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }
        }

        @Nested
        @DisplayName("General register meme tests")
        class GeneralRegisterMemesTests {

            @Test
            @DisplayName("Should report all erros when URL and title are empty.")
            void shouldReportMorethanOneError() {
                try {
                    SoftAssertions softly = new SoftAssertions();

                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.VIDEO);
                    meme.setUrl("");
                    meme.setTitulo("");
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    softly.assertThat(createMeme.checkMessageErrorUrl()).isEqualTo(true);
                    softly.assertThat(createMeme.checkMessageTitleError()).isEqualTo(true);

                    softly.assertAll();

                } catch (TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }

            @Test
            @DisplayName("Should not register meme image with a empty title")
            void shouldNotRegisterMemeImageWithEmptyTitle() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl("https://quatrorodas.abril.com.br/wp-content/uploads/2022/09/Charge-67-Mustang-electric-06.webp?crop=1&resize=1212,909");
                    meme.setTitulo("");
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageTitleError());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }

            @Test
            @DisplayName("should not register meme with title shorter than three characters.")
            void shouldNotRegisterMemeWithTitleShorterThanThreeCharacters() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl("https://quatrorodas.abril.com.br/wp-content/uploads/2022/09/Charge-67-Mustang-electric-06.webp?crop=1&resize=1212,909");
                    meme.setTitulo(MemeFaker.getShortTitle());
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageTitleError());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }

            @Test
            @DisplayName("should not register meme with title longer than fifty characters.")
            void shouldRegisterMemeWithTitleLongerThanFiftyCharacters() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl("https://quatrorodas.abril.com.br/wp-content/uploads/2022/09/Charge-67-Mustang-electric-06.webp?crop=1&resize=1212,909");
                    meme.setTitulo(MemeFaker.getLongTitle());
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageSuccessful());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }

            @Test
            @DisplayName("should not register meme with description longer than fifty characters.")
            void shouldNotRegisterMemeWithDescriptionLongerThanFiftyCharacters() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl("https://quatrorodas.abril.com.br/wp-content/uploads/2022/09/Charge-67-Mustang-electric-06.webp?crop=1&resize=1212,909");
                    meme.setTitulo(MemeFaker.getValidTitle());
                    meme.setDescricao(MemeFaker.getLongDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageDescriptionError());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }

            @Test
            @DisplayName("should not register meme with title containing only space .")
            void shouldNotRegisterMemeWithTitleContainingOnlySpaces() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl("https://quatrorodas.abril.com.br/wp-content/uploads/2022/09/Charge-67-Mustang-electric-06.webp?crop=1&resize=1212,909");
                    meme.setTitulo("      ");
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageTitleError());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }

            @Test
            @DisplayName("Should report all erros when title are empty and description exceed 50 characteres.")
            void shouldReportMoreThanOneErrorWhenTitleIsBlankAndDrescriptionExceedFiftyCharacteres() {
                try {
                    SoftAssertions softly = new SoftAssertions();

                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.VIDEO);
                    meme.setUrl(MemeFaker.getUrlVideos());
                    meme.setTitulo("");
                    meme.setDescricao(MemeFaker.getLongDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    softly.assertThat(createMeme.checkMessageTitleError()).isEqualTo(true);
                    softly.assertThat(createMeme.checkMessageDescriptionError()).isEqualTo(true);

                    softly.assertAll();

                } catch (TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }

        }

        @Nested
        @DisplayName("UI register meme tests")
        class UiRegisterMemeTests {

            @Test
            @DisplayName("Should change page when click view registered memes")
            void shouldChangePageWhenClickViewRegisteredMemes() {
                createMeme.goToMemesCadastradosPage();

                assertEquals("https://webmemes.devhub.dev.br/visualizar.html", driver.getCurrentUrl());
            }
        }
    }
}
