package casosDeUso;

import entidades.EventoSismico;
import javafx.scene.image.Image;

/**
 * Stub del caso de uso GenerarSismograma.
 * Actualmente no realiza cálculos, solo devuelve una imagen fija.
 */
public class GenerarSismograma {

    public Image ejecutar(EventoSismico evento) {
        // Retorna la imagen fija como mock del sismograma
        return new Image("/img/sismograma.jpg"); // asegurate que esté en src/img/
    }
}
