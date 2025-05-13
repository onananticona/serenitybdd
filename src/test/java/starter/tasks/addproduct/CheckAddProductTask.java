package starter.tasks.addproduct;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.ensure.Ensure;
import starter.questions.addproduct.GetAlertText;
import starter.tasks.detailscart.AcceptAlertTask;
import starter.tasks.detailscart.ExtractAlertTextTask;

public class CheckAddProductTask implements Task {

    private final String messageAddProductCart;

    public CheckAddProductTask(String messageAddProductCart) {
        this.messageAddProductCart = messageAddProductCart;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                new ExtractAlertTextTask(),
                Ensure.that(GetAlertText.alertText()).contains(messageAddProductCart),
                new AcceptAlertTask()  // Acepta la alerta
        );
    }
}
