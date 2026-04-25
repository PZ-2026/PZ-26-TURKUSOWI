package com.turkusowi.animalsheltermenager.features.auth.data

import com.turkusowi.animalsheltermenager.core.network.RetrofitClient
import okhttp3.Credentials
import retrofit2.Response

class AuthRepository {
    private val authApi = RetrofitClient.createService(AuthApi::class.java)

    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        // Generujemy dynamicznie nagłówek Basic Auth na podstawie wpisanych danych
        val authHeader = Credentials.basic(request.email, request.password)
        return authApi.login(authHeader, request)
    }
}
