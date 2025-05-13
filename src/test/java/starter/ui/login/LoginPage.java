package starter.ui.login;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class LoginPage {

    public static final Target LOGIN_BUTTON_HOME = Target.the("open login window").located(By.id("login2"));
    public static final Target USERNAME = Target.the("username input").located(By.id("loginusername"));
    public static final Target PASSWORD = Target.the("password input").located(By.id("loginpassword"));
    public static final Target LOGIN_BUTTON_WINDOW = Target.the("login button").located(By.xpath("//button[@class='btn btn-primary' and contains(text(),'Log in')]"));
    public static final String USERNAME_XPATH = "//a[@id='nameofuser']";

}
