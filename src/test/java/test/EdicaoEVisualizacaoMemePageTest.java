package test;



import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.EdicaoEVisualizacaoMemePage;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
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

    void createItemToTestUpdate(){
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

            createItemToTestUpdate();

            updateAndViewMeme.editButton();

            WebElement titleField = driver.findElement(By.id("title"));
            titleField.clear();
            titleField.sendKeys("Edit Title");

            driver.findElement(By.id("submitMeme")).click();

            WebElement updatedTitle = driver.findElement(By.xpath("//*[@id=\"memeList\"]/tr/td[2]"));

            assertEquals("Edit Title", updatedTitle.getText());
        }


        @Test
        @DisplayName("Should not edit a meme URL")
        void ShouldNotEditMemeURL() {
            createItemToTestUpdate();

            updateAndViewMeme.editButton();

            WebElement urlField = driver.findElement(By.id("url"));
            urlField.clear();
            urlField.sendKeys("https://www.hondacaiuas.com.br/wp-content.jpg");

            driver.findElement(By.id("submitMeme")).click();

            WebElement updatedURL = driver.findElement(By.xpath("//*[@id=\"memeList\"]/tr/td[1]/img"));
            String imagemUrl = updatedURL.getAttribute("src");

            assertEquals("https://www.hondacaiuas.com.br/wp-content/uploads/2022/08/tipos-de-carro-hatch-new-city-hatchback.jpg",imagemUrl);
        }

        @Test
        @DisplayName("Should not stay the message of edit with success after delete it")
        void ShouldNotStayTheMessageOfEditWithSuccessAfterDeleteIt(){
            createItemToTestUpdate();

            updateAndViewMeme.editButton();

            WebElement titleField = driver.findElement(By.id("title"));
            titleField.clear();
            titleField.sendKeys("Edit Title");

            driver.findElement(By.id("submitMeme")).click();

            updateAndViewMeme.removeButton();

            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"message\"]")));

                assertTrue(true);
            } catch (TimeoutException e) {
                fail("A mensagem de sucesso ainda está visível após a remoção.");
            }



        }


    }

    @Nested
    @DisplayName("remove test")
    class RemoveTests{
        @Test
        @DisplayName("Should remove message is right")
        void ShouldRemoveRemoveMessageIsRight(){
            createItemToTestUpdate();

            updateAndViewMeme.removeButton();
            String deleteMessage = driver.findElement(By.id("deleteMessage")).getText();

            assertEquals(deleteMessage, "Meme deletado com sucesso!");
        }

        @Test
        @DisplayName("Should remove with success")
        void ShouldRemoveWithSuccess() throws InterruptedException {
            createItemToTestUpdate();

            updateAndViewMeme.removeButton();

            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id='memeList']/tr[1]")));

                fail("O elemento ainda está presente após a remoção.");
            } catch (TimeoutException e) {
                assertTrue(true);
            }


        }
    }

    @Nested
    @DisplayName("Comments tests")
    class CommentsTest {

        @Test
        @DisplayName("Should open the comment modal")
        void ShouldOpenTheCommentModal() {
            createItemToTestUpdate();


            try {
                updateAndViewMeme.commentButton();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"commentsPopup\"]/div")));
                assertTrue(true);

            } catch (TimeoutException e) {
                fail("O modal de comentário não foi aberto dentro do tempo esperado.");
            }

        }

        @Test
        @DisplayName("Should comment with success")
        void ShouldCommentWithSuccess() {
            createItemToTestUpdate();
            try {
                updateAndViewMeme.commentButton();
                driver.findElement(By.id("newComment")).sendKeys("New Comment");
                driver.findElement(By.xpath("//*[@id=\"commentForm\"]/button")).click();
                String message = driver.findElement(By.xpath("//*[@id=\"commentMessage\"]")).getText();

                assertEquals(message, "Comentário adicionado com sucesso!");

            } catch (NoSuchElementException e) {
                fail("Mensagem de sucesso nao apareceu");
            }

        }

        @Test
        @DisplayName("Should not stay the message of add comment with success after close and open the modal")
        void shouldNotStayTheMessageOfAddCommentWithSuccessAfterCloseAndOpenTheModal() {
            createItemToTestUpdate();

            try {
                updateAndViewMeme.commentButton();
                driver.findElement(By.id("newComment")).sendKeys("New Comment");
                driver.findElement(By.xpath("//*[@id=\"commentForm\"]/button")).click();
                driver.findElement(By.xpath("//*[@id=\"commentsPopup\"]/div/span")).click();

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"commentMessage\"]")));

                assertTrue(true);
            } catch (TimeoutException e) {
                fail("O Comentario continua la após sair de comentarios");
            }

        }


        @Test
        @DisplayName("Should handle large comment that exceeds modal width")
        void shouldHandleLargeCommentExceedingModalWidth() {
            createItemToTestUpdate();

            updateAndViewMeme.commentButton(); // Abre o modal

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='commentsPopup']/div")));

            // Adicionando um comentário largo (em largura)
            String largeComment = "This is a very long comment that will test how the modal handles content that exceeds the modal width. This is a test to see how the comment behaves when it is too wide for the modal container. Let's check if the modal handles overflow properly.";
            driver.findElement(By.id("newComment")).sendKeys(largeComment);
            driver.findElement(By.xpath("//*[@id=\"commentForm\"]/button")).click();


            // Verificar se o modal tem overflow
            WebElement commentModal = driver.findElement(By.xpath("//*[@id='commentsPopup']/div"));
            String overflowX = commentModal.getCssValue("overflow-x");

            // O modal não deve permitir overflow horizontal ou deve exibir uma barra de rolagem
            assertTrue(overflowX.equals("auto") || overflowX.equals("scroll"), "O modal permite overflow horizontal.");

            // Verifique se o comentário foi corretamente adicionado ao final da lista
            List<WebElement> comments = driver.findElements(By.xpath("//div[@class='comment-item']"));
            assertFalse(comments.isEmpty(), "O comentário grande não foi adicionado.");

            // Verifique se o comentário grande não saiu pela lateral
            String commentText = comments.get(comments.size() - 1).getText();
            assertTrue(commentText.length() < largeComment.length(), "O comentário grande não foi truncado corretamente.");
        }


    }


}
