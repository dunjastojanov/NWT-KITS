package book_ride;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BookRidePage {

    private final WebDriver driver;
    @FindBy(css = "a#route-link")
    WebElement routeLink;
    @FindBy(css = "a#info-link")
    WebElement infoLink;
    @FindBy(css = "a#confirm-link")
    WebElement confirmLink;
    @FindBy(css = "a#loby-link")
    WebElement lobbyLink;

    public BookRidePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickRoute() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(routeLink));
        routeLink.click();
    }

    public void clickInfo() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(infoLink));
        infoLink.click();
    }

    public void clickConfirm() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(confirmLink));
        confirmLink.click();
    }

    public void clickLobby() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(lobbyLink));
        lobbyLink.click();
    }


}
