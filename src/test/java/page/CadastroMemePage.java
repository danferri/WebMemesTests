package page;

import model.Meme;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class CadastroMemePage {
    protected final WebDriver driver;

    private final By tipoMemeSelect = By.xpath("//*[@id=\"type\"]");
    private final By urlMemeInput = By.xpath("//*[@id=\"url\"]");
    private final By tituloMemeInput = By.xpath("//*[@id=\"title\"]");
    private final By descricaoMemeInput = By.xpath("//*[@id=\"comment\"]");
    private final By cadastrarMemeButton = By.xpath("//*[@id=\"memeForm\"]/button");
    private final By visualizarMemesLink = By.xpath("/html/body/div[1]/a");
    private final By messageSpam = By.xpath("//*[@id=\"message\"]");



    public CadastroMemePage(WebDriver driver) {
        this.driver = driver;

        if(!driver.getCurrentUrl().equals("https://webmemes.devhub.dev.br/index.html")) {
            throw new IllegalStateException("This is not the Cadastro de Meme page," +
                    "current page is: " + driver.getCurrentUrl());
        }
    }

    public void goToMemesCadastradosPage() {
        driver.findElement(visualizarMemesLink).click();
    }

    public void cadastroMemeFromMeme(Meme meme) {
        Select selectTipo = new Select(driver.findElement(tipoMemeSelect));
        selectTipo.selectByValue(meme.getTipo().getString());
        driver.findElement(urlMemeInput).sendKeys(meme.getUrl());
        driver.findElement(tituloMemeInput).sendKeys(meme.getTitulo());
        driver.findElement(descricaoMemeInput).sendKeys(meme.getDescricao());
        driver.findElement(cadastrarMemeButton).click();
    }

    public boolean checkMessageSuccessful() {
        String message = "Meme cadastrado com sucesso!";
        if(driver.findElement(messageSpam).getText().equals(message)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkMessageErrorUrl() {
        String message = "Por favor, insira uma URL válida.";
        if(driver.findElement(messageSpam).getText().equals(message)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkMessageErrorUrlType() {
        String message = "URL inválida para o tipo selecionado.";
        if(driver.findElement(messageSpam).getText().equals(message)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkMessageTitleError() {
        String message = "O título deve ter entre 3 e 50 caracteres.";
        if(driver.findElement(messageSpam).getText().equals(message)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkMessageTitleErrorAndUrlError() {
        String message = "O título deve ter entre 3 e 50 caracteres.\n Por favor, insira uma URL válida.";
        if(driver.findElement(messageSpam).getText().equals(message)) {
            return true;
        } else {
            return false;
        }
    }

}
