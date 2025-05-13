package starter.tasks.detailscart;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.ensure.Ensure;
import starter.questions.detailscart.GetTittleProduct;
import starter.ui.addproduct.ProductCartPage;

public class CheckProductCartTask implements Task {

    private final String producto;

    public CheckProductCartTask(String producto) {
        this.producto = producto;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(ProductCartPage.CART_BUTTON),
                Ensure.that(GetTittleProduct.getTittleProduct()).contains(producto)
        );
    }
}
