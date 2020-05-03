package no.enterprise.onlinetravelagency.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Purchase {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @NotNull
    private Users bookedBy;

    @NotNull
    private LocalDate dateOfBooking;

    @ManyToOne
    @NotNull
    private Trip tripInformation;

    public Purchase() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(Users bookedBy) {
        this.bookedBy = bookedBy;
    }

    public LocalDate getDateOfBooking() {
        return dateOfBooking;
    }

    public void setDateOfBooking(LocalDate dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public Trip getTripInformation() {
        return tripInformation;
    }

    public void setTripInformation(Trip tripInformation) {
        this.tripInformation = tripInformation;
    }
}
