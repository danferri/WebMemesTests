package test;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
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

    //issue: o botao "proximo" mesmo sem poder clicar ele nao fica desativo
    @Test
    @DisplayName("Should not change when 'Proximo' button is disabled")
    void shouldNotChangeWhenProximoButtonIsDisabled() {

        // Tentando clicar no botão "Proximo" mesmo estando desativado
        if (!updateAndViewMeme.getProximo()) {
            // O clique não deve ter efeito se o botão estiver desativado
            String firstPage = updateAndViewMeme.identifyNumberOfPage();
            updateAndViewMeme.goToNextPage();
            String secondPage = updateAndViewMeme.identifyNumberOfPage();

            // Verifica se a página não mudou (pois o botão estava desativado)
            boolean isPageSame = firstPage.equals(secondPage);
            assertTrue(isPageSame, "A página não deveria ter mudado, pois o botão estava desativado");
        } else {
            fail("O botão 'Próximo' deveria estar desativado.");
        }
    }

    //18
    @Test
    @DisplayName("Should report a error when change page when click anterior and first page is 1")
    void shouldReportAErrorWhenChangePageWhenClickAnteriorAndFirstPageIs1(){
        boolean isOnFirstPage = updateAndViewMeme.identifyNumberOfPage().equals("Página 1");
        if(isOnFirstPage){
            try{
                updateAndViewMeme.goToPreviousPage();

                fail("Esperava-se uma exceção ao tentar voltar da pagina 1");
            }catch (Exception e) {
                assertEquals("Não é possível voltar da página 1", e.getMessage());
            }
        }



    }


}
