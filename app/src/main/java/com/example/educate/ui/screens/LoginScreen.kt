package com.example.educate.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educate.ui.theme.EducateBorder
import com.example.educate.ui.theme.EducateNavy
import com.example.educate.ui.theme.EducateTextPrimary
import com.example.educate.ui.theme.EducateTextSecondary

@Composable
fun LoginScreen(
    isLoggingIn: Boolean,
    errorMessage: String?,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onRoleSelected: (String) -> Unit
) {
    var isRegisterMode by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("ana.lopez@universidad.edu") }
    var password by remember { mutableStateOf("123456") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("estudiante") } // "estudiante" or "profesor"

    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var resetEmailInput by remember { mutableStateOf("") }
    var resetSentMessage by remember { mutableStateOf<String?>(null) }

    var showSupportDialog by remember { mutableStateOf(false) }
    var supportMessageInput by remember { mutableStateOf("") }
    var supportSentMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EducateNavy)
            .verticalScroll(rememberScrollState())
    ) {
        // --- TOP NAVY HERO SECTION ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Logo EDUcate",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "EDUcate",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Aprender nos conecta.",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        // --- BOTTOM WHITE CARD OVERLAY ---
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isRegisterMode) "Crear tu cuenta" else "Bienvenido a tu campus",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateTextPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = if (isRegisterMode) "Regístrate para comenzar a aprender o enseñar." else "Tu aprendizaje, en un solo lugar.",
                    fontSize = 14.sp,
                    color = EducateTextSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 16.dp)
                )

                // Selector de Rol (Estudiante / Profesor)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedRole == "estudiante") EducateNavy else Color.Transparent)
                            .clickable {
                                selectedRole = "estudiante"
                                if (!isRegisterMode) {
                                    email = "ana.lopez@universidad.edu"
                                }
                                onRoleSelected("estudiante")
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Estudiante",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selectedRole == "estudiante") Color.White else EducateTextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedRole == "profesor") EducateNavy else Color.Transparent)
                            .clickable {
                                selectedRole = "profesor"
                                if (!isRegisterMode) {
                                    email = "elena.ruiz@universidad.edu"
                                }
                                onRoleSelected("profesor")
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Profesor Voluntario",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selectedRole == "profesor") Color.White else EducateTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Nombre Completo (solo en modo Registro)
                if (isRegisterMode) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Nombre completo") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = EducateTextSecondary)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = EducateBorder,
                            focusedBorderColor = EducateNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Campo Correo
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo institucional") },
                    leadingIcon = {
                        Icon(Icons.Default.Mail, contentDescription = null, tint = EducateTextSecondary)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = EducateBorder,
                        focusedBorderColor = EducateNavy
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = EducateTextSecondary)
                    },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Mostrar contraseña",
                                tint = EducateTextSecondary
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = EducateBorder,
                        focusedBorderColor = EducateNavy
                    )
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botón Iniciar Sesión / Registrarse
                Button(
                    onClick = {
                        if (isRegisterMode) {
                            val finalName = nameInput.ifBlank { "Usuario Registrado" }
                            onRegister(finalName, email, password, selectedRole)
                        } else {
                            onLogin(email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EducateNavy),
                    enabled = !isLoggingIn
                ) {
                    if (isLoggingIn) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (isRegisterMode) "Crear mi cuenta" else "Iniciar sesión",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Enlace de alternar entre Iniciar Sesión y Registrarse
                Text(
                    text = if (isRegisterMode) "¿Ya tienes una cuenta? Inicia sesión aquí" else "¿No tienes cuenta? Regístrate gratis aquí",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateNavy,
                    modifier = Modifier.clickable {
                        isRegisterMode = !isRegisterMode
                        if (isRegisterMode) {
                            if (email == "ana.lopez@universidad.edu" || email == "elena.ruiz@universidad.edu") {
                                email = ""
                            }
                            if (password == "123456") {
                                password = ""
                            }
                        }
                    }
                )

                if (!isRegisterMode) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EducateTextSecondary,
                        modifier = Modifier.clickable {
                            resetEmailInput = email
                            resetSentMessage = null
                            showForgotPasswordDialog = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = EducateBorder)
                    Text(
                        text = "  o usa tu cuenta institucional  ",
                        fontSize = 12.sp,
                        color = EducateTextSecondary
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = EducateBorder)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { onLogin(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EducateTextPrimary),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                ) {
                    Text(
                        text = "Iniciar sesión con Google/Microsoft",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Acceso para estudiantes y profesores",
                    fontSize = 12.sp,
                    color = EducateTextSecondary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Necesitas ayuda? ",
                        fontSize = 12.sp,
                        color = EducateTextSecondary
                    )
                    Text(
                        text = "Contacta a soporte",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EducateTextPrimary,
                        modifier = Modifier.clickable {
                            supportMessageInput = ""
                            supportSentMessage = null
                            showSupportDialog = true
                        }
                    )
                }
            }
        }
    }

    // --- DIALOG: RECUPERAR CONTRASEÑA ---
    if (showForgotPasswordDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = {
                Text(
                    text = "Recuperar contraseña",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateNavy
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Ingresa tu correo institucional registrado para recibir las instrucciones de restablecimiento:",
                        fontSize = 13.sp,
                        color = EducateTextSecondary
                    )
                    OutlinedTextField(
                        value = resetEmailInput,
                        onValueChange = { resetEmailInput = it },
                        label = { Text("Correo institucional") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    if (resetSentMessage != null) {
                        Text(
                            text = resetSentMessage!!,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF16A34A)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (resetEmailInput.isNotBlank()) {
                            resetSentMessage = "¡Enlace de recuperación enviado a $resetEmailInput!"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
                ) {
                    Text("Enviar enlace")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("Cerrar", color = EducateTextSecondary)
                }
            }
        )
    }

    // --- DIALOG: SOPORTE TÉCNICO ---
    if (showSupportDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showSupportDialog = false },
            title = {
                Text(
                    text = "Contacto con Soporte EDUcate",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EducateNavy
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "¿Tienes inconvenientes con tu cuenta o campus? Déjanos tu mensaje:",
                        fontSize = 13.sp,
                        color = EducateTextSecondary
                    )
                    OutlinedTextField(
                        value = supportMessageInput,
                        onValueChange = { supportMessageInput = it },
                        placeholder = { Text("Describe brevemente tu problema...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )
                    if (supportSentMessage != null) {
                        Text(
                            text = supportSentMessage!!,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF16A34A)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (supportMessageInput.isNotBlank()) {
                            supportSentMessage = "¡Mensaje recibido! Te responderemos pronto."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EducateNavy)
                ) {
                    Text("Enviar mensaje")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showSupportDialog = false }) {
                    Text("Cerrar", color = EducateTextSecondary)
                }
            }
        )
    }
}
