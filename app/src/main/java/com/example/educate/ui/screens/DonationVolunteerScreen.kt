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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educate.ui.components.EDUcateBottomNavBar
import com.example.educate.ui.navigation.Screen
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
fun DonationVolunteerScreen(
    isDonating: Boolean,
    donationMessage: String?,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    onProcessDonation: (Double, String, String) -> Unit,
    onApplyAsTeacher: () -> Unit
) {
    val context = LocalContext.current
    var selectedAmount by remember { mutableDoubleStateOf(100.0) }
    var emailInput by remember { mutableStateOf("ana.lopez@correo.com") }
    val amounts = listOf(50.0, 100.0, 200.0)

    var showReceiptDialog by remember { mutableStateOf(false) }
    var lastReceiptId by remember { mutableStateOf("txn_educate_998877") }

    var showTeacherApplicationDialog by remember { mutableStateOf(false) }
    var teacherSubject by remember { mutableStateOf("Matemáticas y Estadística") }
    var teacherHours by remember { mutableStateOf("4 horas semanales") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Apoya a EDUcate",
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
                        Toast.makeText(context, "Iniciativa alineada al ODS 17 · Alianzas para el Desarrollo", Toast.LENGTH_SHORT).show()
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
                currentRoute = Screen.DonationVolunteer.route,
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
            // Hero Navy Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EducateNavy)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Handshake,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ODS 17 · ALIANZAS PARA LOGRAR LOS OBJETIVOS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.8f),
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Educación de calidad gratuita para todos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tu apoyo mantiene el aprendizaje abierto y conecta a más personas con nuevas oportunidades.",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Card 1: Haz una donación
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Haz una donación",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateTextPrimary
                        )
                        Text(
                            text = "MXN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EducateTextSecondary
                        )
                    }

                    Text(
                        text = "Elige tu aportación única",
                        fontSize = 12.sp,
                        color = EducateTextSecondary,
                        modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                    )

                    // Amount Selection Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        amounts.forEach { amount ->
                            val isSelected = amount == selectedAmount
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) EducateNavy else Color(0xFFF1F5F9))
                                    .clickable { selectedAmount = amount },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$${amount.toInt()}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else EducateTextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Correo electrónico para tu recibo") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = EducateBorder,
                            focusedBorderColor = EducateNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            lastReceiptId = "txn_educate_${System.currentTimeMillis().toString().takeLast(6)}"
                            onProcessDonation(selectedAmount, emailInput, "Stripe")
                            showReceiptDialog = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EducateNavy),
                        enabled = !isDonating
                    ) {
                        if (isDonating) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Donar con Stripe/PayPal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = EducateTextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Pago seguro · Sin cargos recurrentes",
                            fontSize = 11.sp,
                            color = EducateTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Card 2: Sé un Profesor Voluntario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Sé un Profesor Voluntario",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateTextPrimary
                    )

                    Text(
                        text = "Comparte lo que sabes. Dona tu tiempo y acompaña a estudiantes a alcanzar sus metas.",
                        fontSize = 13.sp,
                        color = EducateTextSecondary,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    OutlinedButton(
                        onClick = { showTeacherApplicationDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VolunteerActivism, contentDescription = null, tint = EducateNavy, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Postularme como Docente",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = EducateNavy
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // --- DIALOG: RECIBO DIGITAL DE DONACIÓN ---
        if (showReceiptDialog) {
            AlertDialog(
                onDismissRequest = { showReceiptDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EducateSuccessGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("¡Gracias por tu apoyo!", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = EducateNavy)
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Tu donación de $${selectedAmount.toInt()} MXN ha sido procesada exitosamente.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EducateTextPrimary
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EducateContainerBlue,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Comprobante de donación", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EducateNavy)
                                Text("ID Transacción: $lastReceiptId", fontSize = 10.sp, color = EducateNavy)
                                Text("Correo de recibo: $emailInput", fontSize = 10.sp, color = EducateNavy)
                                Text("Destino: Fondo de Becas y Plataforma EDUcate (ODS 17)", fontSize = 10.sp, color = EducateNavy)
                            }
                        }
                        Text(
                            text = "Se ha enviado el comprobante digital fiscal a tu correo.",
                            fontSize = 11.sp,
                            color = EducateTextSecondary
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showReceiptDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }

        // --- DIALOG: POSTULACIÓN DE PROFESOR VOLUNTARIO ---
        if (showTeacherApplicationDialog) {
            AlertDialog(
                onDismissRequest = { showTeacherApplicationDialog = false },
                title = {
                    Text("Postulación de Docente Voluntario", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = EducateNavy)
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Únete a la red docente de EDUcate y ayuda a miles de alumnos:",
                            fontSize = 12.sp,
                            color = EducateTextSecondary
                        )
                        OutlinedTextField(
                            value = teacherSubject,
                            onValueChange = { teacherSubject = it },
                            label = { Text("Área o Asignatura a impartir") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = teacherHours,
                            onValueChange = { teacherHours = it },
                            label = { Text("Disponibilidad estimada semanal") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showTeacherApplicationDialog = false
                            onApplyAsTeacher()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
                    ) {
                        Text("Completar y acceder al Panel")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTeacherApplicationDialog = false }) {
                        Text("Cancelar", color = EducateTextSecondary)
                    }
                }
            )
        }
    }
}
