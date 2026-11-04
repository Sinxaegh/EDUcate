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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educate.data.model.CourseModule
import com.example.educate.data.model.ForumPost
import com.example.educate.data.model.ItemType
import com.example.educate.data.model.ModuleLesson
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
fun CourseDetailScreen(
    currentUser: com.example.educate.data.model.UsuarioResponse?,
    modules: List<CourseModule>,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    onOpenLesson: () -> Unit,
    onOpenLessonWithId: (Int) -> Unit = {},
    onOpenSubmission: () -> Unit,
    onToggleModule: (Int) -> Unit,
    initialTab: Int = 0
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(initialTab) }
    val tabTitles = listOf("Contenido", "Foro", "Información")

    // Forum posts in memory
    val forumPosts = remember {
        mutableStateListOf(
            ForumPost(
                id = 1,
                author = "Dra. Elena Ruiz",
                authorRole = "Profesor",
                avatarInitials = "ER",
                date = "Hoy, 10:15",
                content = "Estimados alumnos: para la Tarea 2 recuerden justificar claramente si la media o la mediana es la medida más representativa ante valores atípicos. ¡Éxito!",
                likesCount = 5
            ),
            ForumPost(
                id = 2,
                author = "Ana López",
                authorRole = "Estudiante",
                avatarInitials = "AL",
                date = "Hoy, 11:30",
                content = "Profesora, en la encuesta del Grupo A la distribución tiene un sesgo a la derecha, por lo que incluí el diagrama de caja y bigotes. ¿Eso cuenta como gráfico adicional?",
                likesCount = 3
            ),
            ForumPost(
                id = 3,
                author = "Luis García",
                authorRole = "Estudiante",
                avatarInitials = "LG",
                date = "Ayer, 16:45",
                content = "Hola compañeros, ¿qué fórmula usaron para la varianza muestral? Recuerden dividir entre (n-1).",
                likesCount = 2
            )
        )
    }

    var newCommentText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi curso",
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
                        Toast.makeText(context, "Estadística aplicada (Grupo A) · Activo", Toast.LENGTH_SHORT).show()
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
                currentRoute = Screen.CourseDetail.route,
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
        ) {
            // --- COURSE HEADER ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Estadística aplicada",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateNavy
                )
                Text(
                    text = "Dra. Elena Ruiz · Grupo A · 2026-II",
                    fontSize = 13.sp,
                    color = EducateTextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LinearProgressIndicator(
                        progress = { 0.67f },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(CircleShape),
                        color = EducateNavy,
                        trackColor = EducateContainerBlue
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "4 de 6 temas · 67%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateNavy
                    )
                }
            }

            // --- TABS (Contenido, Foro, Información) ---
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = EducateNavy,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = EducateNavy
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTabIndex == index) EducateNavy else EducateTextSecondary
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- TAB CONTENT ---
            when (selectedTabIndex) {
                0 -> {
                    // TAB 0: CONTENIDO (MÓDULOS)
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        modules.forEach { module ->
                            ModuleCardItem(
                                module = module,
                                onToggle = { onToggleModule(module.id) },
                                onOpenLesson = onOpenLesson,
                                onOpenSubmission = onOpenSubmission,
                                onOpenForum = { selectedTabIndex = 1 }
                            )
                        }
                    }
                }

                1 -> {
                    // TAB 1: FORO DE DISCUSIÓN
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Chat,
                                        contentDescription = null,
                                        tint = EducateNavy,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Foro · Interpretar los datos",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EducateNavy
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Semana 2 · Cierra el 18 oct, 18:00 · 8 aportes activos",
                                    fontSize = 12.sp,
                                    color = EducateTextSecondary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Pregunta detonadora: ¿En qué circunstancias prácticas de la toma de decisiones la media aritmética puede conducir a conclusiones engañosas en comparación con la mediana?",
                                    fontSize = 13.sp,
                                    color = EducateTextPrimary,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Aportes de la comunidad (${forumPosts.size})",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateTextPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Posts list
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            forumPosts.forEach { post ->
                                ForumPostCard(post)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // New Post Input Field
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Escribe tu aporte o responde a tus compañeros:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = EducateTextPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = newCommentText,
                                    onValueChange = { newCommentText = it },
                                    placeholder = { Text("Comparte tu análisis o consulta...", fontSize = 13.sp, color = EducateTextSecondary) },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = EducateBorder,
                                        focusedBorderColor = EducateNavy
                                    )
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        if (newCommentText.isNotBlank()) {
                                            val authorName = currentUser?.nombre ?: "Ana López"
                                            val authorRoleStr = if (currentUser?.rol == "profesor") "Profesor" else "Estudiante"
                                            val authorInitials = authorName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                                            forumPosts.add(
                                                ForumPost(
                                                    id = forumPosts.size + 1,
                                                    author = authorName,
                                                    authorRole = authorRoleStr,
                                                    avatarInitials = authorInitials,
                                                    date = "Justo ahora",
                                                    content = newCommentText.trim(),
                                                    likesCount = 1
                                                )
                                            )
                                            newCommentText = ""
                                            Toast.makeText(context, "¡Aporte publicado en el foro con éxito!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.End),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = EducateNavy),
                                    enabled = newCommentText.isNotBlank()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Send,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Publicar aporte", fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // TAB 2: INFORMACIÓN DEL CURSO
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Descripción
                        InfoSectionCard(
                            icon = Icons.Default.Info,
                            title = "Descripción del curso"
                        ) {
                            Text(
                                text = "El curso de Estadística Aplicada ofrece al estudiante de educación superior los fundamentos metodológicos y analíticos para la recopilación, organización, síntesis e interpretación de datos empíricos.",
                                fontSize = 13.sp,
                                color = EducateTextSecondary,
                                lineHeight = 18.sp
                            )
                        }

                        // Docente
                        InfoSectionCard(
                            icon = Icons.Default.Person,
                            title = "Docente titular"
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(EducateContainerBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "ER",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EducateNavy
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Dra. Elena Ruiz",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EducateTextPrimary
                                    )
                                    Text(
                                        text = "Doctora en Ciencias Matemáticas · Profesora Voluntaria",
                                        fontSize = 12.sp,
                                        color = EducateTextSecondary
                                    )
                                    Text(
                                        text = "elena.ruiz@universidad.edu",
                                        fontSize = 12.sp,
                                        color = EducateNavy
                                    )
                                }
                            }
                        }

                        // Ponderación de Evaluaciones
                        InfoSectionCard(
                            icon = Icons.Default.Schedule,
                            title = "Criterios de evaluación y rúbrica"
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                EvaluationCriteriaRow("Tareas y actividades prácticas", "40%")
                                EvaluationCriteriaRow("Cuestionarios de módulo", "30%")
                                EvaluationCriteriaRow("Participación en foros", "20%")
                                EvaluationCriteriaRow("Proyecto integrador final", "10%")
                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = EducateBorder)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Calificación mínima aprobatoria", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EducateTextPrimary)
                                    Text("70 / 100", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EducateSuccessGreen)
                                }
                            }
                        }

                        // Descarga de Temario / Syllabus
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable {
                                        Toast.makeText(context, "Descargando Syllabus de Estadística Aplicada (PDF, 1.2 MB)", Toast.LENGTH_SHORT).show()
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(EducateContainerBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Download, contentDescription = null, tint = EducateNavy)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Descargar Syllabus Oficial", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EducateTextPrimary)
                                    Text("PDF · Programa de estudios 2026-II · 1.2 MB", fontSize = 12.sp, color = EducateTextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InfoSectionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = EducateNavy, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = EducateNavy)
            }
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun EvaluationCriteriaRow(concept: String, percentage: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(concept, fontSize = 12.sp, color = EducateTextSecondary)
        Surface(shape = RoundedCornerShape(6.dp), color = EducateContainerBlue) {
            Text(percentage, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EducateNavy, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
        }
    }
}

