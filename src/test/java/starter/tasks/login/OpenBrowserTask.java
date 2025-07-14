package starter.tasks.login;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class OpenBrowserTask implements Task {

    public static final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    @Override
    public <T extends Actor> void performAs(T actor) {

        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("environments.certification.webdriver.base.url");

        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new IllegalArgumentException("La URL base no está configurada. Verifique la configuración del entorno.");
        }

        actor.attemptsTo(
                Open.url(baseUrl)
        );
    }

    public static OpenBrowserTask openBrowserTask() {
        return instrumented(OpenBrowserTask.class);
    }
}
