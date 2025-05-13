package starter.questions.addproduct;

import net.serenitybdd.screenplay.Question;
import starter.tasks.detailscart.ExtractAlertTextTask;


public class GetAlertText {

    // Crear una pregunta para obtener el texto de la alerta
    public static Question<String> alertText() {
        return actor -> ExtractAlertTextTask.getAlertText();  // Devuelve el texto extraído de la alerta
    }
}
