package no.enterprise.onlinetravelagency.frontend.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class SearchController implements Serializable {
    String selection;
    String query;

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String getSearchLink() {
        return "index?searchBy=" + selection + "&query=" + query + "&faces-redirect=true";
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
