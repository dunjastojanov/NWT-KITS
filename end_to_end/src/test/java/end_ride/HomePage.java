package end_ride;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Objects;

public class HomePage {
    private final WebDriver driver;
    private static String PAGE_URL = "http://localhost:4200/";

    @FindBy(css = "button#login-button")
    private WebElement loginModalButton;

    @FindBy(css = "button#logout-button")
    private WebElement logoutButton;

    @FindBy(css = "input#email-input")
    private WebElement emailInput;
    @FindBy(css = "input#password-input")
    private WebElement passwordInput;
    @FindBy(css = "button#log-in")
    private WebElement loginButton;
    @FindBy(css = "div.toast-message:last-child")
    private WebElement toaster;

    @FindBy(css = "p#notifications-link")
    private WebElement notificationsButton;
    @FindBy(css = "p#current-link")
    private WebElement currentRideButton;

    @FindBy(css = "div#ride_notification_5")
    private WebElement notification5;

    @FindBy(css = "div#ride_notification_4")
    private WebElement notification4;

    @FindBy(css = "#ride_notification_9")
    private WebElement notification9;
    @FindBy(css = "h3#notification_title")
    private WebElement notificationTitle;
    @FindBy(css="#no-notifications")
    private WebElement noNotifications;
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
        System.out.println("Click");
        loginButton.click();
    }

    public void clickCurrentRide() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(currentRideButton));

        currentRideButton.click();
    }

    public void clickNotifications() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(notificationsButton));
        notificationsButton.click();
    }

    public void openNotificationModal(int rideId) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        if (rideId == 5) {
            wait.until(ExpectedConditions.visibilityOf(notification5));
            notification5.click();
        }
        if (rideId == 4) {
            wait.until(ExpectedConditions.visibilityOf(notification4));
            notification4.click();
        }
        if (rideId == 9) {
            wait.until(ExpectedConditions.visibilityOf(notification9));
            notification9.click();
        }
    }

    public String getToastMessage() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(toaster));
        PageFactory.initElements(driver, this);
        return toaster.getText();
    }


    public void login(String email, String password) {
        openLoginModal();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        typeEmail(email);
        typePassword(password);
        clickLogin(List.of("email", "password"));
        wait.until(ExpectedConditions.visibilityOf(toaster));
    }

    public void clickLogout() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(logoutButton));
        logoutButton.click();
    }

    public String getNotificationTitle() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(notificationTitle));
        return notificationTitle.getText();
    }

    public String getErrorMessage() {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {

                return !Objects.equals(toaster.getText(), "Login successful!") && !Objects.equals(toaster.getText(), "");
            }
        });

        return toaster.getText();
    }

    public boolean hasNoNotification() {
        return noNotifications.isDisplayed();
    }
}
