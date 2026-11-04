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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VideoCameraFront
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educate.data.model.StudentDashboardResponse
import com.example.educate.data.model.UsuarioResponse
import com.example.educate.ui.components.EDUcateBottomNavBar
import com.example.educate.ui.navigation.Screen
import com.example.educate.ui.theme.EducateBlueBg
import com.example.educate.ui.theme.EducateContainerBlue
import com.example.educate.ui.theme.EducateNavy
import com.example.educate.ui.theme.EducatePendingOrange
import com.example.educate.ui.theme.EducateTextPrimary
import com.example.educate.ui.theme.EducateTextSecondary

@Composable
fun StudentHomeScreen(
    currentUser: UsuarioResponse?,
    dashboardData: StudentDashboardResponse?,
    onNavigate: (String) -> Unit,
    onOpenCourse: () -> Unit,
    onOpenTask: () -> Unit,
    onSwitchUserRole: () -> Unit
) {
    val context = LocalContext.current
    val userName = currentUser?.nombre?.split(" ")?.firstOrNull() ?: "Ana"
    val initials = if (currentUser?.nombre != null) {
        currentUser.nombre.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
    } else "AL"

    var selectedDay by remember { mutableStateOf("14") }
    var showProfileDialog by remember { mutableStateOf(false) }
    var showAnnouncementsDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            EDUcateBottomNavBar(
                currentRoute = Screen.StudentHome.route,
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
            // --- TOP HEADER ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Logo",
                        tint = EducateNavy,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "EDUcate",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateNavy
                    )
                }

                // Avatar initials circle
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(EducateContainerBlue)
                        .clickable { showProfileDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateNavy
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Greeting
            Text(
                text = "Hola, $userName",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = EducateTextPrimary
            )
            Text(
                text = "Miércoles, 14 de octubre de 2026",
                fontSize = 13.sp,
                color = EducateTextSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            // --- METRIC CARDS ROW ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    value = "${dashboardData?.resumen?.cursosActivos ?: 3}",
                    label = "Cursos activos",
                    onClick = onOpenCourse
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    value = "${dashboardData?.resumen?.pendientes ?: 2}",
                    label = "Pendientes",
                    onClick = onOpenTask
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    value = "${dashboardData?.resumen?.promedioActual?.toInt() ?: 86}%",
                    label = "Promedio actual",
                    onClick = { onNavigate(Screen.GradesAnalytics.route) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- CONTINÚA APRENDIENDO CARD ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenCourse() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EducateNavy)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CONTINÚA APRENDIENDO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.7f),
                            letterSpacing = 0.8.sp
                        )
                        Icon(
                            imageVector = Icons.Default.NorthEast,
                            contentDescription = "Ir al curso",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Estadística aplicada",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Dra. Elena Ruiz · Grupo A",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                    )

                    // Barra de progreso
                    LinearProgressIndicator(
                        progress = { 0.67f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.25f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "4 de 6 temas completados",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "67%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- QUICK SHORTCUTS ROW (Explorar Cursos & Donaciones) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigate(Screen.ExploreCourses.route) },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(EducateContainerBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Explore, contentDescription = null, tint = EducateNavy, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Explorar", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EducateTextPrimary)
                            Text("Catálogo", fontSize = 11.sp, color = EducateTextSecondary)
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigate(Screen.DonationVolunteer.route) },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF2F2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Handshake, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Apoyar", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EducateTextPrimary)
                            Text("ODS 17", fontSize = 11.sp, color = EducateTextSecondary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- AVISOS RECIENTES ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Avisos recientes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateTextPrimary
                )
                Text(
                    text = "Ver todos",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EducateNavy,
                    modifier = Modifier.clickable { showAnnouncementsDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            NoticeItemCard(
                icon = Icons.Default.Campaign,
                title = "Guía de la Tarea 2 disponible",
                subtitle = "Dra. Elena Ruiz · Hoy, 08:30",
                onClick = {
                    Toast.makeText(context, "Abriendo: Guía de estadística descriptiva", Toast.LENGTH_SHORT).show()
                    onNavigate(Screen.LessonPlayer.route)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            NoticeItemCard(
                icon = Icons.Default.VideoCameraFront,
                title = "Tutoría de dudas hoy",
                subtitle = "Estadística aplicada · Sala virtual, 17:00",
                onClick = {
                    Toast.makeText(context, "Enlace a Sala Virtual: https://meet.upp.edu/tutoria-estadistica", Toast.LENGTH_LONG).show()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- TUS PRÓXIMAS FECHAS ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tus próximas fechas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateTextPrimary
                )
                Text(
                    text = "Octubre 2026",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EducateTextSecondary,
                    modifier = Modifier.clickable { onNavigate(Screen.Agenda.route) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Strip de días del calendario interactivo
            CalendarDaysRow(
                selectedDay = selectedDay,
                onSelectDay = { selectedDay = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedDay == "16" || selectedDay == "14") {
                UpcomingTaskCard(
                    dateBadge = "16 OCT",
                    title = "Tarea 2 · Informe descriptivo",
                    subtitle = "Estadística aplicada · 23:59",
                    onClick = onOpenTask
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (selectedDay == "18" || selectedDay == "14") {
                UpcomingTaskCard(
                    dateBadge = "18 OCT",
                    title = "Foro · Interpretar los datos",
                    subtitle = "Estadística aplicada · 18:00",
                    onClick = onOpenCourse
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (selectedDay !in listOf("14", "16", "18")) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White
                ) {
                    Text(
                        text = "No hay entregas programadas para el día $selectedDay de octubre.",
                        fontSize = 13.sp,
                        color = EducateTextSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- DIALOG: PERFIL DE USUARIO Y CAMBIO DE ROL ---
        if (showProfileDialog) {
            AlertDialog(
                onDismissRequest = { showProfileDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(EducateNavy),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(initials, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(currentUser?.nombre ?: "Ana López", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = EducateNavy)
                            Text(currentUser?.correo ?: "ana.lopez@universidad.edu", fontSize = 12.sp, color = EducateTextSecondary)
                        }
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Rol activo actual: ${currentUser?.rol?.uppercase() ?: "ESTUDIANTE"}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EducateTextPrimary
                        )
                        HorizontalDivider(color = Color(0xFFE2E8F0))

                        OutlinedButton(
                            onClick = {
                                showProfileDialog = false
                                onSwitchUserRole()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cambiar a Modo Docente Voluntario", fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                showProfileDialog = false
                                onNavigate(Screen.ExploreCourses.route)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Ver Catálogo de Cursos", fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                showProfileDialog = false
                                onNavigate(Screen.DonationVolunteer.route)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Apoyar a EDUcate (ODS 17)", fontSize = 13.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showProfileDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
                    ) {
                        Text("Cerrar")
                    }
                }
            )
        }

        // --- DIALOG: TODOS LOS AVISOS RECIENTES ---
        if (showAnnouncementsDialog) {
            AlertDialog(
                onDismissRequest = { showAnnouncementsDialog = false },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Tablón de Anuncios", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = EducateNavy)
                        IconButton(onClick = { showAnnouncementsDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = EducateTextSecondary)
                        }
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        NoticeItemCard(
                            icon = Icons.Default.Campaign,
                            title = "Guía de la Tarea 2 disponible",
                            subtitle = "Dra. Elena Ruiz · Hoy, 08:30",
                            onClick = { }
                        )
                        NoticeItemCard(
                            icon = Icons.Default.VideoCameraFront,
                            title = "Tutoría de dudas hoy",
                            subtitle = "Estadística aplicada · Sala virtual, 17:00",
                            onClick = { }
                        )
                        NoticeItemCard(
                            icon = Icons.Default.Campaign,
                            title = "Apertura de inscripciones a talleres",
                            subtitle = "Coordinación Académica · Ayer, 14:00",
                            onClick = { }
                        )
                        NoticeItemCard(
                            icon = Icons.Default.Campaign,
                            title = "Recordatorio: Encuesta de satisfacción",
                            subtitle = "Vicerrectoría de Innovación · 12 Oct",
                            onClick = { }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showAnnouncementsDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
                    ) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = EducateNavy
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = EducateTextSecondary
            )
        }
    }
}

@Composable
private fun NoticeItemCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
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
                    imageVector = icon,
                    contentDescription = null,
                    tint = EducateNavy,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
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
                imageVector = Icons.Default.NorthEast,
                contentDescription = null,
                tint = EducateTextSecondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun CalendarDaysRow(
    selectedDay: String,
    onSelectDay: (String) -> Unit
) {
    val days = listOf(
        Pair("L", "12"),
        Pair("M", "13"),
        Pair("X", "14"),
        Pair("J", "15"),
        Pair("V", "16"),
        Pair("S", "17"),
        Pair("D", "18")
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { (weekday, dayNum) ->
                val isSelected = dayNum == selectedDay
                val hasDeadline = dayNum in listOf("16", "18")
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onSelectDay(dayNum) }
                ) {
                    Text(
                        text = weekday,
                        fontSize = 11.sp,
                        color = EducateTextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) EducateNavy else if (hasDeadline) EducateContainerBlue else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayNum,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected || hasDeadline) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else if (hasDeadline) EducateNavy else EducateTextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UpcomingTaskCard(
    dateBadge: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(54.dp),
                shape = RoundedCornerShape(12.dp),
                color = EducateContainerBlue
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dateBadge.split(" ").firstOrNull() ?: "",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateNavy
                    )
                    Text(
                        text = dateBadge.split(" ").getOrNull(1) ?: "",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EducateNavy
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateTextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = EducateTextSecondary
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = EducateNavy,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
