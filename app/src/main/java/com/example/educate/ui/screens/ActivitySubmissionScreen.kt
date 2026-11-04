package com.example.educate.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educate.ui.components.EDUcateBottomNavBar
import com.example.educate.ui.theme.EducateBlueBg
import com.example.educate.ui.theme.EducateBorder
import com.example.educate.ui.theme.EducateContainerBlue
import com.example.educate.ui.theme.EducateNavy
import com.example.educate.ui.theme.EducateTextPrimary
import com.example.educate.ui.theme.EducateTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitySubmissionScreen(
    selectedFileUri: Uri?,
    selectedFileName: String?,
    selectedFileSizeText: String?,
    isSubmitting: Boolean,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    onFileSelected: (Uri, String, String) -> Unit,
    onClearFile: () -> Unit,
    onConfirmSubmission: () -> Unit
) {
    val context = LocalContext.current

    // File picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            var fileName = "Informe_descriptivo.pdf"
            var sizeText = "2,4 MB"
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (cursor.moveToFirst()) {
                    if (nameIndex != -1) fileName = cursor.getString(nameIndex)
                    if (sizeIndex != -1) {
                        val sizeBytes = cursor.getLong(sizeIndex)
                        sizeText = String.format("%.1f MB", sizeBytes.toDouble() / (1024 * 1024))
                    }
                }
            }
            onFileSelected(uri, fileName, sizeText)
        }
    }

    val displayFileName = selectedFileName ?: "Informe_descriptivo.pdf"
    val displaySize = selectedFileSizeText ?: "2,4 MB"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Entrega de actividad",
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
                currentRoute = "activity_submission",
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
            // Overline category
            Text(
                text = "ESTADÍSTICA APLICADA · TAREA 2",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EducateTextSecondary,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Main Title
            Text(
                text = "Informe descriptivo",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = EducateNavy
            )

            Text(
                text = "Trabajo individual · Valor: 20 puntos",
                fontSize = 13.sp,
                color = EducateTextSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            // Deadline Alert Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = EducateContainerBlue
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = EducateNavy,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Fecha límite: 16 oct 2026, 23:59",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateNavy
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Instructions Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "¿Qué debes entregar?",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateTextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "1. Analiza los datos de la encuesta de clase.\n2. Calcula media, mediana y dispersión.\n3. Incluye una gráfica y tus conclusiones.",
                        fontSize = 13.sp,
                        color = EducateTextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // File Selector Card (Dashed Border style)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, EducateBorder, RoundedCornerShape(16.dp))
                    .clickable { launcher.launch("*/*") }
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "Cargar archivo",
                        tint = EducateNavy,
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Seleccionar archivo",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateTextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Format Chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FileTypeBadge("PDF")
                        FileTypeBadge("ZIP")
                        FileTypeBadge("DOCX")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Máximo 25MB",
                        fontSize = 12.sp,
                        color = EducateTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selected File Box (Always visible or when file exists)
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
                            .clip(RoundedCornerShape(10.dp))
                            .background(EducateContainerBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = EducateNavy,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = displayFileName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateTextPrimary
                        )
                        Text(
                            text = "$displaySize · Listo para enviar",
                            fontSize = 12.sp,
                            color = EducateTextSecondary
                        )
                    }

                    IconButton(onClick = onClearFile) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Quitar archivo",
                            tint = EducateTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Primary Confirm Button
            Button(
                onClick = onConfirmSubmission,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EducateNavy),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Confirmar Entrega",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Al confirmar, el archivo se enviará a tu docente.",
                fontSize = 12.sp,
                color = EducateTextSecondary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun FileTypeBadge(type: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = EducateContainerBlue
    ) {
        Text(
            text = type,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = EducateNavy,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}
