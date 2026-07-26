package starter.tasks.login;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.ensure.Ensure;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import starter.questions.login.GetNombreUsuario;
import starter.ui.login.LoginPage;

import java.time.Duration;

import static starter.utils.ErrorDescripcion.describirCausa;
import static starter.utils.ErrorDescripcion.obtenerCausaRaiz;

public class CheckNombreUsuario implements Task {

    WebDriverWait wait;
    private final String usuario;

    // Constructor que acepta el nombre de usuario
    public CheckNombreUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        try {
            wait = new WebDriverWait(BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LoginPage.USERNAME_XPATH)));
            
            actor.attemptsTo(
                    Ensure.that(GetNombreUsuario.getNombreUsuario()).contains("Welcome" + " " + usuario)
            );
        } catch (AssertionError | Exception error) {

            Throwable causaRaiz = obtenerCausaRaiz(error);
            String motivoTecnico = describirCausa(causaRaiz);

            String mensajeError = String.format(
                    "No se pudo visualizar el mensaje de bienvenida para el usuario '%s'. " +
                            "Motivo técnico: %s",
                    usuario,
                    motivoTecnico
            );

            throw new AssertionError(mensajeError, error);
        }
    }

}


