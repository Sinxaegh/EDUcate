package com.example.educate

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.educate.data.model.CourseCatalogItem
import com.example.educate.data.model.DonacionResponse
import com.example.educate.ui.navigation.Screen
import com.example.educate.ui.screens.ActivitySubmissionScreen
import com.example.educate.ui.screens.AgendaScreen
import com.example.educate.ui.screens.CourseDetailScreen
import com.example.educate.ui.screens.DonationVolunteerScreen
import com.example.educate.ui.screens.ExploreCoursesScreen
import com.example.educate.ui.screens.GradesAnalyticsScreen
import com.example.educate.ui.screens.LessonPlayerScreen
import com.example.educate.ui.screens.LoginScreen
import com.example.educate.ui.screens.StudentHomeScreen
import com.example.educate.ui.screens.TeacherDashboardScreen
import com.example.educate.ui.theme.EDUcateTheme
import com.example.educate.ui.viewmodel.AppUiState
import com.example.educate.ui.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EDUcateTheme {
                EDUcateApp()
            }
        }
    }
}

@Composable
fun EDUcateApp(
    viewModel: AppViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    EDUcateAppContent(
        uiState = uiState,
        onLogin = { email, pass, onSuccess ->
            viewModel.login(email, pass, onSuccess)
        },
        onRegister = { nombre, email, pass, rol, onSuccess ->
            viewModel.register(nombre, email, pass, rol, onSuccess)
        },
        onSelectLesson = { lessonId ->
            viewModel.selectLesson(lessonId)
        },
        onSwitchRole = { role ->
            viewModel.switchRole(role)
        },
        onToggleModuleExpanded = { moduleId ->
            viewModel.toggleModuleExpanded(moduleId)
        },
        onSetSelectedFile = { uri, name, size ->
            viewModel.setSelectedFile(uri, name, size)
        },
        onClearSelectedFile = {
            viewModel.clearSelectedFile()
        },
        onSubmitActivity = { context, onSuccess ->
            viewModel.submitActivity(context, onSuccess)
        },
        onOpenGradingDialog = { reviewId, student ->
            viewModel.openGradingDialog(reviewId, student)
        },
        onCloseGradingDialog = {
            viewModel.closeGradingDialog()
        },
        onGradeSubmitted = { grade, feedback ->
            viewModel.gradeSubmission(grade, feedback)
        },
        onProcessDonation = { monto, correo, metodo, onSuccess ->
            viewModel.processDonation(monto, correo, metodo, onSuccess)
        },
        onCreateModule = { title, status, task ->
            viewModel.createCourseModule(title, status, task)
        },
        onEnrollInCourse = { course, onSuccess ->
            viewModel.enrollInCourse(course, onSuccess)
        }
    )
}

