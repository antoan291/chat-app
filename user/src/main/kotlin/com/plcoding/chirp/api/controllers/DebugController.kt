package com.plcoding.chirp.api.controllers

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/debug")
class DebugController(
    @Value("\${spring.data.redis.host}") private val redisHost: String,
    @Value("\${spring.data.redis.port}") private val redisPort: Int,
    @Value("\${spring.data.redis.password:NOT_SET}") private val redisPassword: String
) {
    @GetMapping("/redis-config")
    fun getRedisConfig() = mapOf(
        "host" to redisHost,
        "port" to redisPort,
        "passwordLength" to redisPassword.length,
        "passwordFirst3Chars" to redisPassword.take(3),
        "passwordSet" to (redisPassword != "NOT_SET")
    )
}