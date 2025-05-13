package starter.questions.login;

import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import starter.ui.login.LoginPage;

public class GetNombreUsuario {

//   @Override
//   public String answeredBy(Actor actor) {
//       return BrowseTheWeb.as(actor).textOf(LoginPage.USERNAME_XPATH);
//   }

//   public static GetNombreUsuario message() {
//       return new GetNombreUsuario();
//   }

    public static Question<String> getNombreUsuario() {
        return Question.about("Validar nombre de usuario").answeredBy(
                actor -> BrowseTheWeb.as(actor).textOf(LoginPage.USERNAME_XPATH)
        );
    }
}
