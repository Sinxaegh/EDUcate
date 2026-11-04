package com.example.educate.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object StudentHome : Screen("student_home")
    object CourseDetail : Screen("course_detail")
    object ActivitySubmission : Screen("activity_submission")
    object GradesAnalytics : Screen("grades_analytics")
    object ExploreCourses : Screen("explore_courses")
    object LessonPlayer : Screen("lesson_player")
    object TeacherDashboard : Screen("teacher_dashboard")
    object DonationVolunteer : Screen("donation_volunteer")
    object Agenda : Screen("agenda")
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Inicio : BottomNavItem(Screen.StudentHome.route, "Inicio", Icons.Default.Home)
    object Cursos : BottomNavItem(Screen.CourseDetail.route, "Cursos", Icons.Default.Book)
    object Agenda : BottomNavItem(Screen.Agenda.route, "Agenda", Icons.Default.CalendarMonth)
    object Notas : BottomNavItem(Screen.GradesAnalytics.route, "Notas", Icons.Default.ShowChart)
}
