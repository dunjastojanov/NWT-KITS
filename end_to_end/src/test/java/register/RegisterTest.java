package register;

import helper.Helper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class RegisterTest {

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
    @DisplayName("Registration successful")
    public void registerSuccessful() {
        HomePage homePage = new HomePage(driver);
        homePage.openLoginModal();
        homePage.clickRegister();

        homePage.fillEmail("anastasija@gmail.com");
        homePage.fillPassword("Anastasija123");
        homePage.fillConfirmPassword("Anastasija123");

        Helper.takeScreenshot(driver, "register_successful_first_page");

        homePage.clickNext();

        homePage.fillFirstName("Anastasija");
        homePage.fillLastName("Savic");
        homePage.fillCity("Novi Sad");
        homePage.fillPhone("0697894125");

        Helper.takeScreenshot(driver, "register_successful_second_page");

        homePage.clickSubmit();

        String message = homePage.getToastMessage();

        Helper.takeScreenshot(driver, "register_successful_message");

        Assertions.assertEquals("Registration successful!", message);
    }

    @ParameterizedTest(name="Registration unsuccessful with error: {7}")
    @CsvFileSource(files={"src/test/resources/csv/registration.csv"}, numLinesToSkip = 1)
    public void registerUnsuccessful(String email, String password, String confirm_password, String first_name,String last_name, String city, String phone, String error) {
        HomePage homePage = new HomePage(driver);
        homePage.openLoginModal();
        homePage.clickRegister();

        homePage.fillEmail(email);
        homePage.fillPassword(password);
        homePage.fillConfirmPassword(confirm_password);

        Helper.takeScreenshot(driver, "register_unsuccessful_first_page_" + error.toLowerCase().replace(" ", "_"));

        homePage.clickNext();

        homePage.fillFirstName(first_name);
        homePage.fillLastName(last_name);
        homePage.fillCity(city);
        homePage.fillPhone(phone);

        Helper.takeScreenshot(driver, "register_unsuccessful_second_page_" + error.toLowerCase().replace(" ", "_"));

        homePage.clickSubmit();

        String message = homePage.getToastMessage();

        Helper.takeScreenshot(driver, "register_unsuccessful_message_" + error.toLowerCase().replace(" ", "_"));

        Assertions.assertEquals(error, message);
    }

    @ParameterizedTest(name="Registration unsuccessful because of empty parameters with error: {7}")
    @CsvFileSource(files={"src/test/resources/csv/registration_empty.csv"}, numLinesToSkip = 1)
    public void registerUnsuccessfulEmptyParameters(String email, String password, String confirm_password, String first_name,String last_name, String city, String phone, String error) {
        HomePage homePage = new HomePage(driver);
        homePage.openLoginModal();
        homePage.clickRegister();

        homePage.fillEmail(email);
        homePage.fillPassword(password);
        homePage.fillConfirmPassword(confirm_password);

        Helper.takeScreenshot(driver, "register_unsuccessful_empty_first_page_" + error.toLowerCase().replace(" ", "_"));

        homePage.clickNext();

        homePage.fillFirstName(first_name);
        homePage.fillLastName(last_name);
        homePage.fillCity(city);
        homePage.fillPhone(phone);

        Helper.takeScreenshot(driver, "register_unsuccessful_empty_second_page_" + error.toLowerCase().replace(" ", "_"));

        homePage.clickSubmit();

        String message = homePage.getToastMessage();

        Helper.takeScreenshot(driver, "register_unsuccessful_empty_message_" + error.toLowerCase().replace(" ", "_"));

        Assertions.assertEquals(error, message);
    }

}
