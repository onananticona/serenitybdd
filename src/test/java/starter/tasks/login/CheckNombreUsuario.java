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

         //  Serenity.reportThat("Fallo en la tarea de login",
         //          () -> {
         //              throw new AssertionError("No se pudo completar el login: " + exception.getMessage());
         //          });

            throw new AssertionError(mensajeError, error);
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

        if (causa instanceof TimeoutException) {
            return "El mensaje de bienvenida no apareció dentro de los "
                    + "10 segundos de espera. Verifica las credenciales, "
                    + "el proceso de login o el localizador. Detalle: "
                    + detalle;
        }

        if (causa instanceof StaleElementReferenceException) {
            return "La página actualizó el DOM y la referencia anterior "
                    + "al mensaje dejó de ser válida. Detalle: "
                    + detalle;
        }

        if (causa instanceof AssertionError) {
            return "El mensaje mostrado no coincide con el mensaje esperado. "
                    + "Detalle: " + detalle;
        }

        if (causa instanceof UnhandledAlertException) {
            return "El usuario no fue encontrado. "
                    + "Detalle: " + detalle;
        }



        return detalle;
    }
}


