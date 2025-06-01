package casosDeUso;

import entidades.EventoSismico;
import javafx.scene.image.Image;

public class GenerarSismograma {

    public Image ejecutar(EventoSismico evento) {
        System.out.println("🖼 Ejecutando caso de uso GenerarSismograma para evento: " + evento);

        try {
            Image img = new Image(getClass().getResource("/img/sismograma.jpg").toExternalForm());
            System.out.println("✅ Imagen cargada correctamente");
            return img;
        } catch (Exception e) {
            System.out.println("❌ Error cargando imagen del sismograma: " + e.getMessage());
            return null;
        }
    }


}
