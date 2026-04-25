package com.turkusowi.animalsheltermenager.features.auth.data

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    @SerializedName("haslo")
    val password: String
)
