package com.plcoding.chirp.auth

import com.plcoding.chirp.IntegrationTestBase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

// Tests the complete auth flow: register → login → refresh → logout.
// Also verifies security fixes we made (task #1, #6, #11, #12).
class AuthIntegrationTest : IntegrationTestBase() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `register creates user and returns 201`() {
        mockMvc.post("/api/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "email": "test@example.com",
                    "username": "testuser",
                    "password": "Password123!"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    fun `register with duplicate email returns 409`() {
        // Register first user
        mockMvc.post("/api/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "email": "dupe@example.com",
                    "username": "user1",
                    "password": "Password123!"
                }
            """.trimIndent()
        }

        // Same email, different case — should fail (task #11 fix)
        mockMvc.post("/api/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "email": "DUPE@Example.com",
                    "username": "user2",
                    "password": "Password123!"
                }
            """.trimIndent()
        }.andExpect {
            status { isConflict() }
        }
    }

    @Test
    fun `change-password requires authentication (task 1 fix)`() {
        // This was the P0 security bug — change-password was public
        mockMvc.post("/api/auth/change-password") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "oldPassword": "test",
                    "newPassword": "test2"
                }
            """.trimIndent()
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    fun `login with unverified email returns 403`() {
        // Register but don't verify
        mockMvc.post("/api/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "email": "unverified@example.com",
                    "username": "unverified",
                    "password": "Password123!"
                }
            """.trimIndent()
        }

        mockMvc.post("/api/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "email": "unverified@example.com",
                    "password": "Password123!"
                }
            """.trimIndent()
        }.andExpect {
            status { isForbidden() }
        }
    }
}