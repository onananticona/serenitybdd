package starter.questions.detailscart;

import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import starter.ui.addproduct.ProductCartPage;

public class GetTittleProduct {

    public static Question<String> getTittleProduct() {
        return Question.about("Validar producto en el carrito de compras").answeredBy(
                actor -> BrowseTheWeb.as(actor).textOf(ProductCartPage.TITTLE_PRODUCT_XPATH)
        );
    }
}
