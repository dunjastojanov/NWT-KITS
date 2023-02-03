package book_ride;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;



import java.util.*;

public class InfoPage {
    private final WebDriver driver;

    @FindBy(css = "input#current-time-check")
    WebElement forNow;

    @FindBy(css = "input#split-fair-check")
    WebElement splitFair;

    @FindBy(css = "input#time-input")
    WebElement time;

    @FindBy(css = "input#pal-input")
    WebElement palInput;

    @FindBy(css = "button#pal-button")
    WebElement palButton;

    @FindBy(css = "div#kid_friendly")
    WebElement kidFriendly;

    @FindBy(css = "div#pet_friendly")
    WebElement petFriendly;

    Select vehicleSelect;

    @FindBy(css = "div.toast-message")
    private WebElement toaster;


    public InfoPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getToastMessage() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(toaster));
        PageFactory.initElements(driver, this);
        return toaster.getText();
    }

    public void fillPals(List<String> emails) {
        for (String email: emails) {
            fillPal(email);
        }
    }

    public void fillPal(String email) {
        fill(this.palInput, email);
        click(palButton);
    }


    public void selectOptions(boolean kidFriendly, boolean petFriendly, String vehicleType) {
        if (kidFriendly) {
            click(this.kidFriendly);
        }
        if (petFriendly) {
            click(this.petFriendly);
        }

        vehicleSelect = new Select(driver.findElement(By.cssSelector("select#vehicle-select")));
        vehicleSelect.selectByVisibleText(vehicleType);

    }

    private void click(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(element));
        element.click();
    }

    private void fill(WebElement element, String text) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(text);
    }

    public void fillCheckboxes(boolean forNow, boolean splitFair) {
        if (forNow) {
            click(this.forNow);
        }
        if (splitFair) {
            click(this.splitFair);
        }
    }

}
