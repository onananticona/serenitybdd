package starter.tasks.addproduct;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.StaleElementReferenceException;
import starter.ui.addproduct.ProductCartPage;

import java.time.Duration;

import static starter.utils.ErrorDescripcion.describirCausa;
import static starter.utils.ErrorDescripcion.obtenerCausaRaiz;

public class AddProductCartTask implements Task {

    private final String producto;
    private final String categoria;

    public AddProductCartTask(String producto, String categoria) {
        this.producto = producto;
        this.categoria = categoria;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        Target categoriaTarget = ProductCartPage.CATEGORIAS.get(categoria);

        Target productoTarget = ProductCartPage.PRODUCTOS.get(producto);

        validarTarget(
                categoriaTarget,
                "No existe un Target configurado para la categoría '"
                        + categoria + "'."
        );

        validarTarget(
                productoTarget,
                "No existe un Target configurado para el producto '"
                        + producto + "'."
        );

        try {

            clickConReintento(
                    actor,
                    categoriaTarget,
                    5
            );

            clickConReintento(
                    actor,
                    productoTarget,
                    10
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

    private static <T extends Actor> void clickConReintento(
            T actor,
            Target target,
            int tiempoEsperaSegundos
    ) {

        StaleElementReferenceException ultimoError = null;

        for (int intento = 1; intento <= 3; intento++) {

            try {
                /*
                 * En cada intento se vuelve a resolver el Target.
                 * De esta manera se obtiene el elemento actual del DOM.
                 */
                target.resolveFor(actor)
                        .withTimeoutOf(
                                Duration.ofSeconds(tiempoEsperaSegundos)
                        )
                        .waitUntilClickable()
                        .click();

                // El clic funcionó: terminar el método.
                return;

            } catch (StaleElementReferenceException error) {

                ultimoError = error;

                /*
                 * El siguiente intento volverá a resolver el Target,
                 * obteniendo una referencia nueva del DOM.
                 */
            }
        }

        throw ultimoError;
    }
}