================================================================================
EDUCATE - PLATAFORMA EDUCATIVA SOLIDARIA (ODS 4 Y ODS 17)
================================================================================

INFORMACION GENERAL
--------------------------------------------------------------------------------
Nombre de la aplicacion : EDUcate
Plataforma              : Android Nativo
Lenguaje                : Kotlin 2.2.10
Interfaz de Usuario     : Jetpack Compose (Material 3)
Compatibilidad Minima   : Android 7.0 Nougat (API 24)
Compatibilidad Objetivo : Android 15 / API 37
Arquitectura            : MVVM (Model-View-ViewModel) + Repositorio Hibrido
Licencia                : MIT


PROPOSITO Y VISION
--------------------------------------------------------------------------------
EDUcate es una aplicacion movil creada para reducir la brecha educativa y
facilitar el acceso libre a formacion tecnica y universitaria de calidad.

El proyecto se encuentra estrictamente alineado con dos Objetivos de Desarrollo
Sostenible (ODS) de la Organizacion de las Naciones Unidas:
  * ODS 4: Educacion de Calidad (garantizar una educacion inclusiva y equitativa).
  * ODS 17: Alianzas para lograr los Objetivos (fomentar la colaboracion solidaria
    entre estudiantes, docentes voluntarios y donantes altruistas).


MODULOS Y FUNCIONALIDADES
--------------------------------------------------------------------------------

1. MODULO DE ESTUDIANTE:
   * Panel de control (Dashboard): Resumen con porcentaje de progreso, cursos
     activos, tareas pendientes, promedio general y avisos importantes.
   * Catalogo e inscripcion: Explorador de cursos gratuitos categorizados por
     nivel y duracion, con inscripcion directa e integracion instantanea al inicio.
   * Contenido y modulos: Desglose del curso por semanas o unidades de
     aprendizaje colapsables y expandibles.
   * Lector de lecciones: Visualizacion de lecturas, descarga de recursos y guias
     en formato PDF, y participacion comunitaria en foros con sistema de likes.
   * Entrega de actividades: Selector de archivos locales desde el dispositivo
     movil con carga multipart hacia el servidor.
   * Calificaciones y analiticas: Historial detallado de notas obtenidas sobre
     puntajes maximos con retroalimentacion personalizada del docente.
   * Agenda academica: Cronograma interactivo con filtros para examenes, tareas,
     tutorias y foros con fechas limite.

2. MODULO DE PROFESOR VOLUNTARIO:
   * Panel de control docente: Metricas de cursos impartidos, alumnos activos
     inscritos y total de actividades pendientes de evaluar.
   * Bandeja de revision y calificacion: Dialogo interactivo para calificar
     entregas asignando nota numerica y comentarios de retroalimentacion.
   * Creador de modulos: Herramienta agil para que los profesores publiquen
     nuevas semanas de estudio y asignen actividades con fecha y puntaje.

3. MODULO SOLIDARIO (ODS 17):
   * Donaciones transparentes: Modulo para aportar montos predefinidos ($50,
     $100, $250, $500 MXN) o un valor personalizado para el fondo de becas.
   * Comprobante digital: Generacion automatica de recibo de donacion con
     folio unico de transaccion.
   * Convocatoria de voluntariado: Formulario de postulacion rapida para que
     nuevos profesionales se sumen como profesores voluntarios.


ARQUITECTURA DE SOFTWARE
--------------------------------------------------------------------------------
La aplicacion implementa el patron oficial MVVM (Model - View - ViewModel)
con flujo de datos unidireccional (UDF - Unidirectional Data Flow):

  1. Capa de Presentacion (UI):
     Pantallas y componentes modulares construidos 100% en Jetpack Compose
     con el sistema de diseno Material 3.
  2. Capa de Negocio / Estado (ViewModel):
     AppViewModel centraliza el estado en un unico StateFlow (AppUiState),
     garantizando una unica fuente de la verdad para toda la aplicacion.
  3. Capa de Datos (Repository con Fallback Inteligente):
     AppRepository intenta consumir el backend mediante el cliente Retrofit.
     Si el servidor no responde o el dispositivo esta sin conexion, conmuta
     de forma automatica e inadvertida a un almacenamiento en memoria local.
     Esto asegura que la aplicacion siempre sea 100% interactiva para demos,
     pruebas de campo y revisiones de diseno.


