package no.enterprise.onlinetravelagency.service;


import no.enterprise.onlinetravelagency.entity.Purchase;
import no.enterprise.onlinetravelagency.entity.Trip;
import no.enterprise.onlinetravelagency.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PurchaseService {

    @Autowired
    private EntityManager entityManager;

    public List<Purchase> getAllPurchases(){
        TypedQuery<Purchase> query = entityManager.createQuery("SELECT p FROM Purchase p",Purchase.class);
        return query.getResultList();
    }

    public List<Purchase> filterPurchasesByTrip(Long tripID) {
        TypedQuery<Purchase> query = entityManager.createQuery(
                "SELECT p FROM Purchase p WHERE p.tripInformation.id =?1", Purchase.class);
        query.setParameter(1, tripID);

        return query.getResultList();
    }

    public Long newPurchase(Long tripID, String userID) {
        Trip trip = entityManager.find(Trip.class, tripID);
        Users users = entityManager.find(Users.class, userID);

        if (trip == null) {
            throw new IllegalStateException("Trip not found");
        }
        if (users == null) {
            throw new IllegalStateException("User not found");
        }

        Purchase purchase = new Purchase();
        purchase.setBookedBy(users);
        purchase.setDateOfBooking(LocalDate.now());
        purchase.setTripInformation(trip);
        users.getBookedTrips().add(trip);
        entityManager.persist(purchase);

        return purchase.getId();
    }

    //Could have remove purchase if I have time to implement it

    public List<Purchase> filterPurchasesByUser(String userID) {
        TypedQuery<Purchase> query = entityManager.createQuery(
                "SELECT p FROM Purchase p WHERE p.bookedBy.userID =?1", Purchase.class
        );

        query.setParameter(1, userID);

        return query.getResultList();
    }
}
