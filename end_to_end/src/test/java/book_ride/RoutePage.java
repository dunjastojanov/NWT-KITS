package book_ride;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RoutePage {

    private final WebDriver driver;

    @FindBy(css = "input#destination-0")
    WebElement destinationInput0;
    @FindBy(css = "input#destination-1")
    WebElement destinationInput1;
    @FindBy(css = "input#destination-2")
    WebElement destinationInput2;
    @FindBy(css = "input#destination-3")
    WebElement destinationInput3;
    @FindBy(css = "div#add-destination")
    WebElement addDestination;
    @FindBy(css="button#show-button")
    WebElement showButton;

    @FindBy(css="p#distance")
    WebElement distance;

    public RoutePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickShow() {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until(ExpectedConditions.visibilityOf(showButton));
        showButton.click();
        showButton.click();
        wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOf(distance));

    }

    public void fillDestination(int i, String destination) {
        if (i == 0) {
            fillDestination(destinationInput0,  destination);
        } else if (i == 1) {
            fillDestination(destinationInput1, destination);
        } else if (i == 2) {
            clickAdd();
            fillDestination(destinationInput2,  destination);
        } else if (i == 3) {
            clickAdd();
            fillDestination(destinationInput3, destination);
        }
    }

    public void clickAdd() {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until(ExpectedConditions.visibilityOf(addDestination));
        addDestination.click();
    }

    private void fillDestination(WebElement element,  String destination) {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(destination);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return element.getAttribute("value").length() > 0;
            }
        });
    }

}
