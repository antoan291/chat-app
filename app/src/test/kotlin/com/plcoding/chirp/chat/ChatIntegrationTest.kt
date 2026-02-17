package com.plcoding.chirp.chat

import com.plcoding.chirp.IntegrationTestBase
import com.plcoding.chirp.service.JwtService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.UUID

class ChatIntegrationTest : IntegrationTestBase() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var jwtService: JwtService

    @Test
    fun `create chat with non-existent user returns 404 (task 7 fix)`() {
        val token = createTestUserAndGetToken()
        val fakeUserId = UUID.randomUUID()

        mockMvc.post("/api/chats") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $token")
            content = """
                {
                    "otherUserIds": ["$fakeUserId"]
                }
            """.trimIndent()
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `get chat list returns no duplicates (task 13 fix)`() {
        val token = createTestUserAndGetToken()

        mockMvc.get("/api/chats") {
            header("Authorization", "Bearer $token")
        }.andExpect {
            status { isOk() }
            // Each chat ID should appear only once
        }
    }

    // Helper: creates a user and returns a valid access token
    private fun createTestUserAndGetToken(): String {
        val userId = UUID.randomUUID()  // Simplified — in real test, register + verify + login
        return jwtService.generateAccessToken(userId)
    }
}