@Composable
fun EDUcateAppContent(
    uiState: AppUiState,
    onLogin: (String, String, () -> Unit) -> Unit = { _, _, _ -> },
    onRegister: (String, String, String, String, () -> Unit) -> Unit = { _, _, _, _, _ -> },
    onSelectLesson: (Int) -> Unit = {},
    onSwitchRole: (String) -> Unit = {},
    onToggleModuleExpanded: (Int) -> Unit = {},
    onSetSelectedFile: (Uri?, String?, String) -> Unit = { _, _, _ -> },
    onClearSelectedFile: () -> Unit = {},
    onSubmitActivity: (Context, () -> Unit) -> Unit = { _, _ -> },
    onOpenGradingDialog: (Int, String) -> Unit = { _, _ -> },
    onCloseGradingDialog: () -> Unit = {},
    onGradeSubmitted: (Double, String) -> Unit = { _, _ -> },
    onProcessDonation: (Double, String, String, (DonacionResponse) -> Unit) -> Unit = { _, _, _, _ -> },
    onCreateModule: (String, String, String?) -> Unit = { _, _, _ -> },
    onEnrollInCourse: (CourseCatalogItem, () -> Unit) -> Unit = { _, _ -> }
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // P-01: Inicio de sesión
        composable(Screen.Login.route) {
            LoginScreen(
                isLoggingIn = uiState.isLoggingIn,
                errorMessage = uiState.loginError,
                onLogin = { email, pass ->
                    onLogin(email, pass) {
                        val destination = if (uiState.currentUser?.rol == "profesor") {
                            Screen.TeacherDashboard.route
                        } else {
                            Screen.StudentHome.route
                        }
                        navController.navigate(destination) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onRegister = { nombre, email, pass, rol ->
                    onRegister(nombre, email, pass, rol) {
                        val destination = if (rol == "profesor") {
                            Screen.TeacherDashboard.route
                        } else {
                            Screen.StudentHome.route
                        }
                        navController.navigate(destination) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onRoleSelected = { role ->
                    onSwitchRole(role)
                }
            )
        }

        // P-02: Inicio del estudiante
        composable(Screen.StudentHome.route) {
            StudentHomeScreen(
                currentUser = uiState.currentUser,
                dashboardData = uiState.studentDashboard,
                onNavigate = { route ->
                    if (route != Screen.StudentHome.route) {
                        navController.navigate(route)
                    }
                },
                onOpenCourse = {
                    navController.navigate(Screen.CourseDetail.route)
                },
                onOpenTask = {
                    navController.navigate(Screen.ActivitySubmission.route)
                },
                onSwitchUserRole = {
                    val newRole = if (uiState.currentUser?.rol == "profesor") "estudiante" else "profesor"
                    onSwitchRole(newRole)
                    if (newRole == "profesor") {
                        navController.navigate(Screen.TeacherDashboard.route)
                    } else {
                        Toast.makeText(context, "Modo Estudiante activado", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        // P-03: Cursos y módulos
        composable(Screen.CourseDetail.route) {
            CourseDetailScreen(
                currentUser = uiState.currentUser,
                modules = uiState.courseModules,
                onNavigate = { route ->
                    if (route != Screen.CourseDetail.route) {
                        navController.navigate(route)
                    }
                },
                onBack = { navController.popBackStack() },
                onOpenLesson = { navController.navigate(Screen.LessonPlayer.route) },
                onOpenLessonWithId = { lessonId ->
                    onSelectLesson(lessonId)
                    navController.navigate(Screen.LessonPlayer.route)
                },
                onOpenSubmission = { navController.navigate(Screen.ActivitySubmission.route) },
                onToggleModule = { moduleId -> onToggleModuleExpanded(moduleId) }
            )
        }

        // P-04: Entrega de actividades
        composable(Screen.ActivitySubmission.route) {
            ActivitySubmissionScreen(
                selectedFileUri = uiState.selectedFileUri,
                selectedFileName = uiState.selectedFileName,
                selectedFileSizeText = uiState.selectedFileSizeText,
                isSubmitting = uiState.isSubmitting,
                onNavigate = { route ->
                    if (route != Screen.ActivitySubmission.route) {
                        navController.navigate(route)
                    }
                },
                onBack = { navController.popBackStack() },
                onFileSelected = { uri, name, size ->
                    onSetSelectedFile(uri, name, size)
                },
                onClearFile = { onClearSelectedFile() },
                onConfirmSubmission = {
                    onSubmitActivity(context) {
                        Toast.makeText(context, "¡Actividad entregada con éxito!", Toast.LENGTH_LONG).show()
                        navController.navigate(Screen.GradesAnalytics.route)
                    }
                }
            )
        }

        // P-05: Calificaciones y analíticas
        composable(Screen.GradesAnalytics.route) {
            GradesAnalyticsScreen(
                calificacionesData = uiState.calificaciones,
                submissionMessage = uiState.submissionMessage,
                onNavigate = { route ->
                    if (route != Screen.GradesAnalytics.route) {
                        navController.navigate(route)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // P-06: Catálogo y exploración de cursos
        composable(Screen.ExploreCourses.route) {
            ExploreCoursesScreen(
                catalog = uiState.courseCatalog,
                onNavigate = { route ->
                    if (route != Screen.ExploreCourses.route) {
                        navController.navigate(route)
                    }
                },
                onBack = { navController.popBackStack() },
                onSelectCourse = { course ->
                    onEnrollInCourse(course) {
                        Toast.makeText(context, "¡Inscrito exitosamente en: ${course.title}!", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.StudentHome.route)
                    }
                }
            )
        }

        // P-07: Reproductor y contenido de lección
        composable(Screen.LessonPlayer.route) {
            LessonPlayerScreen(
                lesson = uiState.currentLesson,
                onNavigate = { route ->
                    if (route != Screen.LessonPlayer.route) {
                        navController.navigate(route)
                    }
                },
                onBack = { navController.popBackStack() },
                onCompleteAndContinue = {
                    Toast.makeText(context, "Lección completada", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.ActivitySubmission.route)
                }
            )
        }

        // P-08: Panel del profesor voluntario
        composable(Screen.TeacherDashboard.route) {
            TeacherDashboardScreen(
                teacherData = uiState.teacherPanel,
                showGradingDialog = uiState.showGradingDialog,
                pendingStudentName = uiState.pendingReviewStudent,
                onNavigate = { route ->
                    if (route != Screen.TeacherDashboard.route) {
                        navController.navigate(route)
                    }
                },
                onBack = { navController.popBackStack() },
                onOpenReview = { reviewId, student ->
                    onOpenGradingDialog(reviewId, student)
                },
                onCloseGradingDialog = { onCloseGradingDialog() },
                onGradeSubmitted = { grade, feedback ->
                    onGradeSubmitted(grade, feedback)
                    Toast.makeText(context, "Calificación guardada para ${uiState.pendingReviewStudent}", Toast.LENGTH_LONG).show()
                },
                onSwitchUserRole = {
                    onSwitchRole("estudiante")
                    navController.navigate(Screen.StudentHome.route)
                    Toast.makeText(context, "Modo Estudiante activado", Toast.LENGTH_SHORT).show()
                },
                onCreateModule = { title, status, task ->
                    onCreateModule(title, status, task)
                }
            )
        }

        // P-09: Donaciones y registro de voluntarios
        composable(Screen.DonationVolunteer.route) {
            DonationVolunteerScreen(
                isDonating = uiState.isDonating,
                donationMessage = uiState.donationMessage,
                onNavigate = { route ->
                    if (route != Screen.DonationVolunteer.route) {
                        navController.navigate(route)
                    }
                },
                onBack = { navController.popBackStack() },
                onProcessDonation = { monto, correo, metodo ->
                    onProcessDonation(monto, correo, metodo) { res ->
                        Toast.makeText(context, "${res.mensaje} (ID: ${res.transaccionId})", Toast.LENGTH_LONG).show()
                    }
                },
                onApplyAsTeacher = {
                    onSwitchRole("profesor")
                    navController.navigate(Screen.TeacherDashboard.route)
                    Toast.makeText(context, "Bienvenido al Panel Docente", Toast.LENGTH_LONG).show()
                }
            )
        }

        // P-Agenda: Cronograma y Agenda Académica (Tab Agenda)
        composable(Screen.Agenda.route) {
            AgendaScreen(
                onNavigate = { route ->
                    if (route != Screen.Agenda.route) {
                        navController.navigate(route)
                    }
                },
                onBack = { navController.popBackStack() },
                onOpenTask = { navController.navigate(Screen.ActivitySubmission.route) },
                onOpenCourse = { navController.navigate(Screen.CourseDetail.route) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EDUcateAppPreview() {
    EDUcateTheme {
        EDUcateAppContent(
            uiState = AppUiState()
        )
    }
}
