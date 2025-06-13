package com.sesean.invitation.message

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MessageService(
    private val repository: MessageRepository,
) {
    fun getMessages(pageable: Pageable): Page<Message> {
        return repository.findAllByOrderByCreatedAtDesc(pageable)
    }

    @Transactional
    fun createMessage(
        author: String,
        content: String,
    ): Message {
        val message =
            Message(
                author = author,
                content = content,
            )
        return repository.save(message)
    }
}
