package no.enterprise.onlinetravelagency.service;

import no.enterprise.onlinetravelagency.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TripService {

    @Autowired
    private EntityManager entityManager;

    public List<Trip> getAllTrips(Boolean withTravelers) {
        TypedQuery<Trip> query = entityManager.createQuery(
                "SELECT t FROM Trip t ORDER BY t.cost ASC", Trip.class
        );
        List<Trip> allTrips = query.getResultList();

        if (withTravelers) {
            allTrips.forEach(u -> u.getAllTravelers().size());
        }
        return allTrips;
    }

    public Long createTrip(
            String title,
            String description,
            Long cost,
            String locationName,
            LocalDate departureDate,
            LocalDate returnDate
    ) {
        Trip trip = new Trip();

        trip.setTitle(title);
        trip.setDescription(description);
        trip.setCost(cost);
        trip.setLocationName(locationName);
        trip.setDepartureDate(departureDate);
        trip.setReturnDate(returnDate);

        entityManager.persist(trip);

        return trip.getId();
    }

    public Trip getTrip(Long tripID, Boolean withTravelers) {
        Trip trip = entityManager.find(Trip.class, tripID);

        if (trip == null) {
            throw new IllegalStateException("No such trip found");
        }

        if (withTravelers) {
            trip.getAllTravelers().size();
        }

        return trip;
    }

    public void deleteTrip(Long tripID) {
        Trip tripToRemove = entityManager.find(Trip.class, tripID);

        if (tripToRemove == null) {
            throw new IllegalStateException("Trip not found in database");
        }

        entityManager.remove(tripToRemove);
    }

    public List<Trip> filterTripsByLocation(String locationName) {
        TypedQuery<Trip> query = entityManager.createQuery(
                "SELECT t FROM Trip t WHERE t.locationName =?1 ORDER BY t.cost ASC", Trip.class
        );

        query.setParameter(1, locationName);

        return query.getResultList();
    }

    public List<Trip> filterByCost(Long cost) {
        TypedQuery<Trip> query = entityManager.createQuery(
                "SELECT t FROM Trip t WHERE t.cost =?1", Trip.class
        );

        query.setParameter(1, cost);

        return query.getResultList();
    }
}
