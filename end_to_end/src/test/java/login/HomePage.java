package login;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.*;

public class HomePage {
    private final WebDriver driver;
    private static String PAGE_URL = "http://localhost:4200/";

    @FindBy(css = "button#login-button")
    private WebElement loginModalButton;

    @FindBy(css = "input#email-input")
    private WebElement emailInput;
    @FindBy(css = "input#password-input")
    private WebElement passwordInput;
    @FindBy(css = "button#log-in")
    private WebElement loginButton;
    @FindBy(css = "div.toast-message")
    private WebElement toaster;
    @FindBy(css = "p#profile-link")
    private WebElement profileButton;
    @FindBy(css = "p#admin-link")
    private WebElement adminButton;
    @FindBy(css = "p#book-a-ride-link")
    private WebElement bookButton;

    @FindBy(css = "input#status-toggle")
    private WebElement statusToggle;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);
        PageFactory.initElements(driver, this);
    }

    public void openLoginModal() {
        loginModalButton.click();
        PageFactory.initElements(driver, this);
    }

    public void typeEmail(String email) {
        emailInput.sendKeys(email);

    }

    public void typePassword(String password) {
        passwordInput.sendKeys(password);

    }

    public void clickLogin(List<String> waitFor) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {

                Boolean done = true;
                for (String element: waitFor) {
                    if (Objects.equals(element, "email"))
                        done = done && emailInput.getAttribute("value").length() > 0;
                    if (Objects.equals(element, "password"))
                        done = done && passwordInput.getAttribute("value").length() > 0;
                }
                return done;
            }
        });
        loginButton.click();


    }

    public String getToastMessage() {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until(ExpectedConditions.visibilityOf(toaster));

        PageFactory.initElements(driver, this);

        return toaster.getText();
    }

    public boolean isProfilePresent() {
        return profileButton.isDisplayed();
    }

    public boolean isAdminPanelPresent() {
        return adminButton.isDisplayed();
    }

    public boolean isBookRidePresent() {
        return bookButton.isDisplayed();
    }
    public boolean isStatusTogglePresent() {
        return statusToggle.isDisplayed();
    }

    public void clickProfile() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(profileButton));

        profileButton.click();
    }
    public void clickAdmin() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(adminButton));

        adminButton.click();
    }



}
