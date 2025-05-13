package starter.tasks.detailscart;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ExtractAlertTextTask implements Task {

    private static String alertText;

    @Override
    public <T extends Actor> void performAs(T actor) {
        try {
            WebDriverWait wait = new WebDriverWait(BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.alertIsPresent());

            Alert alert = BrowseTheWeb.as(actor).getDriver().switchTo().alert();
            alertText = alert.getText();

        } catch (NoAlertPresentException e) {
            System.out.println("No alert present at the moment.");
        }
    }

    public static String getAlertText() {
        return alertText;
    }
}
