package no.enterprise.onlinetravelagency.selenium;

import no.enterprise.onlinetravelagency.Application;
import no.enterprise.onlinetravelagency.entity.Trip;
import no.enterprise.onlinetravelagency.selenium.po.DetailsPO;
import no.enterprise.onlinetravelagency.selenium.po.IndexPO;
import no.enterprise.onlinetravelagency.selenium.po.SignUpPO;
import no.enterprise.onlinetravelagency.selenium.po.UserPO;
import no.enterprise.onlinetravelagency.service.TripService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class SeleniumLocalIT {

    private static WebDriver driver;

    @LocalServerPort
    private int port;

    @Autowired
    TripService tripService;

    @BeforeAll
    public static void initClass() {

        driver = SeleniumDriverHandler.getChromeDriver();

        assumeTrue(driver != null, "Cannot find/initialize Chrome driver");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.close();
        }
    }

    protected WebDriver getDriver() {
        return driver;
    }

    protected String getServerHost() {
        return "localhost";
    }

    protected int getServerPort() {
        return port;
    }

    private static final AtomicInteger counter = new AtomicInteger(0);

    private String getUniqueId() {
        return "foo_SeleniumLocalIT_" + counter.getAndIncrement();
    }

    private IndexPO home;

    private IndexPO createNewUser(String username, String password) {
        home.toStartingPage();

        SignUpPO signUpPO = home.toSignUp();

        IndexPO indexPO = signUpPO.signUP(username, password);
        assertNotNull(indexPO);

        return indexPO;
    }

    @BeforeEach
    public void initTest() {

        getDriver().manage().deleteAllCookies();

        home = new IndexPO(getDriver(), getServerHost(), getServerPort());

        home.toStartingPage();

        assertTrue(home.isOnPage(), "Failed to start from home page");
    }

    @Test
    public void testDefaultTrips() {
        assertEquals(5, home.getNumberOfTripsDisplayed());
    }

    @Test
    public void testCreateAndLogoutUser() {
        assertFalse(home.isLoggedIn());

        String userID = getUniqueId();
        String password = "123456";

        home = createNewUser(userID, password);

        assumeTrue(home.isLoggedIn());
        assumeTrue(home.getDriver().getPageSource().contains(userID));

        home.doLogout();

        assertFalse(home.isLoggedIn());
        assertFalse(home.getDriver().getPageSource().contains(userID));
    }

    @Test
    public void testDisplayTripDetails() {

        List<String> rowElements = getDriver().findElements(By.xpath("//*[@id=\"tripTable\"]/tbody/tr[1]/td"))
                .stream().map(t -> t.getText()).collect(Collectors.toList());

        String buttonToClick = getDriver().findElement(By.xpath("/html/body/table/tbody/tr[1]/td[5]/form/input[2]")).getAttribute("id");
        DetailsPO detailsPO = home.getDetails(buttonToClick);

        assertTrue(detailsPO.getDriver().getPageSource().contains(rowElements.get(3)));
        assertTrue(detailsPO.getDriver().getPageSource().contains("Price: " + rowElements.get(2).toString()));

    }

    @Test
    public void testBookATrip() {

        String buttonId = home.getRandomButton();
        DetailsPO detailsPO = home.getDetails(buttonId);
        assertTrue(detailsPO.isOnPage());

        assertNull(detailsPO.makePurchase(""));

        String userID = getUniqueId();
        home = createNewUser(userID, "123456");

        detailsPO = home.getDetails(buttonId);
        detailsPO.makePurchase(userID);
        assertTrue(detailsPO.isOnPage());

        assertTrue(detailsPO.isInFirstColumn(userID));
    }

    @Test
    public void testDisplayUserInfo() {

        UserPO userPO = home.getUserInfo();
        assertNull(userPO);
        String userID = getUniqueId();
        home = createNewUser(userID, "123456");
        userPO = home.getUserInfo();
        assertNotNull(userPO);
        assertTrue(userPO.getUserName().contains(userID));
        userPO.doLogout();
        userPO = home.getUserInfo();
        assertNull(userPO);
    }

    @Test
    public void testSearch() {
        List<Trip> allTrips = tripService.getAllTrips(false);
        Trip firstTrip = allTrips.get(0);
        home = home.searchOnPage("byCost", firstTrip.getCost().toString());
        assertTrue(home.isInFirstColumn(firstTrip.getId().toString()));
        home.toStartingPage();
        home = home.searchOnPage("byLocation", firstTrip.getLocationName());
        assertTrue(home.isInFirstColumn(firstTrip.getId().toString()));
    }


}
