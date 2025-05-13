package starter.utils;

import io.cucumber.datatable.DataTable;
import starter.models.addproduct.ProductModel;
import starter.models.login.LoginModel;

import java.util.List;

public class GetInfoDataTable {

    // Método estático que recibe un DataTable y devuelve un objeto LoginModel
    public static LoginModel getLoginCredentials(DataTable userInfo) {

        // Convertir el DataTable en una lista de listas de Strings
        List<List<String>> rows = userInfo.asLists(String.class);

        // Inicializar las variables para almacenar los datos del usuario y la contraseña
        String usuario = "";
        String contraseña = "";

        // Iterar sobre las filas del DataTable
        for (List<String> columns : rows) {
            // Obtener los valores de usuario y contraseña de la primera fila
            usuario = columns.get(0);  // Primer valor (usuario)
            contraseña = columns.get(1);  // Segundo valor (contraseña)
        }

        // Retornar el objeto LoginModel con los datos
        return new LoginModel(usuario, contraseña);
    }

    public static ProductModel getProductInfo(DataTable productInfo) {

        List<List<String>> rows = productInfo.asLists((String.class));

        String producto = "";
        String categoria = "";

        for (List<String> columns : rows) {
            producto = columns.get(0);
            categoria = columns.get(1);
        }

        return new ProductModel(producto, categoria);
    }
}
//   public void dataLogin(DataTable userInfo
//   ) {

//       List<List<String>> rows = userInfo.asLists(String.class);

//       for (List<String> columns : rows) {
//           this.usuario = columns.get(0);
//           this.contraseña = columns.get(1);
//       }
//   }

//   // Métodos para obtener los datos de usuario y contraseña
//   public String getUsuario() {
//       return usuario;
//   }

//   public String getContraseña() {
//       return contraseña;
//   }


