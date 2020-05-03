package no.enterprise.onlinetravelagency.selenium.po;

import no.enterprise.onlinetravelagency.selenium.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public abstract class LayoutPO extends PageObject {

    public LayoutPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public LayoutPO(PageObject other) {
        super(other);
    }

    public SignUpPO toSignUp() {

        clickAndWait("linkToSignupId");

        SignUpPO po = new SignUpPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public IndexPO doLogout() {

        clickAndWait("logoutBtn");

        IndexPO po = new IndexPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public boolean isLoggedIn() {

        return getDriver().findElements(By.id("logoutBtn")).size() > 0 &&
                getDriver().findElements((By.id("linkToSignupId"))).isEmpty();
    }

    public boolean isInFirstColumn(String id) {
        List<WebElement> table = getDriver().findElements(By.xpath("//*[@id=\"tripTable\"]//tbody//td[1]"));
        boolean isFound = false;
        for (WebElement column : table) {
            if (column.getText().equals(id)) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }
}
