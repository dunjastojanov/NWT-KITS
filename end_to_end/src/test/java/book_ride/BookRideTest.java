package book_ride;

import helper.Helper;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookRideTest {

    WebDriver driver;

    CurrentRidePage currentRidePage;
    HomePage homePage;


    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:\\EdgeDriver\\msedgedriver.exe");

        WebDriver otherDriver = new EdgeDriver();
        HomePage homePage = new HomePage(otherDriver);
        homePage.login("bogoljub@gmail.com", "Bogoljub123");
        otherDriver.close();


        driver = new EdgeDriver();
        driver.manage().window().maximize();

        this.homePage = new HomePage(driver);
        this.currentRidePage = new CurrentRidePage(driver);
    }


    @AfterEach
    public void tearDown() {
       driver.close();
    }

    @Test
    @Order(10)
    public void bookRideSuccessful() {
        String function_name = "book_ride_successful";
        homePage.login("goran@gmail.com", "Goran123");
        homePage.clickBookRide();

        BookRidePage bookRidePage = new BookRidePage(driver);
        bookRidePage.clickRoute();

        RoutePage routePage = new RoutePage(driver);
        routePage.fillDestination(0, "Bulevar Evrope 27 Novi Sad");
        routePage.fillDestination(1, "Narodnog Fronta 34 Novi Sad");
        routePage.clickShow();
        Helper.takeScreenshot(driver, function_name + "_route");

        bookRidePage.clickInfo();

        InfoPage infoPage = new InfoPage(driver);
        infoPage.selectOptions(true, false, "Caravan");
        infoPage.fillCheckboxes(true, false);
        Helper.takeScreenshot(driver, function_name + "_info");

        bookRidePage.clickConfirm();
        ConfirmPage confirmPage = new ConfirmPage(driver);
        confirmPage.clickConfirm();
        Helper.takeScreenshot(driver, function_name + "_confirm");

        homePage.clickLogout();


        answerRequest("bogoljub@gmail.com", "Bogoljub123", true, function_name);
        assertDriver( "goran@gmail.com", "Goran123", "Bogoljub Milicevic", function_name);

        endRide("bogoljub@gmail.com", "Bogoljub123");
    }

    @Test
    @Order(11)
    public void bookRideSuccessfulWithPals() {
        String function_name = "book_ride_successful_with_pals";

        homePage.login("danica@gmail.com", "Danica123");
        homePage.clickBookRide();

        BookRidePage bookRidePage = new BookRidePage(driver);
        bookRidePage.clickRoute();

        RoutePage routePage = new RoutePage(driver);
        routePage.fillDestination(0, "Bulevar Evrope 27 Novi Sad");
        routePage.fillDestination(1, "Narodnog Fronta 34 Novi Sad");
        routePage.clickShow();
        Helper.takeScreenshot(driver, function_name + "_route");


        bookRidePage.clickInfo();

        InfoPage infoPage = new InfoPage(driver);
        infoPage.selectOptions(true, false, "Caravan");
        infoPage.fillCheckboxes(true, false);
        infoPage.fillPal("mileta@gmail.com");
        Helper.takeScreenshot(driver, function_name + "_info");


        bookRidePage.clickConfirm();
        ConfirmPage confirmPage = new ConfirmPage(driver);
        confirmPage.clickConfirm();
        Helper.takeScreenshot(driver, function_name + "_confirm");
        homePage.clickLogout();

        answerRequest("mileta@gmail.com", "Mileta123", true, function_name + "_pal");
        answerRequest( "bogoljub@gmail.com", "Bogoljub123", true, function_name + "_driver");
        assertDriver("danica@gmail.com", "Danica123", "Bogoljub Milicevic", function_name);
        endRide("bogoljub@gmail.com", "Bogoljub123");
    }

    @Test
    @Order(9)
    public void bookRideDriverDenys() {
        String function_name = "book_ride_driver_denys";
        homePage.login("goran@gmail.com", "Goran123");
        homePage.clickBookRide();

        BookRidePage bookRidePage = new BookRidePage(driver);
        bookRidePage.clickRoute();

        RoutePage routePage = new RoutePage(driver);
        routePage.fillDestination(0, "Bulevar Evrope 27 Novi Sad");
        routePage.fillDestination(1, "Narodnog Fronta 34 Novi Sad");
        routePage.clickShow();
        Helper.takeScreenshot(driver, function_name + "_route");


        bookRidePage.clickInfo();

        InfoPage infoPage = new InfoPage(driver);
        infoPage.selectOptions(true, false, "Caravan");
        infoPage.fillCheckboxes(true, false);
        Helper.takeScreenshot(driver, function_name + "_info");

        bookRidePage.clickConfirm();
        ConfirmPage confirmPage = new ConfirmPage(driver);
        confirmPage.clickConfirm();
        Helper.takeScreenshot(driver, function_name + "_confirm");
        homePage.clickLogout();

        answerRequest("bogoljub@gmail.com", "Bogoljub123", false, function_name);

        homePage.login("goran@gmail.com", "Goran123");
        homePage.clickCurrentRide();

        String title = currentRidePage.getTitle();
        Helper.takeScreenshot(driver, function_name + "_current_ride");
        Assertions.assertEquals(title, "No current ride available");

    }

    @Test
    @Order(12)
    public void bookRidePalDenys() {
        String function_name = "book_ride_pal_denys";
        homePage.login("tijana@gmail.com", "Tijana123");
        homePage.clickBookRide();

        BookRidePage bookRidePage = new BookRidePage(driver);
        bookRidePage.clickRoute();

        RoutePage routePage = new RoutePage(driver);
        routePage.fillDestination(0, "Bulevar Evrope 27 Novi Sad");
        routePage.fillDestination(1, "Narodnog Fronta 34 Novi Sad");
        routePage.clickShow();
        Helper.takeScreenshot(driver, function_name + "_route");

        bookRidePage.clickInfo();

        InfoPage infoPage = new InfoPage(driver);
        infoPage.selectOptions(true, false, "Caravan");
        infoPage.fillCheckboxes(true, false);
        infoPage.fillPal("natalija@gmail.com");
        Helper.takeScreenshot(driver, function_name + "_info");

        bookRidePage.clickConfirm();
        ConfirmPage confirmPage = new ConfirmPage(driver);
        confirmPage.clickConfirm();
        Helper.takeScreenshot(driver, function_name + "_confirm");
        homePage.clickLogout();

        answerRequest("natalija@gmail.com", "Natalija123", false, function_name);

        homePage.login("tijana@gmail.com", "Tijana123");
        homePage.clickCurrentRide();
        Helper.takeScreenshot(driver, function_name + "_current_ride");

        String title = currentRidePage.getTitle();
        Assertions.assertEquals(title, "No current ride available");

    }
    @Test
    @Order(1)
    public void bookRideTimeNotFilled() {
        String function_name = "book_ride_time_not_filled";
        homePage.login("danica@gmail.com", "Danica123");
        homePage.clickBookRide();

        BookRidePage bookRidePage = new BookRidePage(driver);
        bookRidePage.clickRoute();

        RoutePage routePage = new RoutePage(driver);
        routePage.fillDestination(0, "Bulevar Evrope 27 Novi Sad");
        routePage.fillDestination(1, "Narodnog Fronta 34 Novi Sad");
        routePage.clickShow();
        Helper.takeScreenshot(driver, function_name + "_confirm");


        bookRidePage.clickInfo();

        InfoPage infoPage = new InfoPage(driver);
        infoPage.selectOptions(true, false, "Caravan");
        infoPage.fillCheckboxes(false, false);
        Helper.takeScreenshot(driver, function_name + "_info");


        bookRidePage.clickConfirm();
        ConfirmPage confirmPage = new ConfirmPage(driver);
        confirmPage.clickConfirmNoWait();
        Helper.takeScreenshot(driver, function_name + "_confirm");

        Assertions.assertTrue(homePage.hasToastMessage("Please fill all fields."));

    }

    @Test
    @Order(2)
    public void bookRideVehicleNotFilled() {
        String function_name = "book_ride_vehicle_not_filled";

        homePage.login("danica@gmail.com", "Danica123");
        homePage.clickBookRide();

        BookRidePage bookRidePage = new BookRidePage(driver);
        bookRidePage.clickRoute();

        RoutePage routePage = new RoutePage(driver);
        routePage.fillDestination(0, "Bulevar Evrope 27 Novi Sad");
        routePage.fillDestination(1, "Narodnog Fronta 34 Novi Sad");
        routePage.clickShow();
        Helper.takeScreenshot(driver, function_name + "_route");


        bookRidePage.clickInfo();

        InfoPage infoPage = new InfoPage(driver);
        infoPage.fillCheckboxes(true, false);

        Helper.takeScreenshot(driver, function_name + "_info");
        bookRidePage.clickConfirm();

        ConfirmPage confirmPage = new ConfirmPage(driver);

        confirmPage.clickConfirmNoWait();
        Helper.takeScreenshot(driver, function_name + "_confirm");

        Assertions.assertTrue(homePage.hasToastMessage("Please fill all fields."));

    }
    @Test
    @Order(3)
    public void bookRideRouteNotFilled() {
        String function_name = "book_ride_route_not_filled";
        homePage.login("danica@gmail.com", "Danica123");
        homePage.clickBookRide();

        BookRidePage bookRidePage = new BookRidePage(driver);
        bookRidePage.clickRoute();
        bookRidePage.clickInfo();


        InfoPage infoPage = new InfoPage(driver);
        infoPage.selectOptions(true, false, "Caravan");
        infoPage.fillCheckboxes(true, false);
        Helper.takeScreenshot(driver, function_name + "_info");

        bookRidePage.clickConfirm();
        ConfirmPage confirmPage = new ConfirmPage(driver);
        confirmPage.clickConfirmNoWait();
        Helper.takeScreenshot(driver, function_name + "_confirm");

        Assertions.assertTrue(homePage.hasToastMessage("Please fill all fields."));

    }

    @Test
    @Order(4)
    public void bookRideNoSuchVehicle() {
        String function_name="book_ride_no_such_vehicle";
        homePage.login("danica@gmail.com", "Danica123");
        homePage.clickBookRide();

        BookRidePage bookRidePage = new BookRidePage(driver);
        bookRidePage.clickRoute();

        RoutePage routePage = new RoutePage(driver);
        routePage.fillDestination(0, "Bulevar Evrope 27 Novi Sad");
        routePage.fillDestination(1, "Narodnog Fronta 34 Novi Sad");
        routePage.clickShow();
        Helper.takeScreenshot(driver, function_name + "_route");

        bookRidePage.clickInfo();

        InfoPage infoPage = new InfoPage(driver);
        infoPage.selectOptions(true, false, "Limousine");
        infoPage.fillCheckboxes(true, false);
        Helper.takeScreenshot(driver, function_name + "_info");

        bookRidePage.clickConfirm();
        ConfirmPage confirmPage = new ConfirmPage(driver);
        confirmPage.clickConfirmNoWait();
        Helper.takeScreenshot(driver, function_name + "_confirm");


        String message = homePage.getErrorMessage();
        Assertions.assertEquals("There is no driver available.", message);

    }

    @Test
    @Order(5)
    public void bookRideNoActiveDrivers() {
        String function_name="book_ride_no_active_drivers";
        homePage.login( "bogoljub@gmail.com", "Bogoljub123");
        homePage.clickLogout();

        homePage.login( "dragan@gmail.com", "Dragan123");
        homePage.clickLogout();

        homePage.login("danica@gmail.com", "Danica123");
        homePage.clickBookRide();

        BookRidePage bookRidePage = new BookRidePage(driver);
        bookRidePage.clickRoute();

        RoutePage routePage = new RoutePage(driver);
        routePage.fillDestination(0, "Bulevar Evrope 27 Novi Sad");
        routePage.fillDestination(1, "Narodnog Fronta 34 Novi Sad");
        routePage.clickShow();
        Helper.takeScreenshot(driver, function_name + "_route");

        bookRidePage.clickInfo();

        InfoPage infoPage = new InfoPage(driver);
        infoPage.selectOptions(true, false, "Limousine");
        infoPage.fillCheckboxes(true, false);
        Helper.takeScreenshot(driver, function_name + "_info");

        bookRidePage.clickConfirm();
        ConfirmPage confirmPage = new ConfirmPage(driver);
        confirmPage.clickConfirmNoWait();
        Helper.takeScreenshot(driver, function_name + "_confirm");

        String message = homePage.getErrorMessage();
        Assertions.assertEquals("There is no driver available.", message);

    }

    @Test
    @Order(6)
    public void bookRideInsufficientFundsBooker() {
        String function_name="book_ride_insufficient_funds_booker";
        homePage.login("miljan@gmail.com", "Miljan123");
        homePage.clickBookRide();

        BookRidePage bookRidePage = new BookRidePage(driver);
        bookRidePage.clickRoute();

        RoutePage routePage = new RoutePage(driver);
        routePage.fillDestination(0, "Bulevar Evrope 27 Novi Sad");
        routePage.fillDestination(1, "Narodnog Fronta 34 Novi Sad");
        routePage.clickShow();
        Helper.takeScreenshot(driver, function_name + "_route");


        bookRidePage.clickInfo();

        InfoPage infoPage = new InfoPage(driver);
        infoPage.selectOptions(true, false, "Caravan");
        infoPage.fillCheckboxes(true, false);
        Helper.takeScreenshot(driver, function_name + "_info");


        bookRidePage.clickConfirm();
        ConfirmPage confirmPage = new ConfirmPage(driver);
        confirmPage.clickConfirmNoWait();
        Helper.takeScreenshot(driver, function_name + "_confirm");

        String message = homePage.getErrorMessage();

        Assertions.assertEquals("Passengers do not have enough funds in their account.", message);

    }

    @Test
    @Order(7)
    public void bookRideInsufficientRidingPal() {
        String function_name="book_ride_insufficient_riding_pal";
        homePage.login("danica@gmail.com", "Danica123");
        homePage.clickBookRide();

        BookRidePage bookRidePage = new BookRidePage(driver);
        bookRidePage.clickRoute();

        RoutePage routePage = new RoutePage(driver);
        routePage.fillDestination(0, "Bulevar Evrope 27 Novi Sad");
        routePage.fillDestination(1, "Narodnog Fronta 34 Novi Sad");
        routePage.clickShow();
        Helper.takeScreenshot(driver, function_name + "_route");

        bookRidePage.clickInfo();

        InfoPage infoPage = new InfoPage(driver);
        infoPage.selectOptions(true, false, "Caravan");
        infoPage.fillCheckboxes(true, true);
        infoPage.fillPal("miljan@gmail.com");
        Helper.takeScreenshot(driver, function_name + "_info");


        bookRidePage.clickConfirm();
        ConfirmPage confirmPage = new ConfirmPage(driver);
        confirmPage.clickConfirmNoWait();
        Helper.takeScreenshot(driver, function_name + "_confirm");

        String message = homePage.getErrorMessage();

        Assertions.assertEquals("Passengers do not have enough funds in their account.", message);

    }

    @Test
    @Order(8)
    public void bookRidePalRasARide() {
        String function_name="book_ride_pal_ras_a_ride";
        homePage.login("danica@gmail.com", "Danica123");
        homePage.clickBookRide();

        BookRidePage bookRidePage = new BookRidePage(driver);
        bookRidePage.clickRoute();

        RoutePage routePage = new RoutePage(driver);
        routePage.fillDestination(0, "Bulevar Evrope 27 Novi Sad");
        routePage.fillDestination(1, "Narodnog Fronta 34 Novi Sad");
        routePage.clickShow();
        Helper.takeScreenshot(driver, function_name + "_route");


        bookRidePage.clickInfo();

        InfoPage infoPage = new InfoPage(driver);
        infoPage.selectOptions(true, false, "Caravan");
        infoPage.fillCheckboxes(true, true);
        infoPage.fillPal("marica@gmail.com");

        Helper.takeScreenshot(driver, function_name + "_info");

        String message = homePage.getErrorMessage();
        Assertions.assertEquals("User marica@gmail.com already has scheduled ride.", message);

    }

    private void assertDriver( String email, String password, String expectedDriver, String testName) {
        homePage.login(email, password);
        homePage.clickCurrentRide();
        String driver = currentRidePage.getDriverName();
        Assertions.assertEquals(expectedDriver, driver);
        Helper.takeScreenshot(this.driver, testName + "_driver");
        homePage.clickLogout();
    }

    private void answerRequest(String email, String password, boolean accept, String testName) {
        homePage.login(email, password);
        homePage.clickNotifications();
        homePage.openNotificationModal();
        Helper.takeScreenshot(driver, testName + "_answer_request");
        homePage.answerRequest(accept);
        homePage.clickLogout();
    }


    private void endRide(String email, String password) {
        homePage.login(email, password);
        homePage.clickCurrentRide();

        currentRidePage.clickStart();
        currentRidePage.clickEnd();
    }


}
