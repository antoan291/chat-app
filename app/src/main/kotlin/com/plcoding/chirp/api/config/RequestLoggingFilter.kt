package com.plcoding.chirp.api.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

// Logs every request with method, path, status code, and duration.
// Combined with CorrelationIdFilter, every log line is traceable.
//
// Example output:
//   [abc-123] --> POST /api/chat/messages
//   [abc-123] <-- 201 CREATED (45ms)
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)  // Runs right after CorrelationIdFilter
class RequestLoggingFilter : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val startTime = System.currentTimeMillis()
        val method = request.method
        val path = request.requestURI
        val query = request.queryString?.let { "?$it" } ?: ""

        log.info("--> {} {}{}", method, path, query)

        try {
            filterChain.doFilter(request, response)
        } finally {
            val duration = System.currentTimeMillis() - startTime
            log.info("<-- {} {} ({}ms)", response.status, path, duration)
        }
    }
}