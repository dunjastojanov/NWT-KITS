package login;

import helper.Helper;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.ArrayList;
import java.util.List;

public class LoginTest {

    WebDriver driver;
    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:\\EdgeDriver\\msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
    }
    @AfterEach
    public void tearDown() {
        driver.close();
    }
    @Test
    public void loginClientSuccessful() {
        HomePage homePage = new HomePage(driver);
        homePage.openLoginModal();

        homePage.typeEmail("slavica@gmail.com");
        homePage.typePassword("Slavica123");

        homePage.clickLogin(List.of("email", "password"));

        String message = homePage.getToastMessage();

        Assertions.assertEquals("Login successful!", message);
        Assertions.assertTrue(homePage.isProfilePresent());
        Assertions.assertTrue(homePage.isBookRidePresent());

        Helper.takeScreenshot(driver, "login_client_successful_homepage");

        homePage.clickProfile();
        ProfilePage profilePage = new ProfilePage(driver);

        String firstName = profilePage.getFirstName();
        Assertions.assertEquals("Slavica", firstName);

        Helper.takeScreenshot(driver, "login_client_successful_profile");
    }

    @Test
    public void loginDriverSuccessful() {
        HomePage homePage = new HomePage(driver);
        homePage.openLoginModal();

        homePage.typeEmail("dragan@gmail.com");
        homePage.typePassword("Dragan123");

        homePage.clickLogin(List.of("email", "password"));

        String message = homePage.getToastMessage();

        Assertions.assertEquals("Login successful!", message);
        Assertions.assertTrue(homePage.isProfilePresent());
        Assertions.assertTrue(homePage.isStatusTogglePresent());

        Helper.takeScreenshot(driver, "login_driver_successful_homepage");

        homePage.clickProfile();
        ProfilePage profilePage = new ProfilePage(driver);

        String firstName = profilePage.getFirstName();
        Assertions.assertEquals("Dragan", firstName);

        Helper.takeScreenshot(driver, "login_driver_successful_profile");
    }

    @Test
    public void loginAdminSuccessful() {
        HomePage homePage = new HomePage(driver);
        homePage.openLoginModal();

        homePage.typeEmail("biljana@gmail.com");
        homePage.typePassword("Biljana123");

        homePage.clickLogin(List.of("email", "password"));

        String message = homePage.getToastMessage();

        Assertions.assertEquals("Login successful!", message);
        Assertions.assertTrue(homePage.isAdminPanelPresent());

        Helper.takeScreenshot(driver, "login_admin_successful_homepage");

        homePage.clickAdmin();
        AdminPage adminPage = new AdminPage(driver);

        String firstName = adminPage.getFirstName();
        Assertions.assertEquals("Biljana", firstName);

        Helper.takeScreenshot(driver, "login_admin_successful_profile");
    }

    @Test
    public void loginEmptyFields() {
        HomePage homePage = new HomePage(driver);
        homePage.openLoginModal();

        homePage.typeEmail("");
        homePage.typePassword("");

        homePage.clickLogin(new ArrayList<>());

        String message = homePage.getToastMessage();
        Assertions.assertEquals("Invalid email or password.", message);

        Helper.takeScreenshot(driver, "login_empty_fields");
    }

    @Test
    public void loginEmptyEmail() {
        HomePage homePage = new HomePage(driver);
        homePage.openLoginModal();

        homePage.typeEmail("");
        homePage.typePassword("Slavica123");

        homePage.clickLogin(List.of("password"));

        String message = homePage.getToastMessage();
        Assertions.assertEquals("Invalid email or password.", message);

        Helper.takeScreenshot(driver, "login_empty_email");
    }

    @Test
    public void loginEmptyPassword() {
        HomePage homePage = new HomePage(driver);
        homePage.openLoginModal();

        homePage.typeEmail("slavica@gmail.com");
        homePage.typePassword("");

        homePage.clickLogin(List.of("email"));

        String message = homePage.getToastMessage();
        Assertions.assertEquals("Invalid email or password.", message);

        Helper.takeScreenshot(driver, "login_empty_password");
    }

    @Test
    public void loginIncorrectPassword() {
        HomePage homePage = new HomePage(driver);
        homePage.openLoginModal();

        homePage.typeEmail("slavica@gmail.com");
        homePage.typePassword("Slavica321");

        homePage.clickLogin(List.of("email", "password"));

        String message = homePage.getToastMessage();
        Assertions.assertEquals("Invalid email or password.", message);

        Helper.takeScreenshot(driver, "login_incorrect_password");
    }

    @Test
    public void loginIncorrectEmail() {
        HomePage homePage = new HomePage(driver);
        homePage.openLoginModal();

        homePage.typeEmail("slavka@gmail.com");
        homePage.typePassword("Slavica123");

        homePage.clickLogin(List.of("email", "password"));

        String message = homePage.getToastMessage();
        Assertions.assertEquals("Invalid email or password.", message);

        Helper.takeScreenshot(driver, "login_incorrect_email");
    }

}
