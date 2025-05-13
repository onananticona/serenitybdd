package starter.tasks.addproduct;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;
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

        if (categoriaTarget != null && productTarget != null) {
            actor.attemptsTo(
                    Click.on(categoriaTarget),
                    Click.on(productTarget)
            );
        } else {
            // Si la categoría no está en el Map, puedes lanzar un error o manejarlo de otra manera
            throw new IllegalArgumentException("Categoría no válida: " + categoria);
        }
    }
}
