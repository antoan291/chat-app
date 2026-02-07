package com.plcoding.chat.domain.infra.database.repositories

import com.plcoding.chat.domain.infra.database.entities.ChatEntity
import com.plcoding.chirp.domain.type.ChatId
import com.plcoding.chirp.domain.type.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ChatRepository: JpaRepository<ChatEntity, ChatId> {
    @Query("""
        SELECT c
        FROM ChatEntity c
        LEFT JOIN FETCH c.participants
        LEFT JOIN FETCH c.creator
        WHERE c.id = :id
        AND EXISTS (
            SELECT 1
            FROM c.participants
            WHERE p.userId = :userId
        )
    """)
    fun findChatById(id: ChatId, userId: UserId): ChatEntity?

    @Query("""
        SELECT c
        FROM ChatEntity c
        LEFT JOIN FETCH c.participants   
        LEFT JOIN FETCH c.creator
        WHERE EXISTS (
            SELECT 1
            WHERE p.userId = :userId
        )   
    """)

    fun findAllByUserId(userId: UserId): List<ChatEntity>
}