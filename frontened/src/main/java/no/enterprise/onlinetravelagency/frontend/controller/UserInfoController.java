package no.enterprise.onlinetravelagency.frontend.controller;

import no.enterprise.onlinetravelagency.entity.Purchase;
import no.enterprise.onlinetravelagency.entity.Users;
import no.enterprise.onlinetravelagency.service.PurchaseService;
import no.enterprise.onlinetravelagency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class UserInfoController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserService userService;

    public String getUserName() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    public Users getUser() {
        return userService.findUserByUserName(getUserName());
    }

    public List<Purchase> listPurchases() {
        return purchaseService.filterPurchasesByUser(getUserName());
    }
}
