package starter.tasks.login;

import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.ensure.Ensure;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import starter.questions.login.GetNombreUsuario;
import starter.ui.login.LoginPage;

import java.time.Duration;

public class CheckNombreUsuario implements Task {

    private final String usuario;

    // Constructor que acepta el nombre de usuario
    public CheckNombreUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        // Configurar WebDriverWait con un tiempo de espera de 10 segundos
        WebDriverWait wait = new WebDriverWait(BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(10));

        // Esperar hasta que el nombre de usuario sea visible
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(LoginPage.USERNAME_XPATH))
        );

        actor.attemptsTo(
                Ensure.that(GetNombreUsuario.getNombreUsuario()).contains("Welcome" + " " + usuario)
        );
    }
}
