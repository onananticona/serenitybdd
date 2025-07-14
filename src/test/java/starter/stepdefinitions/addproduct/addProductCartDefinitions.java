package starter.stepdefinitions.addproduct;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import org.openqa.selenium.WebDriver;
import starter.models.addproduct.ProductModel;
import starter.tasks.addproduct.AddProductCartTask;
import starter.tasks.addproduct.CheckAddProductTask;
import starter.tasks.detailscart.CheckProductCartTask;
import starter.tasks.login.CheckNombreUsuario;
import starter.ui.addproduct.ProductCartPage;
import starter.utils.GetInfoDataTable;

public class addProductCartDefinitions {

    @Managed
    WebDriver browser;
    ProductModel productModel;
    Actor user = Actor.named("user");

    @And("El usuario ve un mensaje de bienvenida que contiene su nombre de usuario")
    public void elUsuarioVeUnMensajeDeBienvenidaQueContieneSuNombreDeUsuario() {
        user.can(BrowseTheWeb.with(browser));
        user.attemptsTo(
                new CheckNombreUsuario("Chava5")
        );
    }

    @When("El usuario selecciona un producto de una de las categorías")
    public void elUsuarioSeleccionaUnProductoDeUnaDeLasCategorías(DataTable productInfo) {
        productModel = GetInfoDataTable.getProductInfo(productInfo);

        user.can(BrowseTheWeb.with(browser));
        user.attemptsTo((
                new AddProductCartTask(productModel.getProduct(), productModel.getCategoria())
        ));
    }

    @And("Añade el producto al carrito de compras")
    public void añadeElProductoAlCarritoDeCompras() {
        user.can(BrowseTheWeb.with(browser));
        user.attemptsTo(
                Click.on(ProductCartPage.PRODUCT_ADD_BUTTON)
        );
    }

    @Then("El sistema debería mostrar el mensaje de confirmación {string}")
    public void elSistemaDeberíaMostrarElMensajeDeConfirmación(String messageAddProductCart) {
        user.can(BrowseTheWeb.with(browser));
        user.attemptsTo(
                new CheckAddProductTask(messageAddProductCart)
        );
    }

    @And("El producto debería aparecer en el carrito de compras")
    public void elProductoDeberíaAparecerEnElCarritoDeCompras() {
        user.can(BrowseTheWeb.with(browser));
        user.attemptsTo(
                new CheckProductCartTask(productModel.getProduct())
        );
    }
}
