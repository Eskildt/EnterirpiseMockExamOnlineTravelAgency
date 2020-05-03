package no.enterprise.onlinetravelagency.frontend.controller;

import no.enterprise.onlinetravelagency.entity.Purchase;
import no.enterprise.onlinetravelagency.entity.Trip;
import no.enterprise.onlinetravelagency.service.PurchaseService;
import no.enterprise.onlinetravelagency.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class TravelController implements Serializable {

    @Autowired
    private TripService tripService;

    @Autowired
    private PurchaseService purchaseService;

    private Long tripID;

    public List<Trip> getTrips(int numberOfTrips) {
        return tripService.getAllTrips(true).stream().limit(numberOfTrips).collect(Collectors.toList());
    }

    public String getTripRedirectionLink(Long tripID) {
        this.tripID = tripID;
        return "/details.jsf?tripID=" + tripID + "&faces-redirect=true";
    }

    public Trip getTrip(Long id) {
        return tripService.getTrip(id, true);
    }

    public List<Trip> filterTripsBy(String searchBy, String query) {
        if (searchBy.equals("byCost")) {
            return tripService.filterByCost(Long.valueOf(query));
        } else if (searchBy.equals("byLocation")) {
            return tripService.filterTripsByLocation(query);
        } else {
            return null;
        }
    }

    public String makePurchase(String userID) {
        if (isNotPurchased(tripID, userID)) {
            purchaseService.newPurchase(tripID, userID);
            return "details?tripID=" + tripID + "&isPurchased=true&faces-redirect=true";
        } else {
            return "details?tripID=" + tripID + "&isPurchased=false&faces-redirect=true";
        }
    }

    //If it was not purchased then trip for user is null
    public Boolean isNotPurchased(Long tripID, String userID) {
        List<Purchase> allPurchase = purchaseService.filterPurchasesByUser(userID);
        return allPurchase.stream().filter(p -> p.getTripInformation().getId().equals(tripID)).findAny().orElse(null) == null;
    }

}
