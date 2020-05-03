package no.enterprise.onlinetravelagency.selenium.po;

import no.enterprise.onlinetravelagency.selenium.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IndexPO extends LayoutPO {
    public IndexPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public IndexPO(PageObject other) {
        super(other);
    }

    public void toStartingPage() {
        getDriver().get(host + ":" + port);
    }

    public String getRandomButton() {
        List<WebElement> buttons = getDriver().findElements(By.xpath("//*[contains(./@id, 'detailsBtn')]"));
        int indexOfButton = new Random().nextInt(buttons.size());
        return buttons.get(indexOfButton).getAttribute("id");
    }

    public int getNumberOfTripsDisplayed() {
        return getDriver().findElements(By.xpath("//table//tr")).size() - 1;
    }

    public IndexPO searchOnPage(String selection, String query) {
        setText("queryInputID", query);
        WebElement dropDown = getDriver().findElement(By.id("selectOneID"));
        Select searchType = new Select(dropDown);
        searchType.selectByValue(selection);
        IndexPO indexPO = new IndexPO(this);
        clickAndWait("searchBtn");
        assertTrue(indexPO.isOnPage());
        return indexPO;
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Home page");
    }

    public DetailsPO getDetails(String id) {
        clickAndWait(id);
        DetailsPO detailsPO = new DetailsPO(this);
        assertTrue(detailsPO.isOnPage());

        return detailsPO;
    }

    public UserPO getUserInfo() {

        if (getDriver().findElements(By.id("userID")).size() == 0)
            return null;
        clickAndWait("userID");
        UserPO userPO = new UserPO(this);
        assertTrue(userPO.isOnPage());
        return userPO;
    }
}
