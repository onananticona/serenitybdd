package starter.utils;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;

public class ErrorDescripcion {

    public static Throwable obtenerCausaRaiz(Throwable error) {

        Throwable causaRaiz = error;

        while (causaRaiz.getCause() != null && causaRaiz.getCause() != causaRaiz) {
            causaRaiz = causaRaiz.getCause();
        }

        return causaRaiz;
    }

    public static String describirCausa(Throwable causa) {

        String detalle = causa.getMessage();

        if (detalle == null || detalle.isBlank()) {
            detalle = causa.getClass().getSimpleName();
        } else {
            detalle = detalle
                    .replaceAll("\\s+", " ")
                    .trim();
        }

        if (causa instanceof TimeoutException) {
            return "El mensaje de bienvenida no apareció dentro de los "
                    + "10 segundos de espera. Verifica las credenciales, "
                    + "el proceso de login o el localizador. Detalle: "
                    + detalle;
        }

        if (causa instanceof StaleElementReferenceException) {
            return "La página actualizó el DOM y la referencia anterior "
                    + "al mensaje dejó de ser válida. Detalle: "
                    + detalle;
        }

        if (causa instanceof AssertionError) {
            return "El mensaje mostrado no coincide con el mensaje esperado. "
                    + "Detalle: " + detalle;
        }

        if (causa instanceof UnhandledAlertException) {
            return "El usuario no fue encontrado. "
                    + "Detalle: " + detalle;
        }

        return detalle;
    }
}
