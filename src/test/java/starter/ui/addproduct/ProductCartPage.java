package starter.ui.addproduct;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;

public class ProductCartPage {

    //Categorias
    public static final Target CATEGORIA_PHONES_BUTTON = Target.the("Phones category button")
            .located(By.xpath("//a[@id='itemc' and text()='Phones']"));
    public static final Target CATEGORIA_LAPTOPS_BUTTON = Target.the("Laptops category button")
            .located(By.xpath("//a[@id='itemc' and text()='Laptops']"));
    public static final Target CATEGORIA_MONITORS_BUTTON = Target.the("Monitors category button")
            .located(By.xpath("//a[@id='itemc' and text()='Monitors']"));
    //Productos
    public static final Target PRODUCT_LAPTOPS_SONYVAIO7_BUTTON = Target.the("Product Sony Vaio i7 button")
            .locatedBy("//a[@class='hrefch' and contains(text(), 'Sony vaio i7')]");
    //Botones
    public static final Target PRODUCT_ADD_BUTTON = Target.the("Product add button")
            .locatedBy("//a[@class='btn btn-success btn-lg' and contains(text(), 'Add to cart')]");
    public static final Target CART_BUTTON = Target.the("Cart button").located(By.id("cartur"));
    //Cart
    public static final String TITTLE_PRODUCT_XPATH = "//td[contains(text(),'Sony vaio i7')]";

    // Usamos HashMap para manejar dinámicamente el mapeo
    public static final Map<String, Target> CATEGORIAS = new HashMap<String, Target>() {{
        put("Phones", CATEGORIA_PHONES_BUTTON);
        put("Laptops", CATEGORIA_LAPTOPS_BUTTON);
        put("Monitors", CATEGORIA_MONITORS_BUTTON);
    }};

    // Usamos HashMap para manejar dinámicamente el mapeo
    public static final Map<String, Target> PRODUCTOS = new HashMap<String, Target>() {{
        put("Sony vaio i7", PRODUCT_LAPTOPS_SONYVAIO7_BUTTON);
    }};
}
