package end_ride;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CurrentRidePage {
    private final WebDriver driver;
    @FindBy(css = "button#end-ride-button")
    private WebElement endButton;
    @FindBy(css = "button#start-ride-button")
    private WebElement startButton;

    @FindBy(css = "button#icon-button")
    private WebElement cancelRideModalButton;

    @FindBy(css = "button#cancel-ride-button")
    private WebElement cancelRideButton;

    @FindBy(css = "input#message-input")
    private WebElement messageInput;

    @FindBy(css="button#close-cancel-ride-modal")
    private WebElement closeModalButton;

    public CurrentRidePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickEnd() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(endButton));
        endButton.click();
    }

    public void clickStart() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(startButton));
        startButton.click();
    }

    public void openCancelModal() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(cancelRideModalButton));
        cancelRideModalButton.click();
    }

    public void clickCancel() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(cancelRideButton));
        cancelRideButton.click();
    }

    public void closeCancelModal() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(closeModalButton));
        closeModalButton.click();
    }


    public void fillReason() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(messageInput));
        messageInput.sendKeys("I have not been feeling really well.");
    }
}
