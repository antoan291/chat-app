package com.plcoding.chirp.domain.exception

class RateLimitException(
    val resetsInSeconds: Long
): RuntimeException(
    "Rate limit exceeded for Please try again $resetsInSeconds seconds"
)