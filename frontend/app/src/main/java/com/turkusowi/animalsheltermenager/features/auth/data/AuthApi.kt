package com.turkusowi.animalsheltermenager.features.auth.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("api/uzytkownicy/login")
    suspend fun login(
        @Header("Authorization") authHeader: String,
        @Body request: LoginRequest
    ): Response<LoginResponse>
}
