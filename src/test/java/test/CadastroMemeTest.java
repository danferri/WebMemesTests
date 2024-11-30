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
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
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
            //1
            @Test
            @DisplayName("shouldSuccessfullyRegisterMeme")
            void shouldSuccessfullyRegisterMemeImage() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl(MemeFaker.getUrlImage());
                    meme.setTitulo(MemeFaker.getValidTitle());
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageSuccessful());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }
            //2
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
            //3
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
            //4
            @Test
            @DisplayName("Should not register meme image with a video URL")
            void shouldNotRegisterMemeImageWithVideoUrl() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl(MemeFaker.getUrlVideos());
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
            //5
            @Test
            @DisplayName("Should not register meme video with a image URL")
            void shouldNotRegisterMemeVideoWithImageUrl() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.VIDEO);
                    meme.setUrl(MemeFaker.getUrlImage());
                    meme.setTitulo(MemeFaker.getValidTitle());
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageErrorUrlType());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }
            //6
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
            //7
            @Test
            @DisplayName("shouldSuccessfullyRegisterMeme")
            void shouldSuccessfullyRegisterMemeVideo() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.VIDEO);
                    meme.setUrl(MemeFaker.getUrlVideos());
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
            //8
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
            //9
            @Test
            @DisplayName("Should not register meme image with a empty title")
            void shouldNotRegisterMemeImageWithEmptyTitle() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl(MemeFaker.getUrlImage());
                    meme.setTitulo("");
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageTitleError());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }
            //10
            @Test
            @DisplayName("should not register meme with title shorter than three characters.")
            void shouldNotRegisterMemeWithTitleShorterThanThreeCharacters() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl(MemeFaker.getUrlImage());
                    meme.setTitulo(MemeFaker.getShortTitle());
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageTitleError());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }
            //11
            @Test
            @DisplayName("should not register meme with title longer than fifty characters.")
            void shouldRegisterMemeWithTitleLongerThanFiftyCharacters() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl(MemeFaker.getUrlImage());
                    meme.setTitulo(MemeFaker.getLongTitle());
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageSuccessful());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }
            //12
            @Test
            @DisplayName("should not register meme with description longer than fifty characters.")
            void shouldNotRegisterMemeWithDescriptionLongerThanFiftyCharacters() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl(MemeFaker.getUrlImage());
                    meme.setTitulo(MemeFaker.getValidTitle());
                    meme.setDescricao(MemeFaker.getLongDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageDescriptionError());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }
            //13
            @Test
            @DisplayName("should not register meme with title containing only space .")
            void shouldNotRegisterMemeWithTitleContainingOnlySpaces() {
                try {
                    Meme meme = new Meme();
                    meme.setTipo(TipoMeme.IMAGE);
                    meme.setUrl(MemeFaker.getUrlImage());
                    meme.setTitulo("      ");
                    meme.setDescricao(MemeFaker.getValidDescricao());

                    createMeme.cadastroMemeFromMeme(meme);

                    assertTrue(createMeme.checkMessageTitleError());

                } catch(TimeoutException ignored) {
                    Assertions.fail("Meme wasn't register");
                }
            }
            //14
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
            //15
            @Test
            @DisplayName("Should change page when click view registered memes")
            void shouldChangePageWhenClickViewRegisteredMemes() {
                createMeme.goToMemesCadastradosPage();

                assertEquals("https://webmemes.devhub.dev.br/visualizar.html", driver.getCurrentUrl());
            }

        }
    }
}
