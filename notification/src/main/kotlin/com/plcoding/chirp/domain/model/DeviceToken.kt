package com.plcoding.chirp.domain.model

import com.plcoding.chirp.domain.type.UserId
import org.apache.catalina.User
import java.time.Instant

data class DeviceToken(
    val id: Long,
    val userId: UserId,
    val token: String,
    val platform: Platform,
    val createdAt: Instant = Instant.now(),
){
    enum class Platform{
        ANDROID,IOS
    }
}
