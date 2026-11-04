package com.example.educate.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educate.data.model.ActividadEntregaResponse
import com.example.educate.data.model.CalificacionesResponse
import com.example.educate.data.model.CourseCatalogItem
import com.example.educate.data.model.CourseModule
import com.example.educate.data.model.DonacionResponse
import com.example.educate.data.model.LessonData
import com.example.educate.data.model.StudentDashboardResponse
import com.example.educate.data.model.TeacherPanelResponse
import com.example.educate.data.model.UsuarioResponse
import com.example.educate.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppUiState(
    val currentUser: UsuarioResponse? = UsuarioResponse(1, "Ana López", "ana.lopez@universidad.edu", "estudiante", "token_demo"),
    val currentLesson: LessonData = LessonData(),
    val isLoggingIn: Boolean = false,
    val loginError: String? = null,
    val studentDashboard: StudentDashboardResponse? = null,
    val teacherPanel: TeacherPanelResponse? = null,
    val calificaciones: CalificacionesResponse? = null,
    val courseModules: List<CourseModule> = emptyList(),
    val courseCatalog: List<CourseCatalogItem> = emptyList(),
    val selectedFileUri: Uri? = null,
    val selectedFileName: String? = null,
    val selectedFileSizeText: String? = "2,4 MB",
    val isSubmitting: Boolean = false,
    val lastSubmission: ActividadEntregaResponse? = null,
    val submissionMessage: String? = null,
    val donationMessage: String? = null,
    val isDonating: Boolean = false,
    val showGradingDialog: Boolean = false,
    val pendingReviewId: Int? = null,
    val pendingReviewStudent: String? = null
)

