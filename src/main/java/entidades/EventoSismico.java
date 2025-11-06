package entidades;

import entidades.estadoPadreAbstracto.EstadoES;
import entidades.estadosConcretos.AutoDetectado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoSismico {

    private LocalDateTime fechaHoraFin;
    private LocalDateTime fechaHoraOcurrencia;
    private String latitudEpicentro;
    private String longitudEpicentro;
    private String latitudHipocentro;
    private String longitudHipocentro;
    private double valorMagnitud;
    private LocalDateTime fechaHoraRevision;
    private Empleado responsableRevision;
    private List<CambioEstadoES> cambiosEstado;
    private EstadoES estado;
    private ClasificacionSismo clasificacionSismo;
    private AlcanceSismo alcanceSismo;
    private OrigenDeGeneracion origenGeneracion;
    private List<SerieTemporal> seriesTemporales;

    public EventoSismico(LocalDateTime fechaHoraOcurrencia, String latitudEpicentro, String longitudEpicentro,
                         String latitudHipocentro, String longitudHipocentro, double valorMagnitud,
                         ClasificacionSismo clasificacionSismo, AlcanceSismo alcanceSismo,
                         OrigenDeGeneracion origenGeneracion) {
        this.fechaHoraOcurrencia = fechaHoraOcurrencia;
        this.latitudEpicentro = latitudEpicentro;
        this.longitudEpicentro = longitudEpicentro;
        this.latitudHipocentro = latitudHipocentro;
        this.longitudHipocentro = longitudHipocentro;
        this.valorMagnitud = valorMagnitud;
        this.clasificacionSismo = clasificacionSismo;
        this.alcanceSismo = alcanceSismo;
        this.origenGeneracion = origenGeneracion;
        this.seriesTemporales = new ArrayList<>();

        this.cambiosEstado = new ArrayList<>();
        EstadoES estadoInicial = new AutoDetectado();
        this.estado = estadoInicial;
        this.cambiosEstado.add(new CambioEstadoES(LocalDateTime.now(), null, null, estadoInicial));
    }

    public void revisar(LocalDateTime fh, Empleado resp) {
        this.estado.revisar(this, fh, resp);
    }

    public void confirmar(LocalDateTime fh, Empleado resp) {
        this.estado.confirmar(this, fh, resp);
    }

    public void rechazar(LocalDateTime fh, Empleado resp) {
        this.estado.rechazar(this, fh, resp);
    }

    public void derivar(LocalDateTime fh, Empleado resp) {
        this.estado.derivar(this, fh, resp);
    }

    public void agregarCE(CambioEstadoES nuevoCambio) {
        this.cambiosEstado.add(nuevoCambio);
    }

    public CambioEstadoES getCambioEstadoActual() {
        return this.cambiosEstado.stream()
                .filter(CambioEstadoES::esActual)
                .findFirst()
                .orElse(null);
    }

    public Boolean soyAutoDetectado() {
        return this.estado instanceof AutoDetectado;
    }

    public void validarExistencias() {
        if (this.getValorMagnitud() == 0
                || this.getAlcanceSismo() == null
                || this.getOrigenGeneracion() == null) {
            throw new IllegalStateException("Faltan datos esenciales (magnitud, alcance u origen) para el evento.");
        }
    }

    public String getFechaHoraOcurrenciaTexto() {
        return this.fechaHoraOcurrencia != null ? this.fechaHoraOcurrencia.toString() : "";
    }

    public Map<String, Object> getDatos() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("fechaHora: ", getFechaHoraOcurrencia());
        datos.put("coordEpicentro: ", getCoordEpicentro());
        datos.put("coordHipocentro: ", getCoordHipocentro());
        datos.put("magnitud: ", getValorMagnitud());
        return datos;
    }

    public String getCoordEpicentro() {
        return getLatitudEpicentro() + getLongitudEpicentro();
    }

    public String getCoordHipocentro() {
        return getLatitudHipocentro() + getLongitudHipocentro();
    }

    public Map<SerieTemporal, List<String>> getDatosMuestrasSismicas() {
        Map<SerieTemporal, List<String>> datosSeries = new HashMap<>();
        for (SerieTemporal serie : seriesTemporales) {
            List<String> muestrasInfo = Collections.singletonList(serie.getDatosMuestrasSismicas());
            datosSeries.put(serie, muestrasInfo);
        }
        return datosSeries;
    }

    public Map<String, String> getACO() {
        Map<String, String> datos = new HashMap<>();
        datos.put("alcance", this.alcanceSismo.getNombre());
        datos.put("clasificacion", this.clasificacionSismo.getNombre());
        datos.put("origen", this.origenGeneracion.getNombre());
        return datos;
    }

    public Map<SerieTemporal, EstacionSismologica> getSeriesPorEstacion() {
        Map<SerieTemporal, EstacionSismologica> resultado = new HashMap<>();
        for (SerieTemporal serie : seriesTemporales) {
            Sismografo sismografo = (Sismografo) serie.getMuestrasSismicas();
            if (sismografo != null) {
                resultado.put(serie, sismografo.getEstacionSismologica());
            }
        }
        return resultado;
    }

    @Override
    public String toString() {
        String fecha = fechaHoraOcurrencia != null ? fechaHoraOcurrencia.toString() : "-";
        String epicentro = (latitudEpicentro != null ? latitudEpicentro : "-") + ", " + (longitudEpicentro != null ? longitudEpicentro : "-");
        String hipocentro = (latitudHipocentro != null ? latitudHipocentro : "-") + ", " + (longitudHipocentro != null ? longitudHipocentro : "-");
        String magnitud = String.valueOf(valorMagnitud);
        String estadoNombre = (estado != null && estado.getNombre() != null) ? estado.getNombre() : "-";
        String clasif = (clasificacionSismo != null && clasificacionSismo.getNombre() != null) ? clasificacionSismo.getNombre() : "-";
        String alcance = (alcanceSismo != null && alcanceSismo.getNombre() != null) ? alcanceSismo.getNombre() : "-";
        String origen = (origenGeneracion != null && origenGeneracion.getNombre() != null) ? origenGeneracion.getNombre() : "-";
        int seriesCount = (seriesTemporales != null) ? seriesTemporales.size() : 0;

        // Información del cambio de estado actual (si existe)
        CambioEstadoES cambioActual = getCambioEstadoActual();
        String cambioInfo = "-";
        if (cambioActual != null) {
            String desde = cambioActual.getFechaHoraInicio() != null ? cambioActual.getFechaHoraInicio().toString() : "-";
            Empleado resp = cambioActual.getResponsable();
            String responsableStr = "-";
            if (resp != null) {
                String nom = resp.getNombre() != null ? resp.getNombre() : "";
                String ape = resp.getApellido() != null ? resp.getApellido() : "";
                responsableStr = (nom + " " + ape).trim();
                if (responsableStr.isEmpty()) responsableStr = "-";
            }
            String estadoCambio = (cambioActual.getEstado() != null && cambioActual.getEstado().getNombre() != null) ? cambioActual.getEstado().getNombre() : "-";
            cambioInfo = String.format("Estado: %s; desde: %s; responsable: %s", estadoCambio, desde, responsableStr);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("+---------------------------------------------------------------+\n");
        sb.append(String.format("| %-30s | %-27s |\n", "Campo", "Valor"));
        sb.append("+---------------------------------------------------------------+\n");
        sb.append(String.format("| %-30s | %-27s |\n", "Fecha ocurrencia", fecha));
        sb.append(String.format("| %-30s | %-27s |\n", "Epicentro (lat,lon)", epicentro));
        sb.append(String.format("| %-30s | %-27s |\n", "Hipocentro (lat,lon)", hipocentro));
        sb.append(String.format("| %-30s | %-27s |\n", "Magnitud", magnitud));
        sb.append(String.format("| %-30s | %-27s |\n", "Estado actual", estadoNombre));
        // Nueva fila: detalle del cambio de estado actual
        sb.append(String.format("| %-30s | %-27s |\n", "Cambio estado actual", cambioInfo));
        sb.append(String.format("| %-30s | %-27s |\n", "Clasificacion", clasif));
        sb.append(String.format("| %-30s | %-27s |\n", "Alcance", alcance));
        sb.append(String.format("| %-30s | %-27s |\n", "Origen de generacion", origen));
        sb.append(String.format("| %-30s | %-27s |\n", "Series temporales (#)", String.valueOf(seriesCount)));
        sb.append("+---------------------------------------------------------------+\n");

        return sb.toString();
    }
}