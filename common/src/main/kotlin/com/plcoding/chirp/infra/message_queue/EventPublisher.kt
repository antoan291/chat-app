package com.plcoding.chirp.infra.message_queue

import com.plcoding.chirp.domain.events.user.UserEvent

class EventPublisher {
    fun publish(event: UserEvent.Created) {}
}