class AppViewModel(
    private val repository: AppRepository = AppRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _uiState.update {
            it.copy(
                courseModules = repository.getCourseModules(),
                courseCatalog = repository.getCourseCatalog()
            )
        }
        loadStudentDashboard()
        loadCalificaciones()
        loadTeacherPanel()
    }

    fun login(correo: String, contrasena: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingIn = true, loginError = null) }
            val result = repository.login(correo, contrasena)
            result.onSuccess { user ->
                _uiState.update { it.copy(currentUser = user, isLoggingIn = false) }
                if (user.rol == "profesor") {
                    loadTeacherPanel()
                } else {
                    loadStudentDashboard()
                    loadCalificaciones()
                }
                onSuccess()
            }.onFailure { err ->
                _uiState.update { it.copy(isLoggingIn = false, loginError = err.message ?: "Error al iniciar sesión") }
            }
        }
    }

    fun register(nombre: String, correo: String, contrasena: String, rol: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingIn = true, loginError = null) }
            val result = repository.register(nombre, correo, contrasena, rol)
            result.onSuccess { user ->
                _uiState.update { it.copy(currentUser = user, isLoggingIn = false) }
                if (user.rol == "profesor") {
                    loadTeacherPanel()
                } else {
                    loadStudentDashboard()
                    loadCalificaciones()
                }
                onSuccess()
            }.onFailure { err ->
                _uiState.update { it.copy(isLoggingIn = false, loginError = err.message ?: "Error al registrarse") }
            }
        }
    }

    fun logout() {
        _uiState.update { AppUiState(currentUser = null) }
    }

    fun switchRole(newRole: String) {
        val name = if (newRole == "profesor") "Dra. Elena Ruiz" else "Ana López"
        val email = if (newRole == "profesor") "elena.ruiz@universidad.edu" else "ana.lopez@universidad.edu"
        val id = if (newRole == "profesor") 2 else 1
        _uiState.update {
            it.copy(
                currentUser = UsuarioResponse(id, name, email, newRole, "token_demo")
            )
        }
        if (newRole == "profesor") {
            loadTeacherPanel()
        } else {
            loadStudentDashboard()
            loadCalificaciones()
        }
    }

    fun loadStudentDashboard() {
        val studentId = _uiState.value.currentUser?.idUsuario ?: 1
        viewModelScope.launch {
            repository.getStudentDashboard(studentId).onSuccess { data ->
                _uiState.update { it.copy(studentDashboard = data) }
            }
        }
    }

    fun loadTeacherPanel() {
        val teacherId = _uiState.value.currentUser?.idUsuario ?: 2
        viewModelScope.launch {
            repository.getTeacherPanel(teacherId).onSuccess { data ->
                _uiState.update { it.copy(teacherPanel = data) }
            }
        }
    }

    fun loadCalificaciones() {
        val studentId = _uiState.value.currentUser?.idUsuario ?: 1
        viewModelScope.launch {
            repository.getCalificaciones(studentId).onSuccess { data ->
                _uiState.update { it.copy(calificaciones = data) }
            }
        }
    }

    fun setSelectedFile(uri: Uri?, name: String?, sizeText: String = "2,4 MB") {
        _uiState.update {
            it.copy(
                selectedFileUri = uri,
                selectedFileName = name,
                selectedFileSizeText = sizeText
            )
        }
    }

    fun clearSelectedFile() {
        _uiState.update {
            it.copy(
                selectedFileUri = null,
                selectedFileName = null
            )
        }
    }

    fun submitActivity(context: Context, onSuccess: () -> Unit) {
        val uri = uiState.value.selectedFileUri
        val fileName = uiState.value.selectedFileName ?: "Informe_descriptivo.pdf"
        val studentId = uiState.value.currentUser?.idUsuario ?: 1

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, submissionMessage = null) }
            
            val mockUri = uri ?: Uri.EMPTY
            val result = repository.entregarActividad(context, studentId, 2, mockUri, fileName)
            result.onSuccess { entrega ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        lastSubmission = entrega,
                        submissionMessage = "Entrega exitosa"
                    )
                }
                loadCalificaciones()
                onSuccess()
            }.onFailure { err ->
                _uiState.update {
                    it.copy(isSubmitting = false, submissionMessage = "Error: ${err.message}")
                }
            }
        }
    }

    fun openGradingDialog(reviewId: Int, studentName: String) {
        _uiState.update {
            it.copy(
                showGradingDialog = true,
                pendingReviewId = reviewId,
                pendingReviewStudent = studentName
            )
        }
    }

    fun closeGradingDialog() {
        _uiState.update {
            it.copy(
                showGradingDialog = false,
                pendingReviewId = null,
                pendingReviewStudent = null
            )
        }
    }

    fun gradeSubmission(grade: Double, feedback: String) {
        val reviewId = uiState.value.pendingReviewId ?: return
        viewModelScope.launch {
            repository.calificarEntrega(reviewId, grade, feedback).onSuccess {
                closeGradingDialog()
                loadTeacherPanel()
                loadCalificaciones()
            }
        }
    }

    fun processDonation(monto: Double, correo: String, metodo: String, onSuccess: (DonacionResponse) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDonating = true, donationMessage = null) }
            repository.procesarDonacion(monto, correo, metodo).onSuccess { res ->
                _uiState.update {
                    it.copy(isDonating = false, donationMessage = res.mensaje)
                }
                onSuccess(res)
            }.onFailure { err ->
                _uiState.update {
                    it.copy(isDonating = false, donationMessage = "Error al procesar donación")
                }
            }
        }
    }

    fun toggleModuleExpanded(moduleId: Int) {
        val updated = repository.toggleModule(moduleId)
        _uiState.update { it.copy(courseModules = updated) }
    }

    fun createCourseModule(title: String, statusText: String, taskTitle: String?) {
        val updated = repository.addCourseModule(title, statusText, taskTitle)
        _uiState.update { it.copy(courseModules = updated) }
    }

    fun enrollInCourse(course: CourseCatalogItem, onSuccess: () -> Unit) {
        repository.enrollCourse(course)
        loadStudentDashboard()
        onSuccess()
    }

    fun selectLesson(lessonId: Int) {
        val lessonData = repository.getLessonDetails(lessonId)
        _uiState.update { it.copy(currentLesson = lessonData) }
    }
}
