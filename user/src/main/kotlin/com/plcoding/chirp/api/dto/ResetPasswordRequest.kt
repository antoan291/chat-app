package com.plcoding.chirp.api.dto

import com.plcoding.chirp.api.util.Password
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class ResetPasswordRequest(
    @field:NotBlank
    val token: String,
    @field:Password
    val newPassword: String
)
