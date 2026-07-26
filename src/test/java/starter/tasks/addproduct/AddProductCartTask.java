package starter.tasks.addproduct;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.StaleElementReferenceException;
import starter.ui.addproduct.ProductCartPage;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class AddProductCartTask implements Task {

    private final String producto;
    private final String categoria;

    public AddProductCartTask(String producto, String categoria) {
        this.producto = producto;
        this.categoria = categoria;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        try {
            Target categoriaTarget = ProductCartPage.CATEGORIAS.get(categoria);
            Target productoTarget = ProductCartPage.PRODUCTOS.get(producto);

            validarTarget(
                    categoriaTarget,
                    "No existe un Target configurado para la categoría '" + categoria + "'."
            );

            validarTarget(
                    productoTarget,
                    "No existe un Target configurado para el producto '" + producto + "'."
            );

            /*
             * Se separan los clics porque al seleccionar una categoría,
             * Demoblaze actualiza la lista de productos en el DOM.
             */
            actor.attemptsTo(
                    Click.on(categoriaTarget)
            );

            actor.attemptsTo(
                    WaitUntil.the(productoTarget, isVisible())
                            .forNoMoreThan(10).seconds(),

                    Click.on(productoTarget)
            );

        } catch (Exception exception) {

            Throwable causaRaiz = obtenerCausaRaiz(exception);
            String motivoTecnico = describirCausa(causaRaiz);

            /*
             * Debe estar en una sola línea para que Jenkins pueda
             * recuperarlo fácilmente desde el reporte de Failsafe.
             */
            String mensajeError = String.format(
                    "No se pudo seleccionar el producto '%s' de la categoría '%s'. " +
                            "Motivo técnico: %s",
                    producto,
                    categoria,
                    motivoTecnico
            );

            Serenity.recordReportData()
                    .withTitle("Error en AddProductCartTask")
                    .andContents(
                            """
                            No se pudo completar la selección del producto.

                            Producto: %s
                            Categoría: %s
                            Motivo técnico: %s
                            """.formatted(producto, categoria, motivoTecnico)
                    );

            throw new AssertionError(mensajeError, exception);
        }
    }

    private static void validarTarget(Target target, String mensaje) {
        if (target == null) {
            throw new IllegalArgumentException(mensaje);
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