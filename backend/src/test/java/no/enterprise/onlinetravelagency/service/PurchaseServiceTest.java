package no.enterprise.onlinetravelagency.service;

import no.enterprise.onlinetravelagency.TestApplication;
import no.enterprise.onlinetravelagency.entity.Purchase;
import no.enterprise.onlinetravelagency.entity.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PurchaseServiceTest extends ServiceTestBase {

    @Autowired
    private TripService tripService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserService userService;

    @Test
    public void testCreatePurchase() {
        userService.createUser("Jonathan", "Jonathan", "Pusparajah", "123", "Jonathan@email.com", "user");
        Long tripID = tripService.createTrip("Test", "Desc", 200L, "Sri Lanka", LocalDate.now(), LocalDate.of(2020, 6, 7));
        Long purchaseId = purchaseService.newPurchase(tripID, "Jonathan");
        assertNotNull(purchaseId);
    }

    @Test
    public void testFilterPurchasesByUser() {
        String userName = "Jonathan";

        userService.createUser(userName, userName, "Pusparajah", "123", "Jonathan@emila.com", "user");
        Long firstTrip = tripService.createTrip("Test", "Desc", 200L, "Sri Lanka", LocalDate.now(), LocalDate.of(2020, 6, 7));
        Long secondTrip = tripService.createTrip("Test-2", "Desc-2", 300L, "Sri Lanka-2", LocalDate.now(), LocalDate.of(2020, 7, 6));
        Long firstPurchase = purchaseService.newPurchase(firstTrip, userName);
        Long secondPurchase = purchaseService.newPurchase(secondTrip, userName);
        Users users = userService.findUserByUserName(userName);
        assertNotNull(firstPurchase);
        assertNotNull(secondPurchase);

        List<Purchase> userPurchase = purchaseService.filterPurchasesByUser(users.getUserID());

        assertEquals(2, userPurchase.size());

    }

    @Test
    public void testFilterPurchasesByTrip() {
        String firstUser = "Jonahan";
        String secondUser = "Birgit";

        userService.createUser(firstUser, firstUser, "Pusparajah", "123", "JP@email.com", "user");
        userService.createUser(secondUser, secondUser, "Randby Kristensen", "123", "BRK@email.com", "user");
        Long firstTrip = tripService.createTrip("Test", "Desc", 200L, "Sri Lanka", LocalDate.now(), LocalDate.of(2020, 6, 7));
        Long secondTrip = tripService.createTrip("Test-2", "Desc-2", 300L, "Sri Lanka-2", LocalDate.now(), LocalDate.of(2020, 7, 6));
        Long firstPurchase = purchaseService.newPurchase(firstTrip, firstUser);
        Long secondPurchase = purchaseService.newPurchase(secondTrip, firstUser);
        Long thirdPurchase = purchaseService.newPurchase(secondTrip, secondUser);

        assertNotNull(firstPurchase);
        assertNotNull(secondPurchase);
        assertNotNull(thirdPurchase);

        List<Purchase> firstTripFilter = purchaseService.filterPurchasesByTrip(firstTrip);
        List<Purchase> secondTripFilter = purchaseService.filterPurchasesByTrip(secondTrip);

        assertEquals(1, firstTripFilter.size());
        assertEquals(2, secondTripFilter.size());
    }
}