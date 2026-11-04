package com.example.educate.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educate.data.model.CalificacionDto
import com.example.educate.data.model.CalificacionesResponse
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
fun GradesAnalyticsScreen(
    calificacionesData: CalificacionesResponse?,
    submissionMessage: String?,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var showReportCardDialog by remember { mutableStateOf(false) }
    var showCertificateDialog by remember { mutableStateOf(false) }

    val items = calificacionesData?.calificaciones ?: listOf(
        CalificacionDto("T1 - Conceptos básicos", 18.0, 20.0, "Evaluada"),
        CalificacionDto("Cuestionario 1", 16.0, 20.0, "Evaluada"),
        CalificacionDto("Foro 1 - Variables", 9.0, 10.0, "Evaluada"),
        CalificacionDto("T2 - Informe descriptivo", null, 20.0, "Pendiente")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis resultados",
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
                    IconButton(onClick = {
                        Toast.makeText(context, "Periodo escolar 2026-II · Activo", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Opciones",
                            tint = EducateTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            EDUcateBottomNavBar(
                currentRoute = Screen.GradesAnalytics.route,
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
            // Success Submission Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = EducateSuccessBg
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EducateSuccessGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = submissionMessage ?: "Entrega exitosa",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateSuccessGreen
                        )
                        Text(
                            text = "Tarea 2 · Pendiente de revisión",
                            fontSize = 12.sp,
                            color = EducateSuccessGreen.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Titles
            Text(
                text = "Calificaciones y analíticas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = EducateTextPrimary
            )
            Text(
                text = "Estadística aplicada · Periodo 2026-II",
                fontSize = 13.sp,
                color = EducateTextSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            // Digital Report Card (Boleta Digital)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showReportCardDialog = true },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EducateNavy)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "BOLETA DIGITAL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.7f),
                            letterSpacing = 0.8.sp
                        )
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Descargar",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${calificacionesData?.promedioGeneral?.toInt() ?: 86}%",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "43/50 pts",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Desempeño alto",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "3 actividades evaluadas",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Academic Tracking Section (Seguimiento académico)
            Text(
                text = "Seguimiento académico",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = EducateTextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Actividad",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateTextSecondary,
                            modifier = Modifier.weight(1.5f)
                        )
                        Text(
                            text = "Puntos",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateTextSecondary,
                            modifier = Modifier.weight(0.8f)
                        )
                        Text(
                            text = "Estado",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateTextSecondary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = EducateBorder)

                    items.forEachIndexed { index, item ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.actividad,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EducateTextPrimary,
                                modifier = Modifier.weight(1.5f)
                            )

                            Text(
                                text = if (item.puntos != null) "${item.puntos.toInt()}/${item.maximo.toInt()}" else "—",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EducateTextPrimary,
                                modifier = Modifier.weight(0.8f)
                            )

                            Box(modifier = Modifier.weight(1f)) {
                                if (item.estado == "Evaluada") {
                                    Text(
                                        text = "Evaluada",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EducateSuccessGreen
                                    )
                                } else {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFFEF3C7)
                                    ) {
                                        Text(
                                            text = "Pendiente",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EducatePendingOrange,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                        if (index < items.size - 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = EducateBorder.copy(alpha = 0.5f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tu Progreso Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tu progreso",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateTextPrimary
                )
                Text(
                    text = "Meta ≥ 80%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EducateTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Column 1: Notas previas
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Notas previas",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateTextSecondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ProgressMiniRow(label = "T1", percentText = "90%", progress = 0.9f)
                        Spacer(modifier = Modifier.height(6.dp))
                        ProgressMiniRow(label = "C1", percentText = "80%", progress = 0.8f)
                        Spacer(modifier = Modifier.height(6.dp))
                        ProgressMiniRow(label = "F1", percentText = "90%", progress = 0.9f)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Column 2: Avance del curso
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Avance del curso",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateTextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "67%",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateNavy
                        )
                        Text(
                            text = "4 de 6 temas completados",
                            fontSize = 11.sp,
                            color = EducateTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "La Tarea 2 no se incluye en el promedio hasta su revisión.",
                fontSize = 11.sp,
                color = EducateTextSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Actions: Boleta Digital & Certificado (RF-05)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { showReportCardDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(23.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EducateNavy),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver Boleta", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { showCertificateDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(23.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
                ) {
                    Icon(Icons.Default.CardMembership, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Certificado", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // --- DIALOG: BOLETA DIGITAL OFICIAL (FE-05) ---
        if (showReportCardDialog) {
            AlertDialog(
                onDismissRequest = { showReportCardDialog = false },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Boleta Oficial de Calificaciones",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateNavy
                        )
                        IconButton(onClick = { showReportCardDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = EducateTextSecondary)
                        }
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EducateContainerBlue,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Universidad Politécnica de Pénjamo", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EducateNavy)
                                Text("Alumno: Ana López  |  Matrícula: 25032100062", fontSize = 11.sp, color = EducateNavy)
                                Text("Programa: Estadística Aplicada  |  Ciclo: 2026-II", fontSize = 11.sp, color = EducateNavy)
                            }
                        }

                        Text("Desglose oficial de evaluaciones:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EducateTextPrimary)

                        items.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.actividad, fontSize = 12.sp, color = EducateTextSecondary)
                                Text(
                                    text = if (item.puntos != null) "${item.puntos.toInt()}/${item.maximo.toInt()}" else "Pendiente",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.puntos != null) EducateNavy else EducatePendingOrange
                                )
                            }
                        }

                        HorizontalDivider(color = EducateBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Promedio ponderado acumulado:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EducateTextPrimary)
                            Text("86.0 / 100", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EducateSuccessGreen)
                        }

                        Text(
                            text = "Folio digital: EDU-2026-BOLETA-88491 · Código QR firmado por Dirección Académica",
                            fontSize = 10.sp,
                            color = EducateTextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showReportCardDialog = false
                            Toast.makeText(context, "Descargando Boleta Digital en PDF (180 KB)...", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Descargar PDF")
                    }
                }
            )
        }

        // --- DIALOG: CERTIFICADO ACADÉMICO (RF-05) ---
        if (showCertificateDialog) {
            AlertDialog(
                onDismissRequest = { showCertificateDialog = false },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Certificado de Reconocimiento",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateNavy
                        )
                        IconButton(onClick = { showCertificateDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = EducateTextSecondary)
                        }
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, EducateBorder, RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = EducateNavy,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "EDUcate & Universidad Politécnica",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateNavy
                        )
                        Text(
                            text = "Otorga el presente certificado a:",
                            fontSize = 11.sp,
                            color = EducateTextSecondary
                        )
                        Text(
                            text = "Ana López",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateNavy
                        )
                        Text(
                            text = "Por haber acreditado con desempeño sobresaliente el programa de:",
                            fontSize = 11.sp,
                            color = EducateTextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Estadística Aplicada (60 horas)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateTextPrimary
                        )
                        Text(
                            text = "Promedio: 86.0 / 100  ·  Fecha: Octubre 2026",
                            fontSize = 11.sp,
                            color = EducateSuccessGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Dra. Elena Ruiz — Docente Voluntaria\nCertificación verificada en Blockchain / Supabase",
                            fontSize = 10.sp,
                            color = EducateTextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showCertificateDialog = false
                            Toast.makeText(context, "Generando Certificado oficial en PDF de alta resolución...", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Descargar Certificado")
                    }
                }
            )
        }
    }
}

@Composable
private fun ProgressMiniRow(label: String, percentText: String, progress: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = EducateTextPrimary,
            modifier = Modifier.width(20.dp)
        )
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .height(5.dp)
                .clip(CircleShape),
            color = EducateNavy,
            trackColor = EducateContainerBlue
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = percentText,
            fontSize = 10.sp,
            color = EducateTextSecondary
        )
    }
}
