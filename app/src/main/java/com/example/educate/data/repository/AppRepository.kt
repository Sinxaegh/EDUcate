package com.example.educate.data.repository

import android.content.Context
import android.net.Uri
import com.example.educate.data.api.ApiClient
import com.example.educate.data.model.ActividadEntregaResponse
import com.example.educate.data.model.CalificacionDto
import com.example.educate.data.model.CalificacionRequest
import com.example.educate.data.model.CalificacionesResponse
import com.example.educate.data.model.CourseCatalogItem
import com.example.educate.data.model.CourseDto
import com.example.educate.data.model.CourseModule
import com.example.educate.data.model.DashboardSummary
import com.example.educate.data.model.DonacionRequest
import com.example.educate.data.model.DonacionResponse
import com.example.educate.data.model.ItemType
import com.example.educate.data.model.LessonResource
import com.example.educate.data.model.LessonData
import com.example.educate.data.model.LoginRequest
import com.example.educate.data.model.RegisterRequest
import com.example.educate.data.model.ModuleLesson
import com.example.educate.data.model.NoticeDto
import com.example.educate.data.model.PendienteRevisionDto
import com.example.educate.data.model.StudentDashboardResponse
import com.example.educate.data.model.TeacherPanelResponse
import com.example.educate.data.model.UpcomingDate
import com.example.educate.data.model.UsuarioResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppRepository {

    // --- BASE DE DATOS LOCAL EN MEMORIA (FALLBACK) ---
    private val localRegisteredUsers = mutableMapOf<String, UsuarioResponse>()
    private val localSubmissions = mutableListOf<ActividadEntregaResponse>()
    private val localTeacherSubmissions = mutableListOf(
        PendienteRevisionDto(1, "Ana López", "T2 - Informe descriptivo", "09:15"),
        PendienteRevisionDto(2, "Luis García", "T2 - Informe descriptivo", "08:42")
    )
    private val localGrades = mutableListOf(
        CalificacionDto("T1 - Conceptos básicos", 18.0, 20.0, "Evaluada"),
        CalificacionDto("Cuestionario 1", 16.0, 20.0, "Evaluada"),
        CalificacionDto("Foro 1 - Variables", 9.0, 10.0, "Evaluada"),
        CalificacionDto("T2 - Informe descriptivo", null, 20.0, "Pendiente")
    )

    private val localModules = mutableListOf(
        CourseModule(
            id = 1,
            title = "Semana 1 · Fundamentos",
            statusText = "Completada · 3 recursos",
            isCompleted = true,
            isExpanded = false,
            lessons = listOf(
                ModuleLesson(101, "Introducción a la estadística", ItemType.LECTURA, "Lectura · PDF · 8 páginas"),
                ModuleLesson(102, "Conceptos de variables", ItemType.LECTURA, "Lectura · PDF · 10 páginas"),
                ModuleLesson(103, "Tarea 1 · Conceptos básicos", ItemType.TAREA, "Entregada · Valor: 20 puntos", isPending = false)
            )
        ),
        CourseModule(
            id = 2,
            title = "Semana 2 · Análisis descriptivo",
            statusText = "En curso · 12–18 de octubre",
            isCompleted = false,
            isExpanded = true,
            lessons = listOf(
                ModuleLesson(201, "Guía de estadística descriptiva", ItemType.LECTURA, "Lectura · PDF · 12 páginas"),
                ModuleLesson(202, "Foro · Interpretar los datos", ItemType.FORO, "8 aportes · Cierra el 18 oct, 18:00"),
                ModuleLesson(
                    id = 203,
                    title = "Tarea 2 · Informe descriptivo",
                    type = ItemType.TAREA,
                    subtitle = "16 oct, 23:59 · Valor: 20 puntos",
                    isPending = true,
                    dueDate = "16 oct 2026, 23:59",
                    points = 20
                )
            )
        ),
        CourseModule(
            id = 3,
            title = "Semana 3 · Probabilidad",
            statusText = "Disponible el 19 de octubre",
            isCompleted = false,
            isExpanded = false,
            lessons = emptyList()
        )
    )

    private val localStudentCourses = mutableListOf(
        CourseDto(idCurso = 101, nombre = "Estadística aplicada", docente = "Dra. Elena Ruiz", avancePorcentaje = 67)
    )

    suspend fun register(nombre: String, correo: String, contrasena: String, rol: String): Result<UsuarioResponse> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.register(RegisterRequest(nombre, correo, contrasena, rol))
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                localRegisteredUsers[correo.lowercase()] = user
                return@withContext Result.success(user)
            } else if (response.errorBody() != null) {
                val errStr = response.errorBody()?.string() ?: ""
                val msg = if (errStr.contains("detail")) {
                    errStr.substringAfter("\"detail\":\"").substringBefore("\"")
                } else {
                    "Error al registrar usuario"
                }
                return@withContext Result.failure(Exception(msg))
            }
        } catch (e: Exception) {
            // Error de conexión red/servidor apagado -> registro local
        }

        val newUser = UsuarioResponse(
            idUsuario = localRegisteredUsers.size + 10,
            nombre = nombre,
            correo = correo,
            rol = rol,
            token = "token_registered_${localRegisteredUsers.size + 10}"
        )
        localRegisteredUsers[correo.lowercase()] = newUser
        return@withContext Result.success(newUser)
    }

    suspend fun login(correo: String, contrasena: String): Result<UsuarioResponse> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.login(LoginRequest(correo, contrasena))
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                localRegisteredUsers[correo.lowercase()] = user
                return@withContext Result.success(user)
            } else if (response.errorBody() != null) {
                val errStr = response.errorBody()?.string() ?: ""
                val msg = if (errStr.contains("detail")) {
                    errStr.substringAfter("\"detail\":\"").substringBefore("\"")
                } else {
                    "Credenciales incorrectas"
                }
                return@withContext Result.failure(Exception(msg))
            }
        } catch (e: Exception) {
            // Error de conexión de red -> fallback local
        }

        // Revisar si existe en los usuarios registrados localmente
        val localUser = localRegisteredUsers[correo.lowercase()]
        if (localUser != null) {
            return@withContext Result.success(localUser)
        }

        // Fallback por defecto si no hay conexión al backend
        return@withContext if (correo.contains("elena") || correo.contains("profesor")) {
            Result.success(
                UsuarioResponse(
                    idUsuario = 2,
                    nombre = "Dra. Elena Ruiz",
                    correo = correo.ifBlank { "elena.ruiz@universidad.edu" },
                    rol = "profesor",
                    token = "bearer_token_seguro_2"
                )
            )
        } else {
            Result.success(
                UsuarioResponse(
                    idUsuario = 1,
                    nombre = "Ana López",
                    correo = correo.ifBlank { "ana.lopez@universidad.edu" },
                    rol = "estudiante",
                    token = "bearer_token_seguro_1"
                )
            )
        }
    }

    suspend fun getStudentDashboard(idEstudiante: Int): Result<StudentDashboardResponse> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.getStudentDashboard(idEstudiante)
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (e: Exception) {
            // Fallback
        }

        val dashboard = StudentDashboardResponse(
            resumen = DashboardSummary(cursosActivos = localStudentCourses.size, pendientes = 2, promedioActual = 86.0),
            cursos = localStudentCourses.toList(),
            avisos = listOf(
                NoticeDto("Guía de la Tarea 2 disponible", "Dra. Elena Ruiz · Hoy, 08:30"),
                NoticeDto("Tutoría de dudas hoy", "Estadística aplicada · Sala virtual, 17:00")
            )
        )
        Result.success(dashboard)
    }

    suspend fun entregarActividad(
        context: Context,
        idEstudiante: Int,
        idActividad: Int,
        fileUri: Uri,
        fileName: String
    ): Result<ActividadEntregaResponse> = withContext(Dispatchers.IO) {
        try {
            val contentResolver = context.contentResolver
            val inputStream: InputStream? = contentResolver.openInputStream(fileUri)
            val bytes = inputStream?.readBytes() ?: ByteArray(0)
            inputStream?.close()

            val requestFile = bytes.toRequestBody("application/octet-stream".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("archivo", fileName, requestFile)
            val studentBody = idEstudiante.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val activityBody = idActividad.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val response = ApiClient.apiService.entregarActividad(studentBody, activityBody, body)
            if (response.isSuccessful && response.body() != null) {
                val newEntrega = response.body()!!
                localSubmissions.add(newEntrega)
                return@withContext Result.success(newEntrega)
            }
        } catch (e: Exception) {
            // Fallback
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val entrega = ActividadEntregaResponse(
            idEntrega = localSubmissions.size + 1,
            idEstudiante = idEstudiante,
            idActividad = idActividad,
            archivoNombre = fileName,
            selloTiempo = sdf.format(Date()),
            estado = "Entregada"
        )
        localSubmissions.add(entrega)
        return@withContext Result.success(entrega)
    }

    suspend fun getCalificaciones(idEstudiante: Int): Result<CalificacionesResponse> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.getCalificaciones(idEstudiante)
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (e: Exception) {
            // Fallback
        }

        val resp = CalificacionesResponse(
            idEstudiante = idEstudiante,
            promedioGeneral = 86.0,
            calificaciones = localGrades.toList()
        )
        Result.success(resp)
    }

    suspend fun getTeacherPanel(idProfesor: Int): Result<TeacherPanelResponse> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.getTeacherPanel(idProfesor)
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (e: Exception) {
            // Fallback
        }

        val panel = TeacherPanelResponse(
            docente = "Dra. Elena Ruiz",
            cursosImpartidos = 2,
            alumnosActivos = 120,
            tareasPorRevisar = localTeacherSubmissions.size,
            pendientesRevision = localTeacherSubmissions.toList()
        )
        Result.success(panel)
    }

    suspend fun calificarEntrega(idEntrega: Int, puntuacion: Double, retro: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.calificarEntrega(
                CalificacionRequest(idEntrega, puntuacion, retro)
            )
            if (response.isSuccessful) {
                localTeacherSubmissions.removeAll { it.idEntrega == idEntrega }
                val t2Index = localGrades.indexOfFirst { it.actividad.contains("T2") }
                if (t2Index >= 0) {
                    localGrades[t2Index] = CalificacionDto("T2 - Informe descriptivo", puntuacion, 20.0, "Evaluada")
                }
                return@withContext Result.success(true)
            }
        } catch (e: Exception) {
            // Fallback
        }

        localTeacherSubmissions.removeAll { it.idEntrega == idEntrega }
        val t2Index = localGrades.indexOfFirst { it.actividad.contains("T2") }
        if (t2Index >= 0) {
            localGrades[t2Index] = CalificacionDto("T2 - Informe descriptivo", puntuacion, 20.0, "Evaluada")
        }
        Result.success(true)
    }

    suspend fun procesarDonacion(monto: Double, correo: String, metodo: String): Result<DonacionResponse> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.procesarDonacion(
                DonacionRequest(monto, correo, metodo)
            )
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (e: Exception) {
            // Fallback
        }

        val resp = DonacionResponse(
            estatus = "Exitoso",
            mensaje = "Gracias por apoyar la educación gratuita bajo el ODS 17",
            monto = monto,
            correo = correo,
            transaccionId = "txn_educate_${System.currentTimeMillis().toString().takeLast(6)}"
        )
        Result.success(resp)
    }

    // --- DATOS MOCK DE MÓDULOS Y CURSOS PARA NAVEGACIÓN Y DETALLES ---

    fun getUpcomingDates(): List<UpcomingDate> {
        return listOf(
            UpcomingDate("16 OCT", "Octubre 2026", "Tarea 2 · Informe descriptivo", "Estadística aplicada", "23:59"),
            UpcomingDate("18 OCT", "Octubre 2026", "Foro · Interpretar los datos", "Estadística aplicada", "18:00")
        )
    }

    fun getCourseModules(): List<CourseModule> {
        return localModules.toList()
    }

    fun toggleModule(moduleId: Int): List<CourseModule> {
        val index = localModules.indexOfFirst { it.id == moduleId }
        if (index >= 0) {
            val mod = localModules[index]
            localModules[index] = mod.copy(isExpanded = !mod.isExpanded)
        }
        return localModules.toList()
    }

    fun addCourseModule(title: String, statusText: String, taskTitle: String?): List<CourseModule> {
        val newId = localModules.size + 1
        val lessons = if (!taskTitle.isNullOrBlank()) {
            listOf(
                ModuleLesson(
                    id = newId * 100 + 1,
                    title = "Lectura introductoria · $title",
                    type = ItemType.LECTURA,
                    subtitle = "Lectura · PDF · 6 páginas"
                ),
                ModuleLesson(
                    id = newId * 100 + 2,
                    title = taskTitle,
                    type = ItemType.TAREA,
                    subtitle = "23 oct, 23:59 · Valor: 20 puntos",
                    isPending = true,
                    points = 20
                )
            )
        } else emptyList()

        val newModule = CourseModule(
            id = newId,
            title = title,
            statusText = statusText.ifBlank { "Disponible en curso" },
            isCompleted = false,
            isExpanded = true,
            lessons = lessons
        )
        localModules.add(newModule)
        return localModules.toList()
    }

    fun enrollCourse(course: CourseCatalogItem) {
        if (localStudentCourses.none { it.idCurso == course.id }) {
            localStudentCourses.add(
                CourseDto(
                    idCurso = course.id,
                    nombre = course.title,
                    docente = course.instructor,
                    avancePorcentaje = 0
                )
            )
        }
    }

    fun getStudentCourses(): List<CourseDto> {
        return localStudentCourses.toList()
    }

    fun getLessonDetails(lessonId: Int): LessonData {
        return when (lessonId) {
            101, 102, 1 -> LessonData(
                id = 101,
                title = "Semana 1: Introducción a la Estadística",
                course = "Estadística aplicada · Dra. Elena Ruiz",
                youtubeVideoId = "sb7-230Bgdc",
                durationText = "14 min",
                weekText = "Semana 1 de 6",
                summaryText = "Conceptos fundamentales de población, muestra, variables cualitativas y cuantitativas.",
                transcriptText = "Bienvenidos a la Semana 1. En esta clase introductoria revisaremos los conceptos esenciales de la estadística descriptiva e inferencial...",
                resources = listOf(
                    LessonResource(1, "Guía de conceptos básicos", "PDF · 8 páginas", "1.1 MB"),
                    LessonResource(2, "Tabla de clasificación de variables", "PDF · 3 páginas", "450 KB")
                )
            )
            201, 202, 203, 2 -> LessonData(
                id = 201,
                title = "Semana 2: Análisis descriptivo y medidas de tendencia central",
                course = "Estadística aplicada · Dra. Elena Ruiz",
                youtubeVideoId = "sb7-230Bgdc",
                durationText = "18 min",
                weekText = "Semana 2 de 6",
                summaryText = "Aprende a resumir e interpretar datos con medidas de tendencia central (media, mediana, moda) y dispersión.",
                transcriptText = "Bienvenidos a la Semana 2. Hoy analizaremos cómo calcular e interpretar la media aritmética, la mediana y la desviación estándar...",
                resources = listOf(
                    LessonResource(1, "Guía de estadística descriptiva", "PDF · 12 páginas", "1.4 MB"),
                    LessonResource(2, "Ejercicios prácticos de tendencia central", "PDF · 4 páginas", "580 KB")
                )
            )
            301, 302, 3 -> LessonData(
                id = 301,
                title = "Semana 3: Probabilidad y Distribuciones de Frecuencia",
                course = "Estadística aplicada · Dra. Elena Ruiz",
                youtubeVideoId = "sb7-230Bgdc",
                durationText = "22 min",
                weekText = "Semana 3 de 6",
                summaryText = "Reglas fundamentales de probabilidad, eventos independientes y distribuciones de frecuencia normal.",
                transcriptText = "Bienvenidos a la Semana 3. Estudiaremos la probabilidad condicional, regla de Bayes y las curvas de distribución...",
                resources = listOf(
                    LessonResource(1, "Manual de probabilidad aplicada", "PDF · 15 páginas", "1.8 MB"),
                    LessonResource(2, "Formulario de distribuciones", "PDF · 2 páginas", "300 KB")
                )
            )
            else -> LessonData(
                id = lessonId,
                title = "Unidad de Aprendizaje · Lección $lessonId",
                course = "Estadística aplicada · Dra. Elena Ruiz",
                youtubeVideoId = "sb7-230Bgdc",
                durationText = "16 min",
                weekText = "Semana Activa",
                summaryText = "Contenido temático y ejercicios para el desarrollo de competencias estadísticas.",
                transcriptText = "Bienvenido a esta lección. Revisa el video explicativo y descarga los materiales de apoyo en PDF.",
                resources = listOf(
                    LessonResource(1, "Material de apoyo y lectura", "PDF · 10 páginas", "1.2 MB")
                )
            )
        }
    }

    fun getLessonResources(): List<LessonResource> {
        return listOf(
            LessonResource(1, "Guía de estadística descriptiva", "PDF · 12 páginas", "1.4 MB"),
            LessonResource(2, "Ejercicios de práctica", "PDF · 4 páginas", "580 KB")
        )
    }

    fun getCourseCatalog(): List<CourseCatalogItem> {
        return listOf(
            CourseCatalogItem(1, "Introducción a Python", "Ing. Carlos Mendoza", "Principiante", "6 semanas", "Tecnología"),
            CourseCatalogItem(2, "Estadística para la vida diaria", "Dra. Elena Ruiz", "Principiante", "4 semanas", "Matemáticas"),
            CourseCatalogItem(3, "Inglés Técnico para Programadores", "Lic. Sofia Ramos", "Intermedio", "8 semanas", "Idiomas")
        )
    }
}
