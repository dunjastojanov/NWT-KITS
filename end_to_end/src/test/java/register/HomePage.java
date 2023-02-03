package register;

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

    @FindBy(css = "div.toast-message")
    private WebElement toaster;

    @FindBy(css="button#create-account")
    private WebElement registerButton;
    @FindBy(css="input#email-input")
    private WebElement email;
    @FindBy(css="input#first-name-input")
    private WebElement firstName;
    @FindBy(css="input#last-name-input")
    private WebElement lastName;
    @FindBy(css="input#password-input")
    private WebElement password;
    @FindBy(css="input#confirm-password-input")
    private WebElement confirmPassword;
    @FindBy(css="input#city-input")
    private WebElement city;
    @FindBy(css="input#phone-input")
    private WebElement phone;

    @FindBy(css="button#continue-button")
    private WebElement next;

    @FindBy(css="button#sing-up-button")
    private WebElement submit;


    public HomePage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);
        PageFactory.initElements(driver, this);
    }

    public void openLoginModal() {
        loginModalButton.click();
        PageFactory.initElements(driver, this);
    }

    public String getToastMessage() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(toaster));
        PageFactory.initElements(driver, this);
        return toaster.getText();
    }

    public void clickRegister() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(registerButton));
        registerButton.click();
    }
    public void clickNext() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(next));
        next.click();
    }
    public void clickSubmit() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(submit));
        submit.click();
    }
    public void fillEmail(String text) {
        fillInput(email, text);
    }
    public void fillPassword(String text) {
        fillInput(password, text);
    }
    public void fillConfirmPassword(String text) {
        fillInput(confirmPassword, text);
    }
    public void fillFirstName(String text) {
        fillInput(firstName, text);
    }

    public void fillLastName(String text) {
        fillInput(lastName, text);
    }

    public void fillCity(String text) {
        fillInput(city, text);
    }

    public void fillPhone(String text) {
        fillInput(phone, text);
    }

    private void fillInput(WebElement element, String text) {
        if (!Objects.equals(text, null)) {
            element.sendKeys(text);
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return element.getAttribute("value").length() > 0;
                }
            });
        }

    }

}
