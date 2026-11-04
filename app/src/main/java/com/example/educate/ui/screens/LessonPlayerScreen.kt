package com.example.educate.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.viewinterop.AndroidView
import com.example.educate.data.model.LessonData
import com.example.educate.data.model.LessonResource
import com.example.educate.ui.components.EDUcateBottomNavBar
import com.example.educate.ui.theme.EducateBlueBg
import com.example.educate.ui.theme.EducateContainerBlue
import com.example.educate.ui.theme.EducateNavy
import com.example.educate.ui.theme.EducateTextPrimary
import com.example.educate.ui.theme.EducateTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonPlayerScreen(
    lesson: LessonData,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    onCompleteAndContinue: () -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Recursos PDF", "Transcripción")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi lección",
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
                        Toast.makeText(context, "Lección interactiva · ${lesson.title}", Toast.LENGTH_SHORT).show()
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
                currentRoute = "lesson_player",
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
            // Header Titles
            Text(
                text = lesson.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = EducateNavy
            )
            Text(
                text = lesson.course,
                fontSize = 13.sp,
                color = EducateTextSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            // Video Player Card (YouTube WebView Player)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EducateContainerBlue)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color.Black)
                    ) {
                        if (isPlaying) {
                            AndroidView(
                                factory = { ctx ->
                                    WebView(ctx).apply {
                                        settings.javaScriptEnabled = true
                                        settings.loadWithOverviewMode = true
                                        settings.useWideViewPort = true
                                        settings.domStorageEnabled = true
                                        webViewClient = WebViewClient()
                                        val htmlData = """
                                            <!DOCTYPE html>
                                            <html>
                                            <head>
                                                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                                <style>
                                                    body { margin: 0; background: #000; display: flex; justify-content: center; align-items: center; height: 100vh; }
                                                    iframe { width: 100%; height: 100%; border: none; }
                                                </style>
                                            </head>
                                            <body>
                                                <iframe src="https://www.youtube.com/embed/${lesson.youtubeVideoId}?autoplay=1&rel=0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                                            </body>
                                            </html>
                                        """.trimIndent()
                                        loadDataWithBaseURL("https://www.youtube.com", htmlData, "text/html", "UTF-8", null)
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(EducateContainerBlue)
                                    .clickable { isPlaying = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFDC2626)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Reproducir YouTube",
                                            tint = Color.White,
                                            modifier = Modifier.size(34.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Ver Video de YouTube (sb7-230Bgdc)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EducateNavy
                                    )
                                    Text(
                                        text = "Haz clic para iniciar reproducción",
                                        fontSize = 11.sp,
                                        color = EducateTextSecondary
                                    )
                                }
                            }
                        }
                    }

                    // Video Control Bar
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = EducateNavy
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { isPlaying = !isPlaying },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isPlaying) "Reproduciendo YouTube" else "Pausado · Toca para reproducir",
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }

                            Text(
                                text = "YouTube · ${lesson.durationText}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Lesson Info Row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = EducateContainerBlue
                ) {
                    Text(
                        text = lesson.weekText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateNavy,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Video interactivo · ${lesson.durationText}",
                    fontSize = 12.sp,
                    color = EducateTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Tabs Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = EducateNavy,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = EducateNavy
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTabIndex == index) EducateNavy else EducateTextSecondary
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedTabIndex == 0) {
                // PDF Resources List
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    lesson.resources.forEach { res ->
                        ResourceCardItem(res)
                    }
                }
            } else {
                // Transcript text
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White
                ) {
                    Text(
                        text = lesson.transcriptText,
                        fontSize = 13.sp,
                        color = EducateTextSecondary,
                        modifier = Modifier.padding(16.dp),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = lesson.summaryText,
                fontSize = 12.sp,
                color = EducateTextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Complete and Continue Button
            Button(
                onClick = onCompleteAndContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
            ) {
                Text(
                    text = "Completar lección y continuar →",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ResourceCardItem(res: LessonResource) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                Toast.makeText(context, "Abriendo recurso: ${res.title} (${res.size})", Toast.LENGTH_SHORT).show()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(EducateContainerBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Article,
                    contentDescription = null,
                    tint = EducateNavy,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = res.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateTextPrimary
                )
                Text(
                    text = "${res.info} · ${res.size}",
                    fontSize = 12.sp,
                    color = EducateTextSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Descargar",
                tint = EducateNavy,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
