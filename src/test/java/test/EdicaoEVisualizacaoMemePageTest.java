package test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.EdicaoEVisualizacaoMemePage;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class EdicaoEVisualizacaoMemePageTest {
    private WebDriver driver;
    static WebDriverWait webDriverWait;

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

    @Nested
    @DisplayName("Navigation and Pagination Tests")
    class NavigationAndPaginationTests {
        @Test
        @DisplayName("Should change page when click return to registration")
        void shoundChangePageWhenClickReturnToRegitration() {
            updateAndViewMeme.goToRegistrationPage();

            assertEquals("https://webmemes.devhub.dev.br/index.html", driver.getCurrentUrl());
        }

        @Test
        @DisplayName("Should not change when 'Proximo' button is disabled")
        void shouldNotChangeWhenProximoButtonIsDisabled() {


            if (!updateAndViewMeme.getProximo()) {

                String firstPage = updateAndViewMeme.identifyNumberOfPage();
                updateAndViewMeme.goToNextPage();
                String secondPage = updateAndViewMeme.identifyNumberOfPage();

                boolean isPageSame = firstPage.equals(secondPage);
                assertTrue(isPageSame, "A página não deveria ter mudado, pois o botão estava desativado");
            } else {
                fail("O botão 'Próximo' deveria estar desativado.");
            }
        }

        @Test
        @DisplayName("Should change page when click proximo")
        void shouldChangePageWhenClickProximo() {
            String firstPage = updateAndViewMeme.identifyNumberOfPage();
            updateAndViewMeme.goToNextPage();
            String secondPage = updateAndViewMeme.identifyNumberOfPage();

            boolean res = !firstPage.equals(secondPage);

            assertTrue(res);
        }

        @Test
        @DisplayName("Should report a error when change page when click anterior and first page is 1")
        void shouldReportAErrorWhenChangePageWhenClickAnteriorAndFirstPageIs1() {
            boolean isOnFirstPage = updateAndViewMeme.identifyNumberOfPage().equals("Página 1");
            if (isOnFirstPage) {
                try {
                    updateAndViewMeme.goToPreviousPage();

                    fail("Esperava-se uma exceção ao tentar voltar da pagina 1");
                } catch (Exception e) {
                    assertEquals("Não é possível voltar da página 1", e.getMessage());
                }
            }
        }
    }

    @Nested
    @DisplayName("Edit Field Validation Tests")
    class ValidateRoles {
        @Test
        @DisplayName("Should error message when try edit mandatory fields to empty")
        void shouldErrorMessageWhenTryEditMandatoryFieldsToEmpty() {
            updateAndViewMeme.createItemToTestUpdate();

            updateAndViewMeme.editButton();

            updateAndViewMeme.clearFields(
                    Arrays.asList(
                            updateAndViewMeme.getEditUrlInput(),
                            updateAndViewMeme.getEditTitleInput()
                    )
            );

            updateAndViewMeme.clickInSave();

            assertTrue(updateAndViewMeme.isErrorMessageDisplayed());

        }

        @Test
        @DisplayName("Should not edit message when edit mandatory fields to empty")
        void shouldNotEditMessageWhenEditMandatoryFieldsToEmpty() {
            updateAndViewMeme.createItemToTestUpdate();

            updateAndViewMeme.editButton();

            String urlValueBeforeTryingToEdit = driver.findElement(updateAndViewMeme.getEditUrlInput()).getAttribute("value");
            String titleValueBeforeTryingToEdit = driver.findElement(updateAndViewMeme.getEditTitleInput()).getAttribute("value");


            updateAndViewMeme.clearFields(
                    Arrays.asList(
                            updateAndViewMeme.getEditUrlInput(),
                            updateAndViewMeme.getEditTitleInput()
                    )
            );

            updateAndViewMeme.clickInSave();

            String urlThatWasRecorded =
                    driver.findElement(By.xpath("//*[@id=\"memeList\"]/tr/td[1]/img")).getAttribute("src");

            String titleThatWasRecorded =
                    driver.findElement(By.xpath("//*[@id=\"memeList\"]/tr/td[2]")).getText();

            assertEquals(urlThatWasRecorded, urlValueBeforeTryingToEdit);
            assertEquals(titleThatWasRecorded, titleValueBeforeTryingToEdit);

        }


        @Test
        @DisplayName("Should not edit select and url for types that are incompatible with each other")
        void shouldNotEditSelectAndUrlForTypesIncompatible() {

            updateAndViewMeme.createItemToTestUpdate();

            updateAndViewMeme.editButton();

            SoftAssertions softly = new SoftAssertions();

            WebElement selectElement = driver.findElement(updateAndViewMeme.getSelect());
            WebElement selectedValueBeforeTryingToEdit = new Select(selectElement).getFirstSelectedOption();
            String urlValueBeforeTryingToEdit = driver.findElement(updateAndViewMeme.getEditUrlInput()).getAttribute("value");


            updateAndViewMeme.clearFields(
                    Collections.singletonList(
                            updateAndViewMeme.getEditUrlInput()
                    )
            );


            Select select = new Select(selectElement);

            select.selectByIndex(0);
            driver.findElement(updateAndViewMeme.getEditUrlInput()).sendKeys("https://www.youtube.com/watch?v=V5vWvs2clBw&list=PLL34mf651faO1vJWlSoYYBJejN9U_rwy-&index=2");


            updateAndViewMeme.clickInSave();


            String urlThatWasRecorded = driver.findElement(By.xpath("//*[@id=\"memeList\"]/tr/td[1]/img")).getAttribute("src");

            softly.assertThat(urlThatWasRecorded)
                    .as("A URL não deveria ter sido atualizada")
                    .isEqualTo(urlValueBeforeTryingToEdit);


            WebElement selectedValueAfterTryingToEdit = new Select(selectElement).getFirstSelectedOption();
            softly.assertThat(selectedValueAfterTryingToEdit.getText())
                    .as("O valor do select não deveria ter sido alterado")
                    .isEqualTo(selectedValueBeforeTryingToEdit.getText());


            softly.assertAll();
        }


        @Test
        @DisplayName("Should display error message when try edit select and url for types that are incompatible with each other")
        void shouldDisplayErrorMessageWhenTryEditSelectAndUrlForTypesIncompatible() {

            updateAndViewMeme.createItemToTestUpdate();

            updateAndViewMeme.editButton();

            WebElement selectElement = driver.findElement(updateAndViewMeme.getSelect());


            updateAndViewMeme.clearFields(
                    Collections.singletonList(
                            updateAndViewMeme.getEditUrlInput()
                    )
            );


            Select select = new Select(selectElement);

            select.selectByIndex(0);
            driver.findElement(updateAndViewMeme.getEditUrlInput()).sendKeys("https://www.youtube.com/watch?v=V5vWvs2clBw&list=PLL34mf651faO1vJWlSoYYBJejN9U_rwy-&index=2");


            updateAndViewMeme.clickInSave();


            assertTrue(updateAndViewMeme.isErrorMessageDisplayed());

        }

        @Test
        @DisplayName("Should not edit title when text shorter than 3 characters")
        void shouldNotEditTitleWhenTextShorterThan3Character() {

            updateAndViewMeme.createItemToTestUpdate();

            updateAndViewMeme.editButton();

            SoftAssertions softly = new SoftAssertions();


            String titleValueBeforeTryingToEdit = driver.findElement(updateAndViewMeme.getEditTitleInput()).getAttribute("value");


            updateAndViewMeme.clearFields(
                    Collections.singletonList(
                            updateAndViewMeme.getEditTitleInput()
                    )
            );


            driver.findElement(updateAndViewMeme.getEditTitleInput()).sendKeys("oi");


            updateAndViewMeme.clickInSave();


            String titleThatWasRecorded = driver.findElement(By.xpath("//*[@id=\"memeList\"]/tr/td[2]")).getText();

            softly.assertThat(titleThatWasRecorded)
                    .as("O titulo não deveria ter sido atualizado")
                    .isEqualTo(titleValueBeforeTryingToEdit);


            softly.assertAll();
        }

        @Test
        @DisplayName("Should not edit title when text bigger than 50 characters")
        void shouldNotEditTitleWhenTextBiggerThan50Character() {
            updateAndViewMeme.createItemToTestUpdate();
            updateAndViewMeme.editButton();

            SoftAssertions softly = new SoftAssertions();
            String titleValueBeforeTryingToEdit =
                    driver.findElement(updateAndViewMeme.getEditTitleInput()).getAttribute("value");

            updateAndViewMeme.clearFields(
                    Collections.singletonList(
                            updateAndViewMeme.getEditTitleInput()
                    )
            );

            driver.findElement(updateAndViewMeme.getEditTitleInput()).sendKeys("Lorem ipsum dolor sit amet," +
                    " consectetur adipiscing elit. Nullam scelerisque orci vitae sapien interdum, id dignissim lectus porttitor.");
            updateAndViewMeme.clickInSave();

            String titleThatWasRecorded = driver.findElement(By.xpath("//*[@id=\"memeList\"]/tr/td[2]")).getText();
            softly.assertThat(titleThatWasRecorded)
                    .as("O titulo não deveria ter sido atualizado")
                    .isEqualTo(titleValueBeforeTryingToEdit);
            softly.assertAll();
        }

        @Test
        @DisplayName("Should display error message when try edit title to text shorter than 3 characters")
        void shouldDisplayErrorMessageWhenTryEditTitleToTextShorterThan3Characters() {
            updateAndViewMeme.createItemToTestUpdate();
            updateAndViewMeme.editButton();
            updateAndViewMeme.clearFields(
                    Collections.singletonList(
                            updateAndViewMeme.getEditTitleInput()
                    )
            );

            driver.findElement(updateAndViewMeme.getEditTitleInput()).sendKeys("oi");
            updateAndViewMeme.clickInSave();
            assertTrue(updateAndViewMeme.isErrorMessageDisplayed());
        }

        @Test
        @DisplayName("Should be possible to edit description to empty")
        void shouldBePossibleToEditDescriptionToEmpty() {

            updateAndViewMeme.createItemToTestUpdate();

            updateAndViewMeme.editButton();

            SoftAssertions softly = new SoftAssertions();

            String newValue = "";

            String displayTextInTableWhenValueIsEmpty = "-";

            updateAndViewMeme.clearFields(
                    Collections.singletonList(
                            updateAndViewMeme.getEditDescriptionInput()
                    )
            );

            driver.findElement(updateAndViewMeme.getEditDescriptionInput()).sendKeys(newValue);

            updateAndViewMeme.clickInSave();

            String descriptionThatWasRecorded = driver.findElement(By.xpath("//*[@id=\"memeList\"]/tr[1]/td[3]")).getText();

            softly.assertThat(descriptionThatWasRecorded)
                    .as("A descrição deveria estar vazia após a edição")
                    .isEqualTo(displayTextInTableWhenValueIsEmpty);

            softly.assertAll();
        }

        @Test
        @DisplayName("Should display error message when try edit title to text bigger than 50 characters")
        void shouldDisplayErrorMessageWhenTryEditTitleToTextBiggerThan50Characters() {

            updateAndViewMeme.createItemToTestUpdate();

            updateAndViewMeme.editButton();


            updateAndViewMeme.clearFields(
                    Collections.singletonList(
                            updateAndViewMeme.getEditTitleInput()
                    )
            );


            driver.findElement(updateAndViewMeme.getEditTitleInput()).sendKeys("Lorem ipsum dolor sit amet, " +
                    "consectetur adipiscing elit. Nullam scelerisque orci vitae sapien interdum, id dignissim lectus porttitor.");


            updateAndViewMeme.clickInSave();


            assertTrue(updateAndViewMeme.isErrorMessageDisplayed());

        }
    }

    @Nested
    @DisplayName("Comment Validation Tests")
    class CommentValidationTests {


        @Test
        @DisplayName("Should not edit comment to empty")
        void shouldNotEditCommentToEmpty() {
            updateAndViewMeme.createItemToTestUpdate();

            String lastValue = "last value";


            SoftAssertions softly = new SoftAssertions();


            updateAndViewMeme.commentButton();

            driver.findElement(By.id("newComment")).sendKeys(lastValue);
            driver.findElement(By.xpath("//*[@id=\"commentForm\"]/button")).click();


            updateAndViewMeme.goToEditComment();

            updateAndViewMeme.clearFields(List.of(By.id("newComment")));
            driver.findElement(By.xpath("//*[@id=\"commentForm\"]/button")).click();

            String commentThatWasRecorded = driver.findElement(By.xpath("//*[@id=\"commentsList\"]/tr/td[1]")).getText();


            softly.assertThat(lastValue)
                    .as("O comentário não poderia ser editado para vazio")
                    .isEqualTo(commentThatWasRecorded);

            softly.assertAll();


        }

        @Test
        @DisplayName("should display an error message when the comment cannot be changed")
        void shouldDisplayAnErrorMessageWhenTheCommentCannotBeChanged() {
            updateAndViewMeme.createItemToTestUpdate();

            String lastValue = "last value";


            updateAndViewMeme.commentButton();

            driver.findElement(By.id("newComment")).sendKeys(lastValue);
            driver.findElement(By.xpath("//*[@id=\"commentForm\"]/button")).click();


            updateAndViewMeme.goToEditComment();

            updateAndViewMeme.clearFields(List.of(By.id("newComment")));
            driver.findElement(By.xpath("//*[@id=\"commentForm\"]/button")).click();

            assertTrue(updateAndViewMeme.isErrorMessageInCommentsDisplayed());


        }

    }

    @Nested
    @DisplayName("Edit functionality tests")
    class EditMemeTest {

        @Test
        @DisplayName("Meme title edited successfully")
        void MemeTitleEditedSuccessfully() {

            updateAndViewMeme.createItemToTestUpdate();

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
            updateAndViewMeme.createItemToTestUpdate();

            updateAndViewMeme.editButton();

            WebElement urlField = driver.findElement(By.id("url"));
            urlField.clear();
            urlField.sendKeys("https://www.hondacaiuas.com.br/wp-content.jpg");

            driver.findElement(By.id("submitMeme")).click();

            WebElement updatedURL = driver.findElement(By.xpath("//*[@id=\"memeList\"]/tr/td[1]/img"));
            String imagemUrl = updatedURL.getAttribute("src");

            assertEquals("https://www.hondacaiuas.com.br/wp-content/uploads/2022/08/tipos-de-carro-hatch-new-city-hatchback.jpg", imagemUrl);
        }

        @Test
        @DisplayName("Should not stay the message of edit with success after delete it")
        void ShouldNotStayTheMessageOfEditWithSuccessAfterDeleteIt() {
            updateAndViewMeme.createItemToTestUpdate();

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
    @DisplayName("Semantic html tests")
    class SemanticHTML {
        @Test
        @DisplayName("Table should have the correct html semantics")
        void tableShouldHaveTheCorrectSemantics() {
            boolean isSemanticCorrect = updateAndViewMeme.tableViewHasCorrectSemanticTable();
            assertTrue(isSemanticCorrect);
        }

        @Test
        @DisplayName("Text should have the correct html semantics")
        void textShouldHaveTheCorrectHtmlSemantics() {
            boolean isSemanticCorrect = updateAndViewMeme.isTextSemanticCorrect();
            assertTrue(isSemanticCorrect);
        }

        @Test
        @DisplayName("Table comments should have the correct html semantics")
        void tableCommentsShouldHaveTheCorrectHtmlSemantics() {
            updateAndViewMeme.createItemToTestUpdate();
            updateAndViewMeme.commentButton();

            boolean isSemanticCorrect = updateAndViewMeme.tableViewHasCorrectsemanticCommentsTable();
            assertTrue(isSemanticCorrect);
        }

        @Test
        @DisplayName("Comment text should have the correct html semantics")
        void commentTextShouldHaveTheCorrectHtmlSemantics() {
            updateAndViewMeme.createItemToTestUpdate();
            updateAndViewMeme.commentButton();

            boolean isSemanticCorrect = updateAndViewMeme.isCommentTextSemantiCorrect();
            assertTrue(isSemanticCorrect);
        }

    }

    @Nested
    @DisplayName("remove functionality test")
    class RemoveTests {
        @Test
        @DisplayName("Should remove message is right")
        void ShouldRemoveRemoveMessageIsRight() {
            updateAndViewMeme.createItemToTestUpdate();

            updateAndViewMeme.removeButton();
            String deleteMessage = driver.findElement(By.id("deleteMessage")).getText();

            assertEquals(deleteMessage, "Meme deletado com sucesso!");
        }

        @Test
        @DisplayName("Should remove with success")
        void ShouldRemoveWithSuccess() throws InterruptedException {
            updateAndViewMeme.createItemToTestUpdate();

            int before = updateAndViewMeme.getQuantityOfRows();
            updateAndViewMeme.removeButton();
            int after = updateAndViewMeme.getQuantityOfRows();

            if (after < before) {
                assertTrue(true);
            } else {
                fail("O elemento não foi apagado");
            }


        }
    }

    @Nested
    @DisplayName("Comments functionality tests")
    class CommentsTest {

        @Test
        @DisplayName("Should open the comment modal")
        void ShouldOpenTheCommentModal() {
            updateAndViewMeme.createItemToTestUpdate();


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
            updateAndViewMeme.createItemToTestUpdate();

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
        @DisplayName("Should handle large comment that exceeds modal width")
        void shouldHandleLargeCommentExceedingModalWidth() {
            updateAndViewMeme.createItemToTestUpdate();

            updateAndViewMeme.commentButton();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='commentsPopup']/div")));


            String largeComment = "This is a very long comment that will test how the modal handles content that exceeds the modal width. This is a test to see how the comment behaves when it is too wide for the modal container. Let's check if the modal handles overflow properly.";
            driver.findElement(By.id("newComment")).sendKeys(largeComment);
            driver.findElement(By.xpath("//*[@id=\"commentForm\"]/button")).click();


            WebElement commentModal = driver.findElement(By.xpath("//*[@id='commentsPopup']/div"));
            String overflowX = commentModal.getCssValue("overflow-x");


            assertTrue(overflowX.equals("auto") || overflowX.equals("scroll"), "O modal permite overflow horizontal.");


            List<WebElement> comments = driver.findElements(By.xpath("//div[@class='comment-item']"));
            assertFalse(comments.isEmpty(), "O comentário grande não foi adicionado.");


            String commentText = comments.get(comments.size() - 1).getText();
            assertTrue(commentText.length() < largeComment.length(), "O comentário grande não foi truncado corretamente.");
        }


    }

    @Nested
    @DisplayName("Comments UI tests")
    class CommentsUITests {
        @Test
        @DisplayName("Should not stay the message of add comment with success after close and open the modal")
        void shouldNotStayTheMessageOfAddCommentWithSuccessAfterCloseAndOpenTheModal() {
            updateAndViewMeme.createItemToTestUpdate();

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

    }
}
