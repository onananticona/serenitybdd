package starter.stepdefinitions.login;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.WebDriver;
import starter.models.login.LoginModel;
import starter.tasks.login.CheckNombreUsuario;
import starter.tasks.login.LoginTask;
import starter.tasks.login.OpenBrowserTask;
import starter.utils.GetInfoDataTable;


public class LoginDefinitions {

    @Managed
    WebDriver browser;
    LoginModel loginModel;
    Actor user = Actor.named("user");

    @Given("El usuario está en la página de inicio de sesión de Product Store")
    public void elUsuarioEstáEnLaPáginaDeInicioDeSesiónDeProductStore() {
        user.can(BrowseTheWeb.with(browser));
        user.attemptsTo(OpenBrowserTask.openBrowserTask());
    }

    @When("El usuario ingresa su nombre de usuario y contraseña")
    public void elUsuarioIngresaSuNombreDeUsuarioYContraseña(DataTable userInfo) {
        loginModel = GetInfoDataTable.getLoginCredentials(userInfo);

        user.attemptsTo(
                new LoginTask(loginModel.getUsuario(), loginModel.getContraseña())
        );
    }

    @Then("El usuario debería ver un mensaje de bienvenida que contiene su nombre de usuario")
    public void elUsuarioDeberíaVerUnMensajeDeBienvenidaQueContieneSuNombreDeUsuario() {
        user.attemptsTo(
                new CheckNombreUsuario(loginModel.getUsuario())
        );
    }
}