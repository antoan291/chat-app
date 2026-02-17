package com.plcoding.chirp.security

import com.plcoding.chirp.IntegrationTestBase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

class SecurityConfigTest : IntegrationTestBase() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `public auth endpoints are accessible without token`() {
        mockMvc.post("/api/auth/login").andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `protected endpoints require token`() {
        mockMvc.get("/api/chats").andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    fun `change-password requires token`() {
        mockMvc.post("/api/auth/change-password").andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    fun `debug endpoints are not accessible`() {
        mockMvc.get("/api/debug/redis-config").andExpect {
            // Should be 401 (deleted) or 404 — never 200
            status { isUnauthorized() }
        }
    }
}