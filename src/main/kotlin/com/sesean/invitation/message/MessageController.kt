package com.sesean.invitation.message

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/messages")
class MessageController(
    private val service: MessageService,
) {
    @GetMapping
    fun getMessages(
        @RequestParam(defaultValue = "0") @Min(0) page: Int,
        @RequestParam(defaultValue = "10") @Min(1) @Max(100) size: Int,
    ): Page<MessageResponse> {
        val pageable = PageRequest.of(page, size)
        return service.getMessages(pageable).map { MessageResponse.from(it) }
    }

    @PostMapping
    fun createMessage(
        @RequestBody request: CreateMessageRequest,
    ): MessageResponse {
        val message = service.createMessage(request.author, request.content)
        return MessageResponse.from(message)
    }
}
