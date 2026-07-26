package starter.tasks.login;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import starter.ui.login.LoginPage;

import static starter.utils.ErrorDescripcion.describirCausa;
import static starter.utils.ErrorDescripcion.obtenerCausaRaiz;

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

}
