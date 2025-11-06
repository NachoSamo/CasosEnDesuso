-- Flyway migration V1: schema and seed data for red_sismica (H2 compatible)

-- 1) CATÁLOGOS
CREATE TABLE IF NOT EXISTS estados_evento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS clasificaciones_sismos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS alcances_sismos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS origenes_generacion (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

-- 2) ACTORES
CREATE TABLE IF NOT EXISTS empleados (
    id VARCHAR(36) PRIMARY KEY,
    usuario_id VARCHAR(100),
    rol_empleado_id INT
);

-- 3) CORE
CREATE TABLE IF NOT EXISTS eventos_sismicos (
    id VARCHAR(36) PRIMARY KEY,
    fecha_ocurrencia TIMESTAMP NOT NULL,
    magnitud DOUBLE,
    epicentro_lat DOUBLE,
    epicentro_lng DOUBLE,
    hipocentro_km DOUBLE,
    estado_actual VARCHAR(100) NOT NULL,
    clasificacion_id INT,
    alcance_id INT,
    origen_id INT,
    auto_confirmado BOOLEAN DEFAULT FALSE,
    cerrado_en TIMESTAMP,
    CONSTRAINT fk_evento_clasif FOREIGN KEY (clasificacion_id) REFERENCES clasificaciones_sismos(id),
    CONSTRAINT fk_evento_alcance FOREIGN KEY (alcance_id) REFERENCES alcances_sismos(id),
    CONSTRAINT fk_evento_origen FOREIGN KEY (origen_id) REFERENCES origenes_generacion(id)
);

CREATE INDEX IF NOT EXISTS ix_eventos_estado_fecha ON eventos_sismicos (estado_actual, fecha_ocurrencia);

-- 4) HISTORIAL
CREATE TABLE IF NOT EXISTS eventos_historial_estado (
    id INT PRIMARY KEY AUTO_INCREMENT,
    evento_id VARCHAR(36) NOT NULL,
    estado_id INT NOT NULL,
    usuario_id VARCHAR(36),
    fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    motivo VARCHAR(4000),
    CONSTRAINT fk_hist_evento FOREIGN KEY (evento_id) REFERENCES eventos_sismicos(id) ON DELETE CASCADE,
    CONSTRAINT fk_hist_estado FOREIGN KEY (estado_id) REFERENCES estados_evento(id)
);

CREATE INDEX IF NOT EXISTS ix_historial_evento_fecha ON eventos_historial_estado (evento_id, fecha_hora);

-- 5) BLOQUEOS
CREATE TABLE IF NOT EXISTS eventos_bloqueos_revision (
    evento_id VARCHAR(36) PRIMARY KEY,
    analista_id VARCHAR(36) NOT NULL,
    tomado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    liberado_en TIMESTAMP,
    CONSTRAINT fk_bloq_evento FOREIGN KEY (evento_id) REFERENCES eventos_sismicos(id) ON DELETE CASCADE,
    CONSTRAINT fk_bloq_analista FOREIGN KEY (analista_id) REFERENCES empleados(id)
);

-- 6) DERIVACIONES
CREATE TABLE IF NOT EXISTS eventos_derivaciones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    evento_id VARCHAR(36) NOT NULL,
    de_analista_id VARCHAR(36) NOT NULL,
    a_supervisor_id VARCHAR(36) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    motivo VARCHAR(2000),
    estado VARCHAR(50) NOT NULL DEFAULT 'pendiente',
    CONSTRAINT fk_der_evento FOREIGN KEY (evento_id) REFERENCES eventos_sismicos(id) ON DELETE CASCADE,
    CONSTRAINT fk_der_de FOREIGN KEY (de_analista_id) REFERENCES empleados(id),
    CONSTRAINT fk_der_a FOREIGN KEY (a_supervisor_id) REFERENCES empleados(id)
);

CREATE INDEX IF NOT EXISTS ix_derivaciones_evento ON eventos_derivaciones (evento_id, fecha_hora);

-- 7) DATOS SEMILLA
INSERT INTO estados_evento (codigo) VALUES
('auto_detectado'), ('bloqueado_en_revision'), ('pendiente_revision'),
('derivado'), ('confirmado'), ('rechazado'),
('anulado'), ('pendiente_cierre'), ('cerrado');

INSERT INTO clasificaciones_sismos (nombre) VALUES ('Tectónico'), ('Volcánico'), ('Artificial');
INSERT INTO alcances_sismos (nombre) VALUES ('Local'), ('Regional'), ('Lejano');
INSERT INTO origenes_generacion (nombre) VALUES ('Natural'), ('Inducido'), ('Desconocido');
