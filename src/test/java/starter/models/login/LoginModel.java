package starter.models.login;

public class LoginModel {

    private final String usuario;
    private final String contraseña;

    public LoginModel(String usuario, String contraseña) {
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

}
