package end_ride;


import helper.Helper;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EndRideTest {

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
    @Order(4)
    public void endStartedRideSuccessful() {
        HomePage homePage = new HomePage(driver);
        homePage.login("rasim@gmail.com", "Rasim123");
        Helper.takeScreenshot(driver, "end_started_ride_successful_driver_logged_in");
        homePage.clickCurrentRide();
        CurrentRidePage currentRidePage = new CurrentRidePage(driver);
        Helper.takeScreenshot(driver, "end_started_ride_successful_current_ride");
        currentRidePage.clickEnd();
        homePage.clickLogout();
        homePage.login("teodora@gmail.com", "Teodora123");
        Helper.takeScreenshot(driver, "end_started_ride_successful_client_logged_in");

        homePage.clickNotifications();
        Helper.takeScreenshot(driver, "end_started_ride_successful_notifications");

        homePage.openNotificationModal(5);
        String notificationTitle = homePage.getNotificationTitle();
        Helper.takeScreenshot(driver, "end_started_ride_successful_notification");

        Assertions.assertEquals("Ride review", notificationTitle);
    }

    @Test
    @Order(3)
    public void endConfirmedRideSuccessful() {
        HomePage homePage = new HomePage(driver);
        homePage.login("borko@gmail.com", "Borko123");
        Helper.takeScreenshot(driver, "end_ride_confirmed_ride_successful_driver_logged_in");

        homePage.clickCurrentRide();
        Helper.takeScreenshot(driver, "end_ride_confirmed_ride_successful_current_ride_start");

        CurrentRidePage currentRidePage = new CurrentRidePage(driver);

        currentRidePage.clickStart();
        Helper.takeScreenshot(driver, "end_ride_confirmed_ride_successful_current_ride_end");

        currentRidePage.clickEnd();
        homePage.clickLogout();
        homePage.login("marica@gmail.com", "Marica123");
        Helper.takeScreenshot(driver, "end_ride_confirmed_ride_successful_client_logged_in");

        homePage.clickNotifications();
        Helper.takeScreenshot(driver, "end_ride_confirmed_ride_successful_notifications");

        homePage.openNotificationModal(4);
        String notificationTitle = homePage.getNotificationTitle();
        Helper.takeScreenshot(driver, "end_ride_confirmed_ride_successful_notification");

        Assertions.assertEquals("Ride review", notificationTitle);
    }

    @Test
    @Order(2)
    public void cancelConfirmedRideSuccessful() {
        HomePage homePage = new HomePage(driver);
        homePage.login("hristina@gmail.com", "Hristina123");
        Helper.takeScreenshot(driver, "cancel_ride_confirmed_ride_successful_driver_logged_in");

        homePage.clickCurrentRide();
        Helper.takeScreenshot(driver, "cancel_ride_confirmed_ride_successful_current_ride");
        CurrentRidePage currentRidePage = new CurrentRidePage(driver);

        currentRidePage.openCancelModal();
        currentRidePage.fillReason();
        currentRidePage.clickCancel();
        Helper.takeScreenshot(driver, "cancel_ride_confirmed_ride_successful_cancel_modal");

        homePage.clickLogout();

        homePage.login("milivoje@gmail.com", "Milivoje123");
        Helper.takeScreenshot(driver, "cancel_ride_confirmed_ride_successful_client_logged_in");

        homePage.clickNotifications();
        Helper.takeScreenshot(driver, "cancel_ride_confirmed_ride_successful_notifications");

        homePage.openNotificationModal(9);
        String notificationTitle = homePage.getNotificationTitle();
        Helper.takeScreenshot(driver, "cancel_ride_confirmed_ride_successful_notification");

        Assertions.assertEquals("Ride has been canceled", notificationTitle);
    }

    @Test
    @Order(1)
    public void cancelConfirmedRideNoReason() {
        HomePage homePage = new HomePage(driver);
        homePage.login("hristina@gmail.com", "Hristina123");
        Helper.takeScreenshot(driver, "cancel_ride_no_reason_driver_logged_in");
        homePage.clickCurrentRide();
        CurrentRidePage currentRidePage = new CurrentRidePage(driver);
        Helper.takeScreenshot(driver, "cancel_ride_no_reason_current_ride_page");

        currentRidePage.openCancelModal();
        currentRidePage.clickCancel();
        Helper.takeScreenshot(driver, "cancel_ride_no_reason_cancel_modal");

        String message = homePage.getErrorMessage();
        Assertions.assertEquals("Must fill reason.", message);

        currentRidePage.closeCancelModal();

        homePage.clickLogout();

        homePage.login("milivoje@gmail.com", "Milivoje123");
        Helper.takeScreenshot(driver, "cancel_ride_no_reason_client_logged_in");
        homePage.clickNotifications();
        Helper.takeScreenshot(driver, "cancel_ride_no_reason_notifications");
        boolean hasNoNotification = homePage.hasNoNotification();
        Assertions.assertTrue(hasNoNotification);
    }
}
