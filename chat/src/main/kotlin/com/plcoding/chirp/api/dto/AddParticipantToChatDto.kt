package com.plcoding.chirp.api.dto

import com.plcoding.chirp.domain.type.UserId
import jakarta.validation.constraints.Size

class AddParticipantToChatDto(
    @field:Size(min = 1)
    val userIds: List<UserId>
)