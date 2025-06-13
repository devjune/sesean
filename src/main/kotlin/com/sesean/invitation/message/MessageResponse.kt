package com.sesean.invitation.message

import java.time.LocalDateTime

data class MessageResponse(
    val id: Long,
    val author: String,
    val content: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(message: Message): MessageResponse {
            return MessageResponse(
                id = message.id,
                author = message.author,
                content = message.content,
                createdAt = message.createdAt,
            )
        }
    }
}

data class CreateMessageRequest(
    val author: String,
    val content: String
)