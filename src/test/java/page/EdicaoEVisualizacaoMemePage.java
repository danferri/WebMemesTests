package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EdicaoEVisualizacaoMemePage {
    protected final WebDriver driver;
    private final By voltarPaginaDeRegistroLink = By.xpath("/html/body/a");
    private final By proximoButton = By.xpath("//*[@id=\"nextPage\"]");
    private final By anteriorButton = By.xpath("//*[@id=\"prevPage\"]");
    private final By numeroPaginaText = By.xpath("//*[@id=\"pageIndicator\"]");
    private final By tableView = By.xpath("//*[@id=\"memeTable\"]");
    private final By tableCommentsView = By.xpath("//*[@id=\"commentsTable\"]");
    private final By editDescriptionInput = By.xpath("//*[@id=\"comment\"]");
    private final By messageOfErrorOrSuccessText = By.xpath("//*[@id=\"message\"]");
    private final By messageOfErrorOrSuccessInCommentModal = By.xpath("//*[@id=\"commentMessage\"]");
    private final By editUrlInput = By.xpath("//*[@id=\"url\"]");
    private final By saveButton = By.id("submitMeme");
    private final By editTitleInput = By.xpath("//*[@id=\"title\"]");
    private final By titleText = By.xpath("/html/body/h1");
    private final By editarMeme = By.xpath("/html/body/div[3]/h2");
    private final By editButton = By.xpath("//*[@id=\"memeList\"]/tr/td[5]/button[1]");
    private final By contadorDePagina = By.xpath("//*[@id=\"pageIndicator\"]");
    private final By removeButton = By.xpath("//*[@id=\"memeList\"]/tr/td[5]/button[2]");
    private final By comentariosButton = By.xpath("//*[@id=\"memeList\"]/tr/td[4]/button");
    private final By commentTitle = By.xpath("//*[@id=\"commentsPopup\"]/div/h2");
    private final By editCommentButton = By.xpath("//*[@id=\"commentsList\"]/tr/td[2]/button[1]");
    private final By select = By.id("type");

    public EdicaoEVisualizacaoMemePage(WebDriver driver) {
        this.driver = driver;


        List<String> permittedNavigationPages = Arrays.asList(
                "https://webmemes.devhub.dev.br/visualizar.html",
                "https://webmemes.devhub.dev.br/"
        );


        if (!permittedNavigationPages.contains(driver.getCurrentUrl())) {
            throw new IllegalStateException("This is not the Edicao e Visualizacao de Meme page, " +
                    "current page is: " + driver.getCurrentUrl());
        }
    }

    public void clearFields(List<By> locators) {
        for (By locator : locators) {
            try {
                WebElement element = driver.findElement(locator);
                element.clear();

            } catch (Exception e) {
                System.err.println("Erro ao limpar o campo localizado por " + locator + ": " + e.getMessage());
            }
        }
    }


    public int getQuantityOfRows() {

        WebElement table = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(tableView));

        WebElement tbody = table.findElement(By.tagName("tbody"));

        if (tbody == null) return 0;

        return tbody.findElements(By.tagName("tr")).size();
    }


    public boolean isErrorMessageDisplayed() {
        try {
            String[] errorMessages = {
                    "A URL não corresponde ao tipo selecionado (imagem ou vídeo).",
                    "O título deve ter entre 3 e 50 caracteres.",
                    "URL inválida para o tipo selecionado."
            };


            if (driver.findElements(messageOfErrorOrSuccessText).isEmpty()) return false;


            String actualMessage = driver.findElement(messageOfErrorOrSuccessText).getText();


            return Arrays.asList(errorMessages).contains(actualMessage);
        } catch (Exception e) {
            System.err.println("Erro ao verificar a mensagem: " + e.getMessage());
            return false;
        }
    }


    public boolean isErrorMessageInCommentsDisplayed() {
        try {
            String[] errorMessages = {
                    "Erro ao alterar comentário."
            };


            if (driver.findElements(messageOfErrorOrSuccessInCommentModal).isEmpty()) return false;


            String actualMessage = driver.findElement(messageOfErrorOrSuccessInCommentModal).getText();


            return Arrays.asList(errorMessages).contains(actualMessage);
        } catch (Exception e) {
            System.err.println("Erro ao verificar a mensagem: " + e.getMessage());
            return false;
        }
    }


    public boolean isTableSemanticCorrect(By tableBy) {
        try {

            WebElement table = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(tableBy));


            WebElement thead = table.findElement(By.tagName("thead"));

            if (thead == null) return false;


            List<WebElement> headerRows = thead.findElements(By.tagName("tr"));

            if (headerRows.isEmpty()) return false;


            for (WebElement row : headerRows) {
                List<WebElement> headers = row.findElements(By.tagName("th"));
                if (headers.isEmpty()) return false;

            }


            WebElement tbody = table.findElement(By.tagName("tbody"));

            if (tbody == null) return false;

            List<WebElement> bodyRows = tbody.findElements(By.tagName("tr"));

            if (bodyRows.isEmpty()) return true;

            for (WebElement row : bodyRows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.isEmpty()) return false;

            }

            return true;
        } catch (Exception e) {
            System.err.println("Error to validate table: " + e.getMessage());
            return false;
        }
    }

    public boolean isCommentTextSemantiCorrect() {
        try {
            List<String> titlesTags = Arrays.asList("h1", "h2", "h3", "h4", "h5");

            WebElement titleElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(commentTitle));

            String actualTagNameTitle = titleElement.getTagName();
            return titlesTags.contains(actualTagNameTitle);

        } catch (Exception e) {
            System.err.println("Erro ao validar a semântica HTML: " + e.getMessage());
            return false;
        }
    }


    public boolean isTextSemanticCorrect() {
        try {
            List<String> titlesTags = Arrays.asList("h1", "h2", "h3", "h4", "h5");
            List<String> textsTags = Arrays.asList("p", "span");

            WebElement titleElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(titleText));

            WebElement editarMemeText = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(editarMeme));

            WebElement contadorDePaginaText = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(contadorDePagina));


            String actualTagNameTitle = titleElement.getTagName();
            String actualTagNameEditMeme = editarMemeText.getTagName();
            String actualTagNameCountPage = contadorDePaginaText.getTagName();


            return titlesTags.contains(actualTagNameTitle) && titlesTags.contains(actualTagNameEditMeme) && textsTags.contains(actualTagNameCountPage);

        } catch (Exception e) {
            System.err.println("Erro ao validar a semântica HTML: " + e.getMessage());
            return false;
        }
    }

    public void createItemToTestUpdate() {
        goToRegistrationPage();
        assertEquals("https://webmemes.devhub.dev.br/index.html", driver.getCurrentUrl());

        WebElement selectElement = driver.findElement(By.id("type"));
        Select select = new Select(selectElement);
        select.selectByIndex(0);


        driver.findElement(By.id("url")).sendKeys("https://www.hondacaiuas.com.br/wp-content/uploads/2022/08/tipos-de-carro-hatch-new-city-hatchback.jpg");
        driver.findElement(By.id("title")).sendKeys("New Meme");
        driver.findElement(By.id("comment")).sendKeys("This is a test meme");

        driver.findElement(By.xpath("//button[text()='Cadastrar Meme']")).click();

        driver.get("https://webmemes.devhub.dev.br/visualizar.html");
    }


    public void goToRegistrationPage() {
        driver.findElement(voltarPaginaDeRegistroLink).click();
    }

    public boolean getProximo() {
        WebElement proximoButtonElement = driver.findElement(proximoButton);

        return proximoButtonElement.isEnabled();
    }

    public By getSelect() {
        return select;
    }

    public By getSaveButton() {
        return saveButton;
    }

    public By getEditDescriptionInput() {
        return editDescriptionInput;
    }

    public By getEditUrlInput() {
        return editUrlInput;
    }

    public By getEditTitleInput() {
        return editTitleInput;
    }

    public void goToNextPage() {
        driver.findElement(proximoButton).click();
    }

    public void goToEditComment() {
        driver.findElement(editCommentButton).click();
    }

    public void goToPreviousPage() {
        driver.findElement(anteriorButton).click();
    }

    public String identifyNumberOfPage() {
        return driver.findElement(numeroPaginaText).getText();
    }

    public void editButton() {
        driver.findElement(editButton).click();
    }

    public void clickInSave() {
        driver.findElement(saveButton).click();
    }

    public void removeButton() {
        driver.findElement(removeButton).click();
    }

    public void commentButton() {
        driver.findElement(comentariosButton).click();
    }

    public boolean tableViewHasCorrectSemanticTable() {
        return isTableSemanticCorrect(tableView);
    }

    public boolean tableViewHasCorrectsemanticCommentsTable() {
        return isTableSemanticCorrect(tableCommentsView);
    }

}