ESTRUCTURA DEL PROYECTO
--------------------------------------------------------------------------------
EDUcate/
|-- gradle/
|   `-- libs.versions.toml             (Catalogo centralizado de dependencias)
|-- app/
|   |-- build.gradle.kts               (Configuracion de compilacion y SDKs)
|   `-- src/
|       `-- main/
|           |-- AndroidManifest.xml    (Permisos e intent filters)
|           |-- java/com/example/educate/
|           |   |-- MainActivity.kt    (Actividad principal y NavHost)
|           |   |-- data/
|           |   |   |-- api/
|           |   |   |   |-- ApiClient.kt          (Cliente Retrofit + OkHttp)
|           |   |   |   `-- EducateApiService.kt  (Endpoints REST)
|           |   |   |-- model/
|           |   |   |   `-- Models.kt             (DTOs y modelos de dominio)
|           |   |   `-- repository/
|           |   |       `-- AppRepository.kt      (Repositorio red + fallback)
|           |   `-- ui/
|           |       |-- components/
|           |       |   `-- BottomNavBar.kt       (Barra de navegacion inferior)
|           |       |-- navigation/
|           |       |   `-- ScreenRoutes.kt       (Rutas selladas de navegacion)
|           |       |-- screens/
|           |       |   |-- LoginScreen.kt              (P-01: Autenticacion)
|           |       |   |-- StudentHomeScreen.kt        (P-02: Inicio alumno)
|           |       |   |-- CourseDetailScreen.kt       (P-03: Detalle cursos)
|           |       |   |-- ActivitySubmissionScreen.kt (P-04: Envio tareas)
|           |       |   |-- GradesAnalyticsScreen.kt    (P-05: Calificaciones)
|           |       |   |-- ExploreCoursesScreen.kt     (P-06: Catalogo)
|           |       |   |-- LessonPlayerScreen.kt       (P-07: Lector leccion)
|           |       |   |-- TeacherDashboardScreen.kt   (P-08: Panel docente)
|           |       |   |-- DonationVolunteerScreen.kt  (P-09: Donaciones/ODS17)
|           |       |   `-- AgendaScreen.kt             (Cronograma academico)
|           |       |-- theme/
|           |       |   |-- Color.kt              (Paleta de colores)
|           |       |   |-- Theme.kt              (Tema Material 3)
|           |       |   `-- Type.kt               (Tipografia)
|           |       `-- viewmodel/
|           |           `-- AppViewModel.kt       (ViewModel y AppUiState)
|           `-- res/                              (Recursos visuales y cadenas)
|-- README.md                          (Documentacion en formato Markdown)
`-- README.txt                         (Documentacion en texto plano)


TECNOLOGIAS Y LIBRERIAS
--------------------------------------------------------------------------------
Componente                  Version        Finalidad
--------------------------------------------------------------------------------
Kotlin                      2.2.10         Lenguaje principal
Android Gradle Plugin       9.5.0          Automatizacion de compilacion
Jetpack Compose BOM         2026.02.01     UI declarativa reactiva
Material 3                  BOM            Diseno visual Material You
Navigation Compose          2.8.8          Enrutamiento entre pantallas
Lifecycle / ViewModel       2.11.0         Gestion de ciclo de vida y estado
Retrofit                    2.11.0         Cliente HTTP tipado
OkHttp / Logging            4.12.0         Capa de conexion y logs de red
Gson                        2.10.1         Serializacion de datos JSON


ENDPOINTS DE LA API REST
--------------------------------------------------------------------------------
Metodo   Ruta                                     Descripcion
--------------------------------------------------------------------------------
POST     /api/v1/auth/login                       Inicio de sesion de usuario
GET      /api/v1/cursos/estudiante/{id}           Dashboard y cursos del estudiante
POST     /api/v1/actividades/entregar             Subida multipart de actividad
GET      /api/v1/calificaciones/{id}              Historial de calificaciones
GET      /api/v1/profesor/panel/{id}              Metricas del profesor voluntario
POST     /api/v1/profesor/calificar               Asignar nota y retroalimentacion
POST     /api/v1/donaciones/procesar              Procesar donacion para fondo ODS17

Nota de configuracion de red:
Por defecto ApiClient apunta a "http://10.0.2.2:8000/" para interactuar con un
servidor backend local desde el Emulador oficial de Android. Si se ejecuta en
un telefono fisico por WiFi, se puede invocar ApiClient.updateBaseUrl con la IP
privada del equipo host.


REQUISITOS DEL ENTORNO
--------------------------------------------------------------------------------
1. JDK: Java Development Kit 11 o 17.
2. Android Studio: Version Koala, Ladybug, Meerkat o superior.
3. Android SDK:
   - compileSdk = 37
   - targetSdk  = 37
   - minSdk     = 24 (Compatible con Android 7.0 y posteriores)


EJECUCION Y COMPILACION DESDE CONSOLA
--------------------------------------------------------------------------------
Para compilar el APK de depuracion:
  ./gradlew assembleDebug

Para compilar e instalar directamente en un dispositivo o emulador:
  ./gradlew installDebug

Para ejecutar las pruebas unitarias:
  ./gradlew test


USUARIOS DEMO PRECONFIGURADOS
--------------------------------------------------------------------------------
Rol: Estudiante
  - Correo      : ana.lopez@universidad.edu
  - Contrasena  : (Cualquiera)
  - Nombre      : Ana Lopez
  - Permisos    : Ver cursos, entregar tareas, revisar notas, consultar agenda.

Rol: Profesor Voluntario
  - Correo      : elena.ruiz@universidad.edu
  - Contrasena  : (Cualquiera)
  - Nombre      : Dra. Elena Ruiz
  - Permisos    : Revisar entregas, calificar con feedback y crear modulos.

Tambien se puede alternar de perfil inmediatamente utilizando el boton de
cambio de rol disponible en la barra superior de la aplicacion o en la pantalla
de inicio de sesion.
