package starter.tasks.login;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.ensure.Ensure;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import starter.questions.login.GetNombreUsuario;
import starter.ui.login.LoginPage;

import java.time.Duration;

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
        } catch (Exception exception) {

            Throwable causaRaiz = obtenerCausaRaiz(exception);
            String motivoTecnico = describirCausa(causaRaiz);

            String mensajeError = String.format(
                    "No se pudo visualizar el mensaje de bienvenida para el usuario '%s'. " +
                            "Motivo técnico: %s",
                    usuario,
                    motivoTecnico
            );

            Serenity.reportThat("Fallo en la tarea de login",
                    () -> {
                        throw new AssertionError("No se pudo completar el login: " + exception.getMessage());
                    });

            throw new AssertionError(mensajeError, exception);
        }
    }

    private static Throwable obtenerCausaRaiz(Throwable error) {

        Throwable causaRaiz = error;

        while (causaRaiz.getCause() != null
                && causaRaiz.getCause() != causaRaiz) {

            causaRaiz = causaRaiz.getCause();
        }

        return causaRaiz;
    }

    private static String describirCausa(Throwable causa) {

        String detalle = causa.getMessage();

        if (detalle == null || detalle.isBlank()) {
            detalle = causa.getClass().getSimpleName();
        } else {
            detalle = detalle
                    .replaceAll("\\s+", " ")
                    .trim();
        }

        return detalle;
    }
}


