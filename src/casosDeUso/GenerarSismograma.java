package casosDeUso;

import entidades.EstacionSismologica;
import entidades.EventoSismico;
import entidades.SerieTemporal;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Map;

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


    // ✅ Generar imagen sismograma con detalle de estaciones
    public Image generarSismograma(Map<SerieTemporal, List<String>> datosMuestrasSismicasXSerie, Map<SerieTemporal, EstacionSismologica> seriesXEstacion) {

        //aca se deberia llamar al caso de uso generar sismograma pero no podemios porque no modelamos ese cu

        // Cargar la imagen "sismograma.jpg" desde el directorio de recursos o ruta absoluta
        try {
            Image img = new Image("file:src/img/sismograma.jpg");
            return img;
        } catch (Exception e) {
            System.out.println("⚠ No se pudo cargar la imagen sismograma.jpg: " + e.getMessage());
            return null;
        }
    }


}
