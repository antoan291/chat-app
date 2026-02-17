package com.plcoding.chirp.infra.database.repositories

import com.plcoding.chirp.infra.database.entities.ChatMessageEntity
import com.plcoding.chirp.domain.type.ChatId
import com.plcoding.chirp.domain.type.ChatMessageId
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface ChatMessageRepository: JpaRepository<ChatMessageEntity, ChatMessageId> {

    @Query("""
        SELECT m
        FROM ChatMessageEntity m
        WHERE m.chatId = :chatId
        AND m.createdAt < :before
        ORDER BY m.createdAt DESC
    """)

    fun findByChatIdBefore(
        chatId: ChatId,
        before: Instant,
        pageable: Pageable
    ): Slice<ChatMessageEntity>

    // This select the messages in order from the last message
    @Query("""
        SELECT m
        FROM ChatMessageEntity m
        LEFT JOIN FETCH m.sender
        WHERE m.chatId IN :chatIds
        AND m.createdAt = (
            SELECT MAX(m2.createdAt)
            FROM ChatMessageEntity m2
            WHERE m2.chatId = m.chatId
        )
    """)
    fun findLatestMessagesByChatIds(
        chatIds: Set<ChatId>
    ): List<ChatMessageEntity>
}