@Composable
private fun ForumPostCard(post: ForumPost) {
    var likes by remember { mutableIntStateOf(post.likesCount) }
    var hasLiked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (post.authorRole == "Profesor") EducateNavy else EducateContainerBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.avatarInitials,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (post.authorRole == "Profesor") Color.White else EducateNavy
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(post.author, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EducateTextPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        if (post.authorRole == "Profesor") {
                            Surface(shape = RoundedCornerShape(4.dp), color = EducateContainerBlue) {
                                Text("Docente", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EducateNavy, modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp))
                            }
                        }
                    }
                    Text(post.date, fontSize = 11.sp, color = EducateTextSecondary)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = post.content,
                fontSize = 13.sp,
                color = EducateTextPrimary,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        if (hasLiked) {
                            likes--
                            hasLiked = false
                        } else {
                            likes++
                            hasLiked = true
                        }
                    }
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Me gusta",
                    tint = if (hasLiked) EducateNavy else EducateTextSecondary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$likes votos útiles",
                    fontSize = 11.sp,
                    color = if (hasLiked) EducateNavy else EducateTextSecondary,
                    fontWeight = if (hasLiked) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun ModuleCardItem(
    module: CourseModule,
    onToggle: () -> Unit,
    onOpenLesson: () -> Unit,
    onOpenSubmission: () -> Unit,
    onOpenForum: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Module Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (module.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completada",
                        tint = EducateSuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                } else if (module.lessons.isEmpty()) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Bloqueada",
                        tint = EducateTextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(EducateContainerBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(EducateNavy)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = module.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateTextPrimary
                    )
                    Text(
                        text = module.statusText,
                        fontSize = 12.sp,
                        color = EducateTextSecondary
                    )
                }

                Icon(
                    imageVector = if (module.isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = EducateTextSecondary
                )
            }

            // Expanded Lesson Sub-Items
            if (module.isExpanded && module.lessons.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    module.lessons.forEach { lesson ->
                        LessonItemRow(
                            lesson = lesson,
                            onOpenLesson = onOpenLesson,
                            onOpenSubmission = onOpenSubmission,
                            onOpenForum = onOpenForum
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonItemRow(
    lesson: ModuleLesson,
    onOpenLesson: () -> Unit,
    onOpenSubmission: () -> Unit,
    onOpenForum: () -> Unit
) {
    if (lesson.isPending && lesson.type == ItemType.TAREA) {
        // Highlighting Task Card (Pendiente de entrega)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenSubmission() },
            shape = RoundedCornerShape(14.dp),
            color = EducateContainerBlue
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFFEF3C7)
                    ) {
                        Text(
                            text = "Pendiente de entrega",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducatePendingOrange,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = lesson.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateNavy
                    )
                    Text(
                        text = lesson.subtitle,
                        fontSize = 12.sp,
                        color = EducateTextSecondary
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Entregar",
                    tint = EducateNavy,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    } else {
        // Standard Resource / Forum Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    if (lesson.type == ItemType.FORO) {
                        onOpenForum()
                    } else {
                        onOpenLesson()
                    }
                }
                .padding(vertical = 8.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (lesson.type == ItemType.FORO) Icons.AutoMirrored.Filled.Chat else Icons.AutoMirrored.Filled.Article,
                contentDescription = null,
                tint = EducateTextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EducateTextPrimary
                )
                Text(
                    text = lesson.subtitle,
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
