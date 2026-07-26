package starter.tasks.login;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import org.openqa.selenium.StaleElementReferenceException;
import starter.ui.login.LoginPage;

public class LoginTask implements Task {

    private final String username;
    private final String password;

    public LoginTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        try {
            actor.attemptsTo(
                    Click.on(LoginPage.LOGIN_BUTTON_HOME),
                    Enter.theValue(username).into(LoginPage.USERNAME),
                    Enter.theValue(password).into(LoginPage.PASSWORD),
                    Click.on(LoginPage.LOGIN_BUTTON_WINDOW)
            );
        } catch (Exception exception) {

            Throwable causaRaiz = obtenerCausaRaiz(exception);
            String motivoTecnico = describirCausa(causaRaiz);

            String mensajeError = String.format(
                    "No se pudo iniciar sesión con el usuario '%s'. " +
                            "Motivo técnico: %s",
                    username,
                    motivoTecnico
            );

            Serenity.recordReportData()
                    .withTitle("Error durante el login")
                    .andContents("**Mensaje:** Usuario no encontrado\n\n**Sugerencia:** Verificar credenciales");

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

        if (causa instanceof StaleElementReferenceException) {
            return "La página actualizó el DOM y la referencia anterior al elemento dejó "
                    + "de ser válida. Detalle: " + detalle;
        }

        return detalle;
    }
}
