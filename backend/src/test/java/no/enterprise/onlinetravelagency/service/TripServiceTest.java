package no.enterprise.onlinetravelagency.service;

import no.enterprise.onlinetravelagency.TestApplication;
import no.enterprise.onlinetravelagency.entity.Trip;
import no.enterprise.onlinetravelagency.entity.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TripServiceTest extends ServiceTestBase {
    @Autowired
    private TripService tripService;


    @Test
    public void testCreateTrip() {
        Long id = tripService.createTrip(
                "Title",
                "My description",
                100L,
                "Norway",
                LocalDate.now(),
                LocalDate.of(2020, 8, 3)
        );
        assertNotNull(id);

        Trip myTrip = tripService.getTrip(id, true);
        List<Users> users = myTrip.getAllTravelers();
        assertNotNull(myTrip);
    }

    @Test
    public void testGetAllTrips() {
        Long firstTrip = tripService.createTrip(
                "Foo",
                "Foo-Foo",
                1000L,
                "Midgard",
                LocalDate.now(),
                LocalDate.of(2020, 8, 3)
        );

        Long secondTrip = tripService.createTrip(
                "Bar",
                "Bar-Bar",
                2000L,
                "Midgard",
                LocalDate.now(),
                LocalDate.of(2020, 8, 6)
        );

        assertNotNull(firstTrip);
        assertNotNull(secondTrip);

        List<Trip> allTrips = tripService.getAllTrips(false);
        assertEquals(2, allTrips.size());
    }

    @Test
    public void testDeleteTrip() {
        Long id = tripService.createTrip(
                "Foo",
                "Foo-Foo",
                1000L,
                "Foo",
                LocalDate.now(),
                LocalDate.of(2020, 12, 12)
        );
        assertNotNull(id);
        tripService.deleteTrip(id);
        assertThrows(IllegalStateException.class, () -> tripService.getTrip(id, false));
    }

    @Test
    public void testFilterTripsByLocation() {
        Long firstTrip = tripService.createTrip(
                "Foo",
                "Foo-Foo",
                100L,
                "Bosnia",
                LocalDate.now(),
                LocalDate.of(2020, 9, 28)
        );
        Long secondTrip = tripService.createTrip(
                "Bar",
                "Bar-Bar",
                2000L,
                "Norway",
                LocalDate.now(),
                LocalDate.of(2020, 9, 6)
        );
        Long thirdTrip = tripService.createTrip(
                "Bar",
                "Bar-Bar",
                2000L,
                "Norway",
                LocalDate.now(),
                LocalDate.of(2020, 11, 6)
        );

        assertNotNull(firstTrip);
        assertNotNull(secondTrip);
        assertNotNull(thirdTrip);

        List<Trip> bosnianTrips = tripService.filterTripsByLocation("Bosnia");
        List<Trip> norwegianTrips = tripService.filterTripsByLocation("Norway");

        assertEquals(1, bosnianTrips.size());
        assertEquals(2, norwegianTrips.size());
    }

    @Test
    public void filterByCost() {
        Long firstTrip = tripService.createTrip(
                "Foo",
                "Foo-Foo",
                1000L,
                "Bosnia",
                LocalDate.now(),
                LocalDate.of(2020, 9, 28)
        );
        Long secondTrip = tripService.createTrip(
                "Bar",
                "Bar-Bar",
                2000L,
                "Norway",
                LocalDate.now(),
                LocalDate.of(2020, 9, 6)
        );
        Long thirdTrip = tripService.createTrip(
                "Bar",
                "Bar-Bar",
                2000L,
                "Norway",
                LocalDate.now(),
                LocalDate.of(2020, 11, 6)
        );

        assertNotNull(firstTrip);
        assertNotNull(secondTrip);
        assertNotNull(thirdTrip);

        List<Trip> cheapTrips = tripService.filterByCost(1000L);
        List<Trip> expensiveTrips = tripService.filterByCost(2000L);

        assertEquals(1, cheapTrips.size());
        assertEquals(2, expensiveTrips.size());
    }


}