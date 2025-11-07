package entidades;

import entidades.estadoPadreAbstracto.EstadoES;
import entidades.estadosConcretos.AutoDetectado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "eventos_sismicos")
public class EventoSismico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime fechaHoraFin;
    private LocalDateTime fechaHoraOcurrencia;
    private String latitudEpicentro;
    private String longitudEpicentro;
    private String latitudHipocentro;
    private String longitudHipocentro;
    private double valorMagnitud;
    private LocalDateTime fechaHoraRevision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_revision_id")
    private Empleado responsableRevision;

    @OneToMany(mappedBy = "eventoSismico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CambioEstadoES> cambiosEstado = new ArrayList<>();

    @Transient
    private EstadoES estado;

    @Column(name = "estado_actual_class_name") // Se guarda el nombre de la clase del estado.
    private String estadoActualClassName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "nombre", column = @Column(name = "clasificacion_nombre")),
            @AttributeOverride(name = "kmProfundidadDesde", column = @Column(name = "clasificacion_profundidad_desde")),
            @AttributeOverride(name = "kmProfundidadHasta", column = @Column(name = "clasificacion_profundidad_hasta"))
    })
    private ClasificacionSismo clasificacionSismo;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "nombre", column = @Column(name = "alcance_nombre")),
            @AttributeOverride(name = "descripcion", column = @Column(name = "alcance_descripcion"))
    })
    private AlcanceSismo alcanceSismo;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "nombre", column = @Column(name = "origen_nombre")),
            @AttributeOverride(name = "descripcion", column = @Column(name = "origen_descripcion"))
    })
    private OrigenDeGeneracion origenGeneracion;


    @Transient
    private List<SerieTemporal> seriesTemporales = new ArrayList<>();

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
        CambioEstadoES cambioInicial = new CambioEstadoES(LocalDateTime.now(), null, estadoInicial);
        this.agregarCE(cambioInicial);;
    }

    public void revisar(LocalDateTime fh, Empleado resp) {


        this.estado.revisar(this, fh, resp, this.cambiosEstado);
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
        if (this.cambiosEstado == null) {
            this.cambiosEstado = new ArrayList<>();
        }
        this.cambiosEstado.add(nuevoCambio);
        nuevoCambio.setEventoSismico(this); // Importante para la relación bidireccional
    }

    public CambioEstadoES getCambioEstadoActual() {
        if (this.cambiosEstado == null || this.cambiosEstado.isEmpty()) {
            return null;
        }
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

    public void setEstado(EstadoES estado) {
        this.estado = estado;
        if (estado != null) {
            this.estadoActualClassName = estado.getClass().getName();
        } else {
            this.estadoActualClassName = null;
        }
    }

    @PostLoad
    private void rehidratarEstado() {
        if (this.estadoActualClassName != null) {
            try {
                Class<?> clazz = Class.forName(this.estadoActualClassName);
                this.estado = (EstadoES) clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                System.err.println("Error al rehidratar el estado del evento: " + e.getMessage());
                this.estado = null;
            }
        }
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