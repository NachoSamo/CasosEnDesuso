# Red Sísmica – PPAI 2025

**Proyecto Práctico de Aplicación Integrador – UTN FRC / FRVM**
**Materia:** Diseño de Sistemas de Información
**Grupo:** Casos en Desuso

---

## 💡 Objetivo del Proyecto

Implementar en Java el caso de uso **CU 23: Registrar resultado de revisión manual** del sistema Red Sísmica. Esta funcionalidad permite al **Analista en Sismos** revisar eventos sísmicos autodetectados y registrar el resultado: **confirmar**, **rechazar** o **derivar a experto**.

Incluye:

* Modelado UML: clases de análisis, diagrama de secuencia, diagrama de estados
* Implementación orientada a objetos en Java 17+
* Interfaz gráfica desarrollada con **JavaFX 24.0.1**
* Carga simulada de datos desde objetos en memoria

---

## 🔧 Requisitos para ejecutar el proyecto

* JDK 17 o superior (se recomienda JDK 21 o 24)
* SDK de JavaFX instalado (ej: `javafx-sdk-24.0.1`)
* IntelliJ IDEA, NetBeans o similar

> Este proyecto **no usa Maven ni Gradle**, solo Java puro

---

## 📂 Estructura del Proyecto

```
repo/
├── src/
│   ├── boundary/               # Pantalla principal (JavaFX) y Main
│   ├── controlador/            # Lógica de control (pendiente de expansión)
│   ├── entidades/              # Clases del dominio (EventoSismico, Estado, etc.)
│   ├── fxml/                   # Archivos .fxml de la interfaz
│   └── persistence/            # Simulación de acceso a datos (si aplica)
├── .idea/ (ignorar)
├── out/   (ignorar)
├── target/ (ignorar)
├── README.md
└── .gitignore
```

---

## ✨ Instrucciones para ejecutar

### 1. Clonar el repositorio

```bash
git clone https://github.com/<usuario>/<repo>.git
cd <repo>
```

### 2. Configurar IntelliJ IDEA

1. Abrir el proyecto desde `File > Open...` y seleccionar la carpeta `repo`
2. Asegurarse que `src/` esté marcada como `Sources Root`
3. Ir a `File > Project Structure > SDK` y seleccionar tu JDK (>= 17)
4. Ir a `Run > Edit Configurations` y crear una nueva configuración de tipo **Application**:

    * **Main class:** `boundary.Main`

    * **VM options:**

      ```
      --module-path "C:\Programas\javaFx\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml
      ```

    * Working directory: carpeta del proyecto

### 3. Ejecutar

Presionar **Shift + F10** o el botón verde de "Run". Se abrirá la interfaz JavaFX.

---

## 📊 Funcionalidades implementadas

* Visualización de eventos sísmicos cargados desde una lista
* Acciones disponibles: Ver casos, Registrar nuevo caso, Actualizar estado, Generar reporte
* Interfaz gráfica con botones, tabla, y cuadros de diálogo
* Arquitectura pensada para expandirse con persistencia real (SQLite / JDBC)

---

## 🔒 .gitignore sugerido

```
# IntelliJ
.idea/
*.iml
out/

# NetBeans
nbproject/

# Sistema operativo
.DS_Store
Thumbs.db

# Otros
*.log
*.db
```

---

## ✅ Estado del desarrollo

| Módulo                   | Estado        |
| ------------------------ | ------------- |
| Diagrama de clases UML   | ✅ Completo    |
| Diagrama de secuencia    | ✅ Completo    |
| Diagrama de estados      | ✅ Completo    |
| Interfaz JavaFX          | ✅ Funcional   |
| Conexión a base de datos | ❌ Simulada    |
| CU 23 implementado       | ✅ En progreso |

---

## 📕 Historial de entregas

| Fecha      | Entrega                  | Descripción                            |
| ---------- | ------------------------ | -------------------------------------- |
| 2025-05-27 | Versión base funcional   | JavaFX operativo + estructura completa |
| 2025-05-30 | Entrega intermedia       | Interacción con controlador y flujo CU |
| 2025-06-04 | Entrega final (esperada) | Flujo completo CU23 + presentación     |

---
