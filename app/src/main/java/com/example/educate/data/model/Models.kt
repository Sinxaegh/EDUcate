package com.example.educate.data.model

import com.google.gson.annotations.SerializedName

// --- API REQUEST / RESPONSE MODELS ---

data class LoginRequest(
    @SerializedName("correo") val correo: String,
    @SerializedName("contrasena") val contrasena: String
)

data class RegisterRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("contrasena") val contrasena: String,
    @SerializedName("rol") val rol: String
)

data class UsuarioResponse(
    @SerializedName("id_usuario") val idUsuario: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("rol") val rol: String, // "estudiante" o "profesor"
    @SerializedName("token") val token: String
)

data class DashboardSummary(
    @SerializedName("cursos_activos") val cursosActivos: Int,
    @SerializedName("pendientes") val pendientes: Int,
    @SerializedName("promedio_actual") val promedioActual: Double
)

data class CourseDto(
    @SerializedName("id_curso") val idCurso: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("docente") val docente: String,
    @SerializedName("avance_porcentaje") val avancePorcentaje: Int
)

data class NoticeDto(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("fecha") val fecha: String
)

data class StudentDashboardResponse(
    @SerializedName("resumen") val resumen: DashboardSummary,
    @SerializedName("cursos") val cursos: List<CourseDto>,
    @SerializedName("avisos") val avisos: List<NoticeDto>
)

data class ActividadEntregaResponse(
    @SerializedName("id_entrega") val idEntrega: Int,
    @SerializedName("id_estudiante") val idEstudiante: Int,
    @SerializedName("id_actividad") val idActividad: Int,
    @SerializedName("archivo_nombre") val archivoNombre: String,
    @SerializedName("sello_tiempo") val selloTiempo: String,
    @SerializedName("estado") val estado: String
)

data class CalificacionDto(
    @SerializedName("actividad") val actividad: String,
    @SerializedName("puntos") val puntos: Double?,
    @SerializedName("maximo") val maximo: Double,
    @SerializedName("estado") val estado: String
)

data class CalificacionesResponse(
    @SerializedName("id_estudiante") val idEstudiante: Int,
    @SerializedName("promedio_general") val promedioGeneral: Double,
    @SerializedName("calificaciones") val calificaciones: List<CalificacionDto>
)

data class PendienteRevisionDto(
    @SerializedName("id_entrega") val idEntrega: Int,
    @SerializedName("estudiante") val estudiante: String,
    @SerializedName("actividad") val actividad: String,
    @SerializedName("hora") val hora: String
)

data class TeacherPanelResponse(
    @SerializedName("docente") val docente: String,
    @SerializedName("cursos_impartidos") val cursosImpartidos: Int,
    @SerializedName("alumnos_activos") val alumnosActivos: Int,
    @SerializedName("tareas_por_revisar") val tareasPorRevisar: Int,
    @SerializedName("pendientes_revision") val pendientesRevision: List<PendienteRevisionDto>
)

data class CalificacionRequest(
    @SerializedName("id_entrega") val idEntrega: Int,
    @SerializedName("puntuacion") val puntuacion: Double,
    @SerializedName("retroalimentacion") val retroalimentacion: String
)

data class DonacionRequest(
    @SerializedName("monto") val monto: Double,
    @SerializedName("correo") val correo: String,
    @SerializedName("metodo") val metodo: String
)

data class DonacionResponse(
    @SerializedName("estatus") val estatus: String,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("monto") val monto: Double,
    @SerializedName("correo") val correo: String,
    @SerializedName("transaccion_id") val transaccionId: String
)

// --- APP UI DOMAIN MODELS ---

enum class ItemType {
    LECTURA, FORO, TAREA
}

data class LessonResource(
    val id: Int,
    val title: String,
    val info: String,
    val size: String
)

data class ModuleLesson(
    val id: Int,
    val title: String,
    val type: ItemType,
    val subtitle: String,
    val isPending: Boolean = false,
    val dueDate: String? = null,
    val points: Int? = null,
    val duration: String? = null
)

data class CourseModule(
    val id: Int,
    val title: String,
    val statusText: String,
    val isCompleted: Boolean = false,
    val isExpanded: Boolean = false,
    val lessons: List<ModuleLesson> = emptyList()
)

data class CourseCatalogItem(
    val id: Int,
    val title: String,
    val instructor: String,
    val level: String,
    val duration: String,
    val category: String,
    val isFree: Boolean = true,
    val isVolunteer: Boolean = true
)

data class UpcomingDate(
    val dayNumber: String,
    val monthText: String,
    val title: String,
    val course: String,
    val timeText: String
)

data class ForumPost(
    val id: Int,
    val author: String,
    val authorRole: String, // "Profesor" o "Estudiante"
    val avatarInitials: String,
    val date: String,
    val content: String,
    val likesCount: Int = 0
)

data class AgendaItem(
    val id: Int,
    val title: String,
    val course: String,
    val dayNumber: String,
    val monthText: String,
    val timeText: String,
    val type: String, // "Tarea", "Foro", "Tutoría", "Examen"
    val isPending: Boolean = true,
    val points: Int? = null
)

data class LessonData(
    val id: Int = 201,
    val title: String = "Semana 2: Análisis descriptivo",
    val course: String = "Estadística aplicada · Dra. Elena Ruiz",
    val youtubeVideoId: String = "sb7-230Bgdc",
    val durationText: String = "18 min",
    val weekText: String = "Semana 2 de 6",
    val summaryText: String = "Aprende a resumir e interpretar datos con medidas de tendencia central (media, mediana) y dispersión.",
    val transcriptText: String = "Bienvenidos a la lección. Hoy analizaremos las medidas de tendencia central y cómo calcular la media, mediana y desviación estándar...",
    val resources: List<LessonResource> = listOf(
        LessonResource(1, "Guía de estadística descriptiva", "PDF · 12 páginas", "1.4 MB"),
        LessonResource(2, "Ejercicios de práctica", "PDF · 4 páginas", "580 KB")
    )
)

