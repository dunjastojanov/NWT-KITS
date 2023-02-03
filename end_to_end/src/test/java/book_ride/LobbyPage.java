package book_ride;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class LobbyPage {
    private final WebDriver driver;

    public LobbyPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
}
