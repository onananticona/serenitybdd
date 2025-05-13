package starter.models.addproduct;

public class ProductModel {

    private final String producto;
    private final String categoria;

    public ProductModel(String product, String categoria) {
        this.producto = product;
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getProduct() {
        return producto;
    }
}
