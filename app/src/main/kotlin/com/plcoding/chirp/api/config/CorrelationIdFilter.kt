package com.plcoding.chirp.api.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

// Assigns a unique correlation ID to every request.
// This ID appears in every log line for that request,
// making it easy to trace a complete request flow in production logs.
//
// How it works:
//   1. Checks if the client sent an X-Request-Id header (useful for mobile app debugging)
//   2. If not, generates a new UUID
//   3. Puts it in SLF4J's MDC so every logger includes it automatically
//   4. Adds it to the response header so the client can reference it in bug reports
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorrelationIdFilter : OncePerRequestFilter() {

    companion object {
        const val CORRELATION_ID_HEADER = "X-Request-Id"
        const val CORRELATION_ID_MDC_KEY = "correlationId"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val correlationId = request.getHeader(CORRELATION_ID_HEADER)
            ?: UUID.randomUUID().toString()

        try {
            MDC.put(CORRELATION_ID_MDC_KEY, correlationId)
            response.setHeader(CORRELATION_ID_HEADER, correlationId)
            filterChain.doFilter(request, response)
        } finally {
            // Always clean up MDC to prevent leaking between requests
            MDC.remove(CORRELATION_ID_MDC_KEY)
        }
    }
}