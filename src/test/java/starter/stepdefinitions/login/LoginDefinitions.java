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
import starter.utils.GetInfoDataTable;

public class LoginDefinitions {

    @Managed
    WebDriver browser;

    String usuario = "";
    String contraseña = "";

    Actor user = Actor.named("usuario");
    LoginModel loginModel;

    //Credenciales válidas
    @Given("Estoy en la página de inicio de sesión de Product Store")
    public void estoyEnLaPáginaDeInicioDeSesiónDeProductStore() {
        user.can(BrowseTheWeb.with(browser));
        browser.get("https://www.demoblaze.com/");
    }

    @When("Ingreso mi nombre de usuario y contraseña")
    public void ingresoMiNombreDeUsuarioYContraseña(DataTable userInfo) {

        loginModel = GetInfoDataTable.getLoginCredentials(userInfo);

        user.attemptsTo(
                new LoginTask(loginModel.getUsuario(), loginModel.getContraseña())
        );
    }

    @Then("Debería ver el mensaje de bienvenida que contiene el nombre de usuario")
    public void verElMensajeDeBienvenidaQueContieneElNombreDeUsuario() {
        user.attemptsTo(
                new CheckNombreUsuario(loginModel.getUsuario())
        );
    }

    //Credenciales inválidas
    @Then("Debería ver un mensaje de error que diga {string}")
    public void deberíaVerUnMensajeDeErrorQueDiga(String errorMessage) {
        user.attemptsTo(

        );
    }


}