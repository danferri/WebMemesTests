package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EdicaoEVisualizacaoMemePage {
    protected final WebDriver driver;

    private final By voltarPaginaDeRegistro = By.xpath("/html/body/a");
    private final By proximoButton = By.xpath("//*[@id=\"nextPage\"]");
    private final By anteriorButton = By.xpath("//*[@id=\"prevPage\"]");
    private final By numeroPagina = By.xpath("//*[@id=\"pageIndicator\"]");
    private final By tableView = By.xpath("//*[@id=\"memeTable\"]");
    private final By title = By.xpath("/html/body/h1");
    private final By editarMemeText = By.xpath("/html/body/div[3]/h2");
    private final By contadorDePaginaText = By.xpath("//*[@id=\"pageIndicator\"]");



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



    public void goToRegistrationPage(){
        driver.findElement(voltarPaginaDeRegistro).click();
    }

    public boolean getProximo(){
        WebElement proximoButtonElement = driver.findElement(proximoButton);

        return proximoButtonElement.isEnabled();
    }

    public void goToNextPage(){
        driver.findElement(proximoButton).click();
    }
    public void goToPreviousPage(){
        driver.findElement(anteriorButton).click();
    }

    public String identifyNumberOfPage(){
        return driver.findElement(numeroPagina).getText();
    }


    public boolean isTableSemanticCorrect() {
        try {

            WebElement table = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(tableView));


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


    public boolean isTextSemanticCorrect() {
            return false;
    }
}
