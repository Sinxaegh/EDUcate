package com.example.educate.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educate.data.model.CourseCatalogItem
import com.example.educate.ui.components.EDUcateBottomNavBar
import com.example.educate.ui.theme.EducateBlueBg
import com.example.educate.ui.theme.EducateBorder
import com.example.educate.ui.theme.EducateContainerBlue
import com.example.educate.ui.theme.EducateNavy
import com.example.educate.ui.theme.EducateSuccessBg
import com.example.educate.ui.theme.EducateSuccessGreen
import com.example.educate.ui.theme.EducateTextPrimary
import com.example.educate.ui.theme.EducateTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreCoursesScreen(
    catalog: List<CourseCatalogItem>,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    onSelectCourse: (CourseCatalogItem) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tecnología") }
    val categories = listOf("Tecnología", "Matemáticas", "Idiomas")

    val filteredCatalog = catalog.filter { item ->
        (selectedCategory.isEmpty() || item.category == selectedCategory) &&
                (searchQuery.isEmpty() || item.title.contains(searchQuery, ignoreCase = true))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Explorar Cursos",
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
                currentRoute = "explore_courses",
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
            // Search Input with Filter Icon
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("¿Qué quieres aprender?", color = EducateTextSecondary) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = EducateTextSecondary)
                },
                trailingIcon = {
                    Icon(Icons.Default.FilterList, contentDescription = "Filtros", tint = EducateTextSecondary)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = EducateBorder,
                    focusedBorderColor = EducateNavy
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Filter Chips Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = cat == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = if (isSelected) "" else cat },
                        label = {
                            Text(
                                text = cat,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EducateNavy,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = EducateTextPrimary
                        ),
                        shape = RoundedCornerShape(20.dp),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = EducateBorder,
                            selectedBorderColor = EducateNavy
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cursos destacados",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateTextPrimary
                )
                Text(
                    text = "Inscripciones abiertas",
                    fontSize = 12.sp,
                    color = EducateTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Course Cards List
            filteredCatalog.forEach { item ->
                CourseCatalogCard(
                    course = item,
                    onEnroll = { onSelectCourse(item) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CourseCatalogCard(
    course: CourseCatalogItem,
    onEnroll: () -> Unit
) {
    var isEnrolled by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Image Banner Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(EducateNavy),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (course.category == "Tecnología") Icons.Default.Code else Icons.Default.Functions,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(56.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Badges Row
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = EducateSuccessBg
                    ) {
                        Text(
                            text = "100% Gratuito",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateSuccessGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = EducateContainerBlue
                    ) {
                        Text(
                            text = "Profesor Voluntario",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateNavy,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = course.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateTextPrimary
                )

                Text(
                    text = "${course.instructor} · ${course.level} · ${course.duration}",
                    fontSize = 12.sp,
                    color = EducateTextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                )

                Button(
                    onClick = {
                        isEnrolled = true
                        onEnroll()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isEnrolled) EducateSuccessGreen else EducateNavy
                    )
                ) {
                    Text(
                        text = if (isEnrolled) "Inscrito ✓" else "Inscribirse",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
