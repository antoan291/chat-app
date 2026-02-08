package com.plcoding.chat.infra.messaging

import com.plcoding.chat.domain.models.ChatParticipant
import com.plcoding.chat.service.ChatParticipantService
import com.plcoding.chirp.domain.events.user.UserEvent
import com.plcoding.chirp.infra.message_queue.MessageQueues
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ChatUserEventListener(
    private val chatParticipantService: ChatParticipantService
) {

    @RabbitListener(queues = [MessageQueues.CHAT_USER_EVENTS])
    fun handleUserEvent(event: UserEvent){
        when(event) {
            is UserEvent.Verified -> {
                chatParticipantService.createChatParticipant(
                    chatParticipant = ChatParticipant(
                        userId = event.userId,
                        username = event.username,
                        email = event.email,
                        profilePictureUrl = null
                    )
                )
            }

            else -> Unit
        }
    }
}