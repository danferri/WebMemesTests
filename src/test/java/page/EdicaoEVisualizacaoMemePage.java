package page;

import org.openqa.selenium.WebDriver;

public class EdicaoEVisualizacaoMemePage {
    protected final WebDriver driver;

    public EdicaoEVisualizacaoMemePage(WebDriver driver) {
        this.driver = driver;

        if(!driver.getCurrentUrl().equals("https://webmemes.devhub.dev.br/visualizar.html")) {
            throw new IllegalStateException("This is not the Edicao e vizualizacao de Meme page," +
                    "current page is: " + driver.getCurrentUrl());
        }
    }
}
