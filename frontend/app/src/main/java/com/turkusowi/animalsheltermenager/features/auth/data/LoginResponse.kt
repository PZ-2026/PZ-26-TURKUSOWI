package com.turkusowi.animalsheltermenager.features.auth.data

data class LoginResponse(
    val token: String,
    val type: String = "Bearer",
    val id: String? = null,
    val email: String? = null,
    val username: String? = null,
    val roles: List<String> = emptyList()
)
