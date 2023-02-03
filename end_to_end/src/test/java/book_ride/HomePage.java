package book_ride;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;
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
    @FindBy(css = "div.toast-message")
    private List<WebElement> toasts;

    @FindBy(css = "div.toast-message")
    private WebElement toast;

    @FindBy(css = "p#notifications-link")
    private WebElement notificationsButton;
    @FindBy(css = "p#book-ride-link")
    private WebElement bookRideButton;
    @FindBy(css = "p#current-link")
    private WebElement currentRideButton;

    @FindBy(css = "a#home-link")
    private WebElement homeButton;


    @FindBy(css = "h3#notification_title")
    private WebElement notificationTitle;
    @FindBy(css="#no-notifications")
    private WebElement noNotifications;

    @FindBy(css="button#accept-button")
    WebElement acceptButton;

    @FindBy(css="button#deny-button")
    WebElement denyButton;
    @FindBy(css=".notification")
    private List<WebElement> notifications;
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

    public void answerRequest(boolean accept) {
        if (accept) {
            click(acceptButton);
        }
        else {
            click(denyButton);
        }
    }

    public void clickHome() {
        click(homeButton);
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

    public void clickCurrentRide() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(currentRideButton));

        currentRideButton.click();
    }

    private void click(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(element));
        element.click();
    }


    public void clickNotifications() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(notificationsButton));
        notificationsButton.click();
    }

    public void clickBookRide() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(bookRideButton));
        bookRideButton.click();
    }

    public void openNotificationModal() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        for (WebElement element: notifications) {
            element.click();
        }
    }

    public boolean hasToastMessage(String toastMessage) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        boolean hasMessage = false;

        for (WebElement toast: toasts) {
            wait.until(ExpectedConditions.visibilityOf(toast));
            hasMessage = hasMessage || Objects.equals(toast.getText(), toastMessage);
            System.out.println(toast.getText());
        }

        return hasMessage;
    }

    public boolean hasToastMessage(String toastMessage, String falseMessage) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        boolean hasMessage = false;

        for (WebElement toast: toasts) {
            wait.until(ExpectedConditions.visibilityOf(toast));
            hasMessage = hasMessage || Objects.equals(toast.getText(), toastMessage);
            System.out.println(toast.getText());
        }

        return hasMessage;
    }


    public void login(String email, String password) {
        openLoginModal();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        typeEmail(email);
        typePassword(password);
        clickLogin(List.of("email", "password"));

        wait.until(new ExpectedCondition<Object>() {
            public Object apply(@NullableDecl WebDriver driver) {
                return hasToastMessage("Login successful!");
            }
        });
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

                return !Objects.equals(toast.getText(), "Login successful!") && !Objects.equals(toast.getText(), "");
            }
        });

        return toast.getText();
    }




    public boolean hasNoNotification() {
        return noNotifications.isDisplayed();
    }
}
