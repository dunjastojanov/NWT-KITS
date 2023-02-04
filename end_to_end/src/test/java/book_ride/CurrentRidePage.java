package book_ride;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CurrentRidePage {

    private final WebDriver driver;

    @FindBy(css="p#driver-name-field")
    WebElement driverName;

    @FindBy(css = "button#end-ride-button")
    private WebElement endButton;
    @FindBy(css = "button#start-ride-button")
    private WebElement startButton;

    @FindBy(css="h1.title")
    private WebElement title;

    public CurrentRidePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getDriverName() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(driverName));
        PageFactory.initElements(driver, this);
        return driverName.getText();
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

    public String getTitle() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(title));
        return title.getText();
    }
}
