package starter.tasks.detailscart;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.Alert;

public class AcceptAlertTask implements Task {

    @Override
    public <T extends Actor> void performAs(T actor) {
        // Cambiar al contexto de la alerta
        Alert alert = BrowseTheWeb.as(actor).getDriver().switchTo().alert();
        alert.accept();  // Aceptar la alerta
    }
}