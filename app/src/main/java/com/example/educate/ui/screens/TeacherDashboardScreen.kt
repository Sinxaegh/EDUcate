package com.example.educate.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educate.data.model.TeacherPanelResponse
import com.example.educate.ui.components.EDUcateBottomNavBar
import com.example.educate.ui.navigation.Screen
import com.example.educate.ui.theme.EducateBlueBg
import com.example.educate.ui.theme.EducateBorder
import com.example.educate.ui.theme.EducateContainerBlue
import com.example.educate.ui.theme.EducateNavy
import com.example.educate.ui.theme.EducatePendingOrange
import com.example.educate.ui.theme.EducateSuccessBg
import com.example.educate.ui.theme.EducateSuccessGreen
import com.example.educate.ui.theme.EducateTextPrimary
import com.example.educate.ui.theme.EducateTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(
    teacherData: TeacherPanelResponse?,
    showGradingDialog: Boolean,
    pendingStudentName: String?,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    onOpenReview: (Int, String) -> Unit,
    onCloseGradingDialog: () -> Unit,
    onGradeSubmitted: (Double, String) -> Unit,
    onSwitchUserRole: () -> Unit,
    onCreateModule: (String, String, String?) -> Unit = { _, _, _ -> }
) {
    val context = LocalContext.current
    var scoreInput by remember { mutableStateOf("18.0") }
    var feedbackInput by remember { mutableStateOf("Excelente trabajo en el cálculo de la mediana y la gráfica de dispersión.") }

    var showCreateModuleDialog by remember { mutableStateOf(false) }
    var newModuleTitle by remember { mutableStateOf("Semana 4 · Inferencia y Estimación") }
    var newModuleStatus by remember { mutableStateOf("Disponible el 26 de octubre") }
    var newModuleTaskTitle by remember { mutableStateOf("Tarea 3 · Estimación por intervalos") }

    var showRosterMatrixDialog by remember { mutableStateOf(false) }

    val teacherName = teacherData?.docente ?: "Dra. Elena Ruiz"
    val initials = teacherName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Panel Docente",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = EducateTextPrimary
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(EducateContainerBlue)
                            .clickable { onSwitchUserRole() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials.ifBlank { "ER" },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateNavy
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            EDUcateBottomNavBar(
                currentRoute = Screen.TeacherDashboard.route,
                onNavigate = onNavigate
            )
        },
        containerColor = EducateBlueBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header Greeting
            Text(
                text = "Hola, $teacherName",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = EducateTextPrimary
            )
            Text(
                text = "Profesora voluntaria · Periodo 2026-II",
                fontSize = 13.sp,
                color = EducateTextSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            // Metrics Card (light blue background)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EducateContainerBlue)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    TeacherMetricItem(
                        icon = Icons.Default.Book,
                        label = "Cursos impartidos: ${teacherData?.cursosImpartidos ?: 2}"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TeacherMetricItem(
                        icon = Icons.Default.People,
                        label = "Alumnos activos: ${teacherData?.alumnosActivos ?: 120}"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TeacherMetricItem(
                        icon = Icons.Default.CheckCircle,
                        label = "Tareas por revisar: ${teacherData?.tareasPorRevisar ?: 2}"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 1: Mis cursos asignados
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mis cursos asignados",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateTextPrimary
                )
                Text(
                    text = "Ver matriz de grupo",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EducateNavy,
                    modifier = Modifier.clickable { showRosterMatrixDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TeacherCourseCard(
                title = "Estadística aplicada",
                subtitle = "Grupo A · 72 alumnos · 6 módulos",
                onClick = { onNavigate(Screen.CourseDetail.route) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TeacherCourseCard(
                title = "Matemáticas básicas",
                subtitle = "Grupo B · 48 alumnos · 4 módulos",
                onClick = { Toast.makeText(context, "Curso: Matemáticas básicas (Grupo B)", Toast.LENGTH_SHORT).show() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Section 2: Por calificar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Por calificar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateTextPrimary
                )
                Text(
                    text = "Ver pendientes",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EducateNavy,
                    modifier = Modifier.clickable { showRosterMatrixDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val pendingList = teacherData?.pendientesRevision ?: emptyList()
            if (pendingList.isNotEmpty()) {
                pendingList.forEach { pending ->
                    PendingSubmissionRow(
                        studentName = pending.estudiante,
                        activity = pending.actividad,
                        time = pending.hora,
                        onReview = { onOpenReview(pending.idEntrega, pending.estudiante) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EducateSuccessGreen)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("¡Al día! No tienes actividades pendientes por calificar.", fontSize = 13.sp, color = EducateTextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Button: + Crear nuevo módulo
            Button(
                onClick = { showCreateModuleDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Crear nuevo módulo",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // --- DIALOG: CREAR NUEVO MÓDULO (RF-02) ---
        if (showCreateModuleDialog) {
            AlertDialog(
                onDismissRequest = { showCreateModuleDialog = false },
                title = {
                    Text(
                        text = "Crear nuevo módulo de aprendizaje",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateNavy
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Agrega una nueva unidad o semana al curso de Estadística Aplicada:",
                            fontSize = 12.sp,
                            color = EducateTextSecondary
                        )

                        OutlinedTextField(
                            value = newModuleTitle,
                            onValueChange = { newModuleTitle = it },
                            label = { Text("Título del módulo / Semana") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = newModuleStatus,
                            onValueChange = { newModuleStatus = it },
                            label = { Text("Estado / Disponibilidad") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = newModuleTaskTitle,
                            onValueChange = { newModuleTaskTitle = it },
                            label = { Text("Actividad o tarea asociada (opcional)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newModuleTitle.isNotBlank()) {
                                onCreateModule(newModuleTitle.trim(), newModuleStatus.trim(), newModuleTaskTitle.trim())
                                showCreateModuleDialog = false
                                Toast.makeText(context, "¡Módulo publicado con éxito en el curso!", Toast.LENGTH_LONG).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
                    ) {
                        Text("Publicar módulo")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateModuleDialog = false }) {
                        Text("Cancelar", color = EducateTextSecondary)
                    }
                }
            )
        }

        // --- DIALOG: MATRIZ DE SEGUIMIENTO ACADÉMICO (RF-05.1) ---
        if (showRosterMatrixDialog) {
            AlertDialog(
                onDismissRequest = { showRosterMatrixDialog = false },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Matriz de Seguimiento del Grupo A",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateNavy
                        )
                        IconButton(onClick = { showRosterMatrixDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = EducateTextSecondary)
                        }
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Estadística aplicada · 72 estudiantes inscritos", fontSize = 12.sp, color = EducateTextSecondary)
                        HorizontalDivider(color = EducateBorder)

                        StudentRosterRow("Ana López", "T1: 18/20", "C1: 16/20", "T2: Pendiente", "86.0%")
                        StudentRosterRow("Luis García", "T1: 17/20", "C1: 14/20", "T2: Pendiente", "78.0%")
                        StudentRosterRow("Carlos Vega", "T1: 19/20", "C1: 18/20", "T2: 18/20", "92.5%")
                        StudentRosterRow("María Torres", "T1: 15/20", "C1: 13/20", "T2: 16/20", "74.0%")
                        StudentRosterRow("Sofía Morales", "T1: 20/20", "C1: 19/20", "T2: 19/20", "96.0%")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showRosterMatrixDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
                    ) {
                        Text("Cerrar")
                    }
                }
            )
        }

        // --- INTERACTIVE GRADING DIALOG (RF-04) ---
        if (showGradingDialog) {
            AlertDialog(
                onDismissRequest = onCloseGradingDialog,
                title = {
                    Text(
                        text = "Calificar entrega de ${pendingStudentName ?: "Estudiante"}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateNavy
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Tarea 2 · Informe descriptivo (Máximo: 20 pts)",
                            fontSize = 12.sp,
                            color = EducateTextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Rubric guidelines box
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EducateContainerBlue,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Rúbrica de evaluación:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EducateNavy)
                                Text("• Cálculo de media, mediana y dispersión: 8 pts", fontSize = 10.sp, color = EducateNavy)
                                Text("• Gráfica y visualización: 6 pts", fontSize = 10.sp, color = EducateNavy)
                                Text("• Conclusiones e interpretación: 6 pts", fontSize = 10.sp, color = EducateNavy)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = scoreInput,
                            onValueChange = { scoreInput = it },
                            label = { Text("Puntuación (0 - 20)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = feedbackInput,
                            onValueChange = { feedbackInput = it },
                            label = { Text("Retroalimentación docente") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val grade = scoreInput.toDoubleOrNull() ?: 18.0
                            onGradeSubmitted(grade, feedbackInput)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
                    ) {
                        Text("Guardar calificación")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onCloseGradingDialog) {
                        Text("Cancelar", color = EducateTextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
private fun StudentRosterRow(
    name: String,
    t1: String,
    c1: String,
    t2: String,
    average: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF8FAFC)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EducateTextPrimary)
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (t2.contains("Pendiente")) Color(0xFFFEF3C7) else EducateContainerBlue
                ) {
                    Text(
                        text = "Promedio: $average",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (t2.contains("Pendiente")) EducatePendingOrange else EducateNavy,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(t1, fontSize = 11.sp, color = EducateTextSecondary)
                Text(c1, fontSize = 11.sp, color = EducateTextSecondary)
                Text(
                    text = t2,
                    fontSize = 11.sp,
                    fontWeight = if (t2.contains("Pendiente")) FontWeight.Bold else FontWeight.Normal,
                    color = if (t2.contains("Pendiente")) EducatePendingOrange else EducateSuccessGreen
                )
            }
        }
    }
}

@Composable
private fun TeacherMetricItem(
    icon: ImageVector,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EducateNavy,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = EducateNavy
        )
    }
}

@Composable
private fun TeacherCourseCard(title: String, subtitle: String, onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(EducateContainerBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = EducateNavy,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateTextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = EducateTextSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = EducateTextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun PendingSubmissionRow(
    studentName: String,
    activity: String,
    time: String,
    onReview: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(EducateContainerBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = studentName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateNavy
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = studentName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateTextPrimary
                )
                Text(
                    text = "$activity · $time",
                    fontSize = 12.sp,
                    color = EducateTextSecondary
                )
            }
            OutlinedButton(
                onClick = onReview,
                shape = RoundedCornerShape(20.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = EducateNavy)
            ) {
                Text("Revisar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
