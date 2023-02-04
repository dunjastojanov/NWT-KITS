package book_ride;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Objects;

public class ConfirmPage {
    private final WebDriver driver;

    @FindBy(css = "button#confirm-ride-button")
    WebElement confirmButton;
    @FindBy(css = "p#split-fair-info")
    WebElement splitFairInfo;
    @FindBy(css = "p#vehicle-info")
    WebElement vehicleInfo;
    @FindBy(css = "p#time-info")
    WebElement timeInfo;

    @FindBy(css = "div.toast-message")
    private WebElement toaster;


    public ConfirmPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickConfirmNoWait() {
        click(confirmButton);
    }

    public void clickConfirm() {
        click(confirmButton);
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return Objects.equals(toaster.getText(), "Ride successfully booked. Waiting for available driver.");
            }
        });

    }

    private void click(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(element));
        element.click();
    }

    private String getText(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }

    public String getTime() {
        return getText(timeInfo);
    }

    public String getSplitFair() {
        return getText(splitFairInfo);
    }

    public String getVehicle() {
        return getText(vehicleInfo);
    }
}
