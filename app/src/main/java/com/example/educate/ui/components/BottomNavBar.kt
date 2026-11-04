package com.example.educate.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educate.ui.navigation.Screen
import com.example.educate.ui.theme.EducateNavy
import com.example.educate.ui.theme.EducateTextMuted
import com.example.educate.ui.theme.EducateTextSecondary

@Composable
fun EDUcateBottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val isInicio = currentRoute == Screen.StudentHome.route || currentRoute == Screen.TeacherDashboard.route
        val isCursos = currentRoute == Screen.CourseDetail.route || currentRoute == Screen.ExploreCourses.route || currentRoute == Screen.LessonPlayer.route || currentRoute == Screen.ActivitySubmission.route
        val isAgenda = currentRoute == Screen.Agenda.route
        val isNotas = currentRoute == Screen.GradesAnalytics.route

        NavigationBarItem(
            selected = isInicio,
            onClick = { onNavigate(Screen.StudentHome.route) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EducateNavy,
                selectedTextColor = EducateNavy,
                indicatorColor = Color(0xFFEBF3FA),
                unselectedIconColor = EducateTextMuted,
                unselectedTextColor = EducateTextSecondary
            )
        )

        NavigationBarItem(
            selected = isCursos,
            onClick = { onNavigate(Screen.CourseDetail.route) },
            icon = { Icon(Icons.Default.Book, contentDescription = "Cursos") },
            label = { Text("Cursos", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EducateNavy,
                selectedTextColor = EducateNavy,
                indicatorColor = Color(0xFFEBF3FA),
                unselectedIconColor = EducateTextMuted,
                unselectedTextColor = EducateTextSecondary
            )
        )

        NavigationBarItem(
            selected = isAgenda,
            onClick = { onNavigate(Screen.Agenda.route) },
            icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Agenda") },
            label = { Text("Agenda", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EducateNavy,
                selectedTextColor = EducateNavy,
                indicatorColor = Color(0xFFEBF3FA),
                unselectedIconColor = EducateTextMuted,
                unselectedTextColor = EducateTextSecondary
            )
        )

        NavigationBarItem(
            selected = isNotas,
            onClick = { onNavigate(Screen.GradesAnalytics.route) },
            icon = { Icon(Icons.AutoMirrored.Filled.ShowChart, contentDescription = "Notas") },
            label = { Text("Notas", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EducateNavy,
                selectedTextColor = EducateNavy,
                indicatorColor = Color(0xFFEBF3FA),
                unselectedIconColor = EducateTextMuted,
                unselectedTextColor = EducateTextSecondary
            )
        )
    }
}
