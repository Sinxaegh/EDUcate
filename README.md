# EDUcate — Plataforma Educativa Solidaria (ODS 17 & ODS 4)

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24%20(Nougat)-orange?style=for-the-badge)](https://developer.android.com/about/dashboards)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-37-blue?style=for-the-badge)](https://developer.android.com/)
[![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](LICENSE)

> **EDUcate** es una aplicación móvil nativa para Android diseñada para democratizar el acceso a la educación superior y de calidad mediante alianzas colaborativas entre estudiantes, docentes voluntarios y donantes. La plataforma está alineada con el **ODS 4 (Educación de Calidad)** y el **ODS 17 (Alianzas para lograr los Objetivos)** de las Naciones Unidas.

---

## Tabla de Contenidos

- [Propósito y Visión](#-propósito-y-visión)
- [Características Principales](#-características-principales)
  - [Módulo de Estudiante](#1-módulo-de-estudiante)
  - [Módulo de Profesor Voluntario](#2-módulo-de-profesor-voluntario)
  - [Módulo Solidario (ODS 17)](#3-módulo-solidario-ods-17)
- [Arquitectura de Software](#-arquitectura-de-software)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Tecnologías y Librerías](#-tecnologías-y-librerías)
- [API REST y Modo Híbrido / Offline](#-api-rest-y-modo-híbrido--offline)
- [Requisitos de Instalación](#-requisitos-de-instalación)
- [Ejecución y Despliegue](#-ejecución-y-despliegue)
- [Usuarios Demo para Pruebas](#-usuarios-demo-para-pruebas)

---

## Propósito y Visión

La educación es un motor fundamental para el desarrollo humano, sin embargo, millones de jóvenes enfrentan barreras económicas y de acceso a contenidos universitarios y técnicos de calidad.

**EDUcate** surge como una solución tecnológica y social que:
1. **Conecta estudiantes** de cualquier región con cursos gratuitos y recursos pedagógicos abiertos.
2. **Fomenta el voluntariado docente**, permitiendo que profesionales y profesores experimentados donen tiempo académico para evaluar actividades y orientar alumnos.
3. **Garantiza la sostenibilidad transparente (ODS 17)** mediante un canal de microdonaciones y comprobantes digitales directos para financiar infraestructura y fondos de becas.

---

## Características Principales

### 1. Módulo de Estudiante
- **Panel de Control (Dashboard)**: Métricas en tiempo real de cursos activos, tareas pendientes, promedio de calificaciones ponderado y notificaciones de avisos docentes.
- **Explorador y Catálogo de Cursos**: Búsqueda e inscripción inmediata a cursos sin costo con categorización por dificultad y duración.
- **Detalle del Curso y Módulos**: Visualización por semanas o unidades de aprendizaje colapsables y expandibles.
- **Lector de Lecciones y Recursos**: Acceso a lecturas estructuradas, visualización de material en PDF, ejercicios prácticos y foro de debate estudiantil integrado.
- **Entrega de Actividades**: Formulario para cargar documentos o entregas desde el almacenamiento del dispositivo con soporte para envíos *Multipart*.
- **Historial de Calificaciones y Analíticas**: Registro pormenorizado de evaluaciones, puntuación obtenida vs. puntaje máximo y retroalimentación docente.
- **Agenda Académica**: Calendario interactivo con recordatorios y fechas límite de entregas, foros y exámenes.

### 2. Módulo de Profesor Voluntario
- **Panel Docente**: Estadísticas sobre cursos impartidos, número total de estudiantes inscritos y cantidad de tareas pendientes de revisión.
- **Revisión y Calificación**: Bandeja de entregas recibidas de estudiantes con diálogo interactivo para asignar notas numéricas y registrar retroalimentaciones constructivas.
- **Gestión de Contenidos**: Creación dinámica de nuevos módulos temáticos y asignación de tareas con fechas límite.

### 3. Módulo Solidario (ODS 17)
- **Donaciones Seguras**: Selector de montos predefinidos (\$50, \$100, \$250, \$500 MXN) o cantidad personalizada, con selección de método de pago.
- **Comprobante Digital**: Generación de recibo digital de aportación con folio de transacción único (`txn_educate_...`).
- **Postulación de Voluntariado**: Formulario y bienvenida guiada para profesionales interesados en sumarse a la comunidad docente.

---

## Arquitectura de Software

La aplicación sigue el patrón arquitectónico oficial recomendado por Google: **MVVM (Model - View - ViewModel)** combinado con principios de **Clean Architecture** y flujo de datos unidireccional (**UDF - Unidirectional Data Flow**).

```
   ┌────────────────────────────────────────────────────────┐
   │                       UI LAYER                         │
   │  Jetpack Compose Screens & Reusable Components        │
   │  (StudentHome, CourseDetail, ActivitySubmission, etc.) │
   └───────────────────────────▲────────────────────────────┘
                               │ StateFlow (AppUiState)
                               │ Events (Actions / Callbacks)
   ┌───────────────────────────▼────────────────────────────┐
   │                    VIEWMODEL LAYER                     │
   │  AppViewModel                                          │
   │  - Gestión de Estado Centralizado (AppUiState)         │
   │  - Coroutines (viewModelScope)                         │
   └───────────────────────────▲────────────────────────────┘
                               │ Invocación de casos de uso / repo
                               │ Resultados encapsulados (Result<T>)
   ┌───────────────────────────▼────────────────────────────┐
   │                    DATA & NETWORK                      │
   │  AppRepository                                         │
   │  ├── Retrofit ApiClient (EducateApiService) ──► REST   │
   │  └── Mock / In-Memory Store (Resiliencia Offline)      │
   └────────────────────────────────────────────────────────┘
```

- **UI Declarativa**: Toda la interfaz de usuario está construida 100% en Jetpack Compose con componentes Material 3.
- **Single Source of Truth**: El estado de la aplicación se centraliza en `AppViewModel` exponiendo un único `StateFlow<AppUiState>`.
- **Patrón Repository con Fallback Inteligente**: La capa `AppRepository` intenta primero consumir la API REST remota. Si no hay conexión o el servidor está inactivo, activa automáticamente un fallback local en memoria para garantizar que la app sea siempre navegable y funcional en modo demo.

---

## Estructura del Proyecto

```text
EDUcate/
├── gradle/
│   └── libs.versions.toml              # Catálogo de dependencias centralizado
├── app/
│   ├── build.gradle.kts                # Configuración de compilación, SDKs y plugins
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml     # Permisos e intent filters
│           ├── java/com/example/educate/
│           │   ├── MainActivity.kt     # Actividad principal y orquestador NavHost
│           │   ├── data/
│           │   │   ├── api/
│           │   │   │   ├── ApiClient.kt          # Cliente HTTP Retrofit + OkHttp
│           │   │   │   └── EducateApiService.kt  # Definición de endpoints REST
│           │   │   ├── model/
│           │   │   │   └── Models.kt             # DTOs, entidades de dominio y peticiones
│           │   │   └── repository/
│           │   │       └── AppRepository.kt      # Repositorio híbrido (Red + Fallback local)
│           │   └── ui/
│           │       ├── components/
│           │       │   └── BottomNavBar.kt       # Barra de navegación inferior
│           │       ├── navigation/
│           │       │   └── ScreenRoutes.kt       # Rutas y destinos sellados (Screen)
│           │       ├── screens/
│           │       │   ├── LoginScreen.kt              # P-01: Autenticación y roles
│           │       │   ├── StudentHomeScreen.kt        # P-02: Inicio del estudiante
│           │       │   ├── CourseDetailScreen.kt       # P-03: Módulos y contenidos
│           │       │   ├── ActivitySubmissionScreen.kt # P-04: Carga de tareas
│           │       │   ├── GradesAnalyticsScreen.kt    # P-05: Calificaciones y analíticas
│           │       │   ├── ExploreCoursesScreen.kt     # P-06: Catálogo de cursos
│           │       │   ├── LessonPlayerScreen.kt       # P-07: Reproductor de lecciones
│           │       │   ├── TeacherDashboardScreen.kt   # P-08: Panel docente voluntario
│           │       │   ├── DonationVolunteerScreen.kt  # P-09: Donaciones y ODS 17
│           │       │   └── AgendaScreen.kt             # Cronograma de actividades
│           │       ├── theme/
│           │       │   ├── Color.kt              # Paleta de colores institucional
│           │       │   ├── Theme.kt              # Tema Material 3 EDUcate
│           │       │   └── Type.kt               # Tipografía y estilos de texto
│           │       └── viewmodel/
│           │           └── AppViewModel.kt       # ViewModel principal y AppUiState
│           └── res/                              # Iconos vectoriales, mipmaps y recursos
└── README.md
```

---

## 🛠 Tecnologías y Librerías

| Tecnología | Versión | Propósito |
|---|---|---|
| **Kotlin** | `2.2.10` | Lenguaje de programación principal |
| **Android Gradle Plugin (AGP)** | `9.5.0` | Herramienta de automatización de compilación |
| **Jetpack Compose BOM** | `2026.02.01` | Kit de herramientas de UI declarativa moderna |
| **Material 3** | Última (BOM) | Sistema de diseño de interfaces (*Material You*) |
| **Navigation Compose** | `2.8.8` | Navegación nativa entre pantallas composables |
| **Lifecycle / ViewModel** | `2.11.0` | Gestión del ciclo de vida y retención de estado |
| **Retrofit** | `2.11.0` | Cliente REST HTTP tipado para Android |
| **OkHttp & Logging Interceptor** | `4.12.0` | Capa de red, conexión y depuración HTTP |
| **Gson** | `2.10.1` | Serialización y deserialización de JSON |
| **Coroutines & Flow** | Integrado | Concurrencia asíncrona reactiva y flujos de datos |

---

## API REST y Modo Híbrido / Offline

La app está diseñada para conectarse a un servicio backend REST mediante las siguientes rutas:

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/v1/auth/login` | Autenticación de usuario con correo y contraseña |
| `GET` | `/api/v1/cursos/estudiante/{id_estudiante}` | Obtiene el dashboard y cursos del alumno |
| `POST` | `/api/v1/actividades/entregar` | Envío de archivos de actividad (Multipart FormData) |
| `GET` | `/api/v1/calificaciones/{id_estudiante}` | Consulta de historial de notas y promedio |
| `GET` | `/api/v1/profesor/panel/{id_profesor}` | Métricas y tareas por revisar del docente |
| `POST` | `/api/v1/profesor/calificar` | Envía calificación numérica y retroalimentación |
| `POST` | `/api/v1/donaciones/procesar` | Registra una donación voluntaria para el fondo ODS 17 |

> [!NOTE]
> **Configuración del host**: Por defecto, `ApiClient.kt` apunta a `http://10.0.2.2:8000/` (el alias estándar que apunta al `localhost` de la máquina host desde el Emulador de Android). Si pruebas en un dispositivo físico, puedes modificar la URL base invocando `ApiClient.updateBaseUrl("http://tu-ip-local:8000/")`.

---

## Requisitos de Instalación

1. **Java Development Kit (JDK)**: JDK 11 o JDK 17 configurado en el entorno.
2. **Android Studio**: Android Studio Koala / Ladybug / Meerkat o superior.
3. **Android SDK**:
   - `compileSdk`: 37
   - `targetSdk`: 37
   - `minSdk`: 24 (Compatible con Android 7.0 y versiones superiores)

---

## Ejecución y Despliegue

### Desde Android Studio
1. Clona el directorio del proyecto en Android Studio
2. Espera a que Gradle termine de sincronizar las dependencias (`Sync Project with Gradle Files`).
3. Selecciona un dispositivo virtual (AVD) o un teléfono físico con depuración USB habilitada.
4. Presiona el botón **Run** (`Shift + F10`).

### Desde la Consola (Gradle Wrapper)
- **Compilar APK en modo Debug**:
  ```bash
  ./gradlew assembleDebug
  ```
- **Instalar en un emulador o dispositivo conectado**:
  ```bash
  ./gradlew installDebug
  ```
- **Ejecutar pruebas unitarias**:
  ```bash
  ./gradlew test
  ```

---

## Usuarios Demo para Pruebas

Para facilitar la navegación y evaluación de la aplicación sin necesidad de una base de datos externa, se encuentran preconfiguradas las siguientes cuentas:

| Rol | Correo Electrónico | Contraseña | Nombre | Funcionalidades Clave |
|---|---|---|---|---|
| **Estudiante** | `ana.lopez@universidad.edu` | *(Cualquiera)* | Ana López | Cursos activos, entrega de tareas, agenda, calificaciones y donaciones. |
| **Profesor Voluntario** | `elena.ruiz@universidad.edu` | *(Cualquiera)* | Dra. Elena Ruiz | Panel docente, revisión de entregas pendientes, calificación y creación de módulos. |

> [!TIP]
> También puedes alternar rápidamente de rol en cualquier momento desde el diálogo de usuario en el encabezado superior o desde la pantalla de login mediante los selectores rápidos.

---

## Licencia

Este proyecto se distribuye bajo la licencia **MIT**. Consulta el archivo `LICENSE` para más detalles.
