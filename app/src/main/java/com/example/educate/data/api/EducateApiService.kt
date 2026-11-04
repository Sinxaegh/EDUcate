package com.example.educate.data.api

import com.example.educate.data.model.ActividadEntregaResponse
import com.example.educate.data.model.CalificacionRequest
import com.example.educate.data.model.CalificacionesResponse
import com.example.educate.data.model.DonacionRequest
import com.example.educate.data.model.DonacionResponse
import com.example.educate.data.model.LoginRequest
import com.example.educate.data.model.RegisterRequest
import com.example.educate.data.model.StudentDashboardResponse
import com.example.educate.data.model.TeacherPanelResponse
import com.example.educate.data.model.UsuarioResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface EducateApiService {

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<UsuarioResponse>

    @POST("api/v1/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<UsuarioResponse>

    @GET("api/v1/cursos/estudiante/{id_estudiante}")
    suspend fun getStudentDashboard(
        @Path("id_estudiante") idEstudiante: Int
    ): Response<StudentDashboardResponse>

    @Multipart
    @POST("api/v1/actividades/entregar")
    suspend fun entregarActividad(
        @Part("id_estudiante") idEstudiante: RequestBody,
        @Part("id_actividad") idActividad: RequestBody,
        @Part archivo: MultipartBody.Part
    ): Response<ActividadEntregaResponse>

    @GET("api/v1/calificaciones/{id_estudiante}")
    suspend fun getCalificaciones(
        @Path("id_estudiante") idEstudiante: Int
    ): Response<CalificacionesResponse>

    @GET("api/v1/profesor/panel/{id_profesor}")
    suspend fun getTeacherPanel(
        @Path("id_profesor") idProfesor: Int
    ): Response<TeacherPanelResponse>

    @POST("api/v1/profesor/calificar")
    suspend fun calificarEntrega(
        @Body request: CalificacionRequest
    ): Response<Map<String, Any>>

    @POST("api/v1/donaciones/procesar")
    suspend fun procesarDonacion(
        @Body request: DonacionRequest
    ): Response<DonacionResponse>
}
