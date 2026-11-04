package com.example.educate.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.VideoCameraFront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educate.data.model.AgendaItem
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
fun AgendaScreen(
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    onOpenTask: () -> Unit,
    onOpenCourse: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Todas") }
    var selectedDay by remember { mutableStateOf("16") }
    val filters = listOf("Todas", "Tareas", "Foros", "Tutorías", "Exámenes")

    val agendaItems = listOf(
        AgendaItem(1, "Tarea 2 · Informe descriptivo", "Estadística aplicada", "16", "OCT", "23:59", "Tareas", isPending = true, points = 20),
        AgendaItem(2, "Foro · Interpretar los datos", "Estadística aplicada", "18", "OCT", "18:00", "Foros", isPending = true, points = 10),
        AgendaItem(3, "Tutoría de dudas en vivo", "Estadística aplicada", "21", "OCT", "17:00", "Tutorías", isPending = true, points = null),
        AgendaItem(4, "Cuestionario 2 · Dispersión", "Estadística aplicada", "23", "OCT", "23:59", "Exámenes", isPending = true, points = 20),
        AgendaItem(5, "Tarea 1 · Conceptos básicos", "Estadística aplicada", "09", "OCT", "23:59", "Tareas", isPending = false, points = 20)
    )

    val filteredItems = agendaItems.filter {
        selectedFilter == "Todas" || it.type.equals(selectedFilter, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agenda Académica",
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
                    IconButton(onClick = { }) {
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
                currentRoute = Screen.Agenda.route,
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
            // Header Month Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Octubre 2026",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateNavy
                    )
                    Text(
                        text = "2 entregas pendientes esta semana",
                        fontSize = 13.sp,
                        color = EducateTextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EducateContainerBlue,
                    modifier = Modifier.clickable { }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = EducateNavy,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Semana 2",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateNavy
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Calendar Strip
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
                    val days = listOf(
                        Pair("L", "12"),
                        Pair("M", "13"),
                        Pair("X", "14"),
                        Pair("J", "15"),
                        Pair("V", "16"),
                        Pair("S", "17"),
                        Pair("D", "18")
                    )

                    days.forEach { (weekday, dayNum) ->
                        val isSelected = dayNum == selectedDay
                        val hasDeadline = dayNum in listOf("16", "18")
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { selectedDay = dayNum }
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
                                    .size(34.dp)
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

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Chips Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EducateNavy,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = EducateTextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (selectedFilter == filter) EducateNavy else EducateBorder,
                            selectedBorderColor = EducateNavy,
                            enabled = true,
                            selected = selectedFilter == filter
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Cronograma de actividades",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = EducateTextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Items List
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                filteredItems.forEach { item ->
                    AgendaItemCard(
                        item = item,
                        onClick = {
                            if (item.type == "Tareas") {
                                onOpenTask()
                            } else {
                                onOpenCourse()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AgendaItemCard(
    item: AgendaItem,
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
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date Badge Box
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (item.isPending) EducateContainerBlue else EducateSuccessBg
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.dayNumber,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isPending) EducateNavy else EducateSuccessGreen
                    )
                    Text(
                        text = item.monthText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (item.isPending) EducateNavy else EducateSuccessGreen
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val icon: ImageVector = when (item.type) {
                        "Tareas" -> Icons.Default.Assignment
                        "Foros" -> Icons.AutoMirrored.Filled.Chat
                        "Tutorías" -> Icons.Default.VideoCameraFront
                        else -> Icons.Default.Quiz
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = EducateTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${item.course} · ${item.timeText}",
                        fontSize = 11.sp,
                        color = EducateTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateTextPrimary
                )

                if (item.points != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (item.isPending) "Valor: ${item.points} puntos · Pendiente" else "Completada · Evaluada",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (item.isPending) EducatePendingOrange else EducateSuccessGreen
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Abrir",
                tint = EducateNavy,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
