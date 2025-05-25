package starter.tasks.addproduct;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.webdriver.exceptions.ElementShouldBePresentException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import starter.ui.addproduct.ProductCartPage;


public class AddProductCartTask implements Task {

    private final String producto;
    private final String categoria;

    public AddProductCartTask(String producto, String categoria) {
        this.producto = producto;
        this.categoria = categoria;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        // Target categoriaTarget = Target.the("Botón de categoría " + categoria)
        //         .located(By.xpath("//a[@id='itemc' and text()='" + categoria + "']"));
        Target categoriaTarget = ProductCartPage.CATEGORIAS.get(categoria);
        Target productTarget = ProductCartPage.PRODUCTOS.get(producto);

        try {
            actor.attemptsTo(
                    Click.on(categoriaTarget),
                    Click.on(productTarget)
            );
        } catch (ElementShouldBePresentException e) {
            Serenity.recordReportData()
                    .withTitle("Error: Elemento no presente")
                    .andContents("No se pudo localizar el elemento. Mensaje: " + e.getMessage());

            Serenity.reportThat("Fallo en AddProductCartTask",
                    () -> {
                        throw new AssertionError("Error detectado. Por favor revisa la sección 'Error: Elemento no presente' para más detalles.", e);
                    });
        } catch (NoSuchElementException e) {
            Serenity.recordReportData()
                    .withTitle("Error: Elemento no encontrado en DOM")
                    .andContents("No se encontró el elemento en el DOM. Mensaje: " + e.getMessage());

            Serenity.reportThat("Fallo en AddProductCartTask",
                    () -> {
                        assert false : "Error detectado. Por favor revisa la sección 'Error: Elemento no encontrado en DOM' para más detalles.";
                    });
        } catch (ElementNotInteractableException e) {
            Serenity.recordReportData()
                    .withTitle("Error: Elemento no interactuable")
                    .andContents("El elemento está en el DOM pero no es interactuable. Mensaje: " + e.getMessage());

            Serenity.reportThat("Fallo en AddProductCartTask",
                    () -> {
                        assert false : "Error detectado. Por favor revisa la sección 'Error: Elemento no interactuable' para más detalles.";
                    });
        } catch (Exception e) {
            Serenity.recordReportData()
                    .withTitle("Error inesperado en AddProductCartTask")
                    .andContents("No se pudo completar la tarea. Detalles: " + e.getMessage());

            Serenity.reportThat("Fallo inesperado en AddProductCartTask",
                    () -> {
                        assert false : "Error detectado. Por favor revisa la sección 'Error inesperado en AddProductCartTask' para más detalles.";
                    });
        }
    }
}
