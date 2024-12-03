package test;


import faker.MemeFaker;
import io.github.bonigarcia.wdm.WebDriverManager;
import model.Meme;
import model.TipoMeme;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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

    @Test
    @DisplayName("Table should have the correct html semantics")
    void tableShouldHaveTheCorrectSemantics(){
        boolean isSemanticCorrect = updateAndViewMeme.isTableSemanticCorrect();
        assertTrue(isSemanticCorrect);
    }

    @Test
    @DisplayName("Text should have the correct html semantics")
    void TextShouldHaveTheCorrectHtmlSemantics(){
        boolean isSemanticCorrect = updateAndViewMeme.isTextSemanticCorrect();
        assertTrue(isSemanticCorrect);
    }

    @Nested
    @DisplayName("Edit meme test")
    class EditMemeTest {

        @Test
        @DisplayName("Meme title edited successfully")
        void MemeTitleEditedSuccessfully() {
            updateAndViewMeme.goToRegistrationPage();
            assertEquals("https://webmemes.devhub.dev.br/index.html", driver.getCurrentUrl());

            WebElement selectElement = driver.findElement(By.id("type"));
            Select select = new Select(selectElement);
            select.selectByIndex(0);//seleciona imagem


            driver.findElement(By.id("url")).sendKeys("https://www.hondacaiuas.com.br/wp-content/uploads/2022/08/tipos-de-carro-hatch-new-city-hatchback.jpg");
            driver.findElement(By.id("title")).sendKeys("New Meme");
            driver.findElement(By.id("comment")).sendKeys("This is a test meme");

            driver.findElement(By.xpath("//button[text()='Cadastrar Meme']")).click();

            driver.get("https://webmemes.devhub.dev.br/visualizar.html");

            updateAndViewMeme.editButton();

            WebElement titleField = driver.findElement(By.id("title"));
            titleField.clear();
            titleField.sendKeys("Edit Title");

            driver.findElement(By.id("submitMeme")).click();

            WebElement updatedTitle = driver.findElement(By.xpath("//*[@id=\"memeList\"]/tr/td[2]"));

            assertEquals("Edit Title", updatedTitle.getText());
        }
    }


}
