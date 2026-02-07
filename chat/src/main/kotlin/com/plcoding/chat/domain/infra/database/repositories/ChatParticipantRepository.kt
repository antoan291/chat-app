package com.plcoding.chat.domain.infra.database.repositories

import com.plcoding.chat.domain.infra.database.entities.ChatParticipantEntity
import com.plcoding.chirp.domain.type.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ChatParticipantRepository: JpaRepository<ChatParticipantEntity, UserId> {
    fun findByUserIdIn(userIds: List<UserId>): Set<ChatParticipantEntity>

    @Query("""
        SELECT p
        FROM ChatParticipantEntity p
        WHERE LOWER(p.username) = :query OR LOWER(p.email) = :query
    """

    )
    fun findByEmailOrUsername(query: String): ChatParticipantEntity?

}