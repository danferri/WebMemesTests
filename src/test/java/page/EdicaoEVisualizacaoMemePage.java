package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class EdicaoEVisualizacaoMemePage {
    protected final WebDriver driver;

    private final By voltarPaginaDeRegistro = By.xpath("/html/body/a");
    private final By proximoButton = By.xpath("//*[@id=\"nextPage\"]");
    private final By anteriorButton = By.xpath("//*[@id=\"prevPage\"]");
    private final By numeroPagina = By.xpath("//*[@id=\"pageIndicator\"]");

    private final By tableView = By.xpath("//*[@id=\"memeTable\"]");

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
            driver.findElement(tableView);
            return false;
    }
}
