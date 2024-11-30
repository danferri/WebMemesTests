package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EdicaoEVisualizacaoMemePage {
    protected final WebDriver driver;

    private final By voltarPaginaDeRegistro = By.xpath("/html/body/a");

    public EdicaoEVisualizacaoMemePage(WebDriver driver) {
        this.driver = driver;

        if(!driver.getCurrentUrl().equals("https://webmemes.devhub.dev.br/visualizar.html")) {
            throw new IllegalStateException("This is not the Edicao e vizualizacao de Meme page," +
                    "current page is: " + driver.getCurrentUrl());
        }

    }

    public void goToRegistrationPage(){driver.findElement(voltarPaginaDeRegistro).click();}

}
