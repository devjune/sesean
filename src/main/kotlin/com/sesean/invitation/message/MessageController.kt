package com.sesean.invitation.message

import com.sesean.invitation.util.IpAddressUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/messages")
class MessageController(
    private val service: MessageService
) {

    @GetMapping
    fun getMessages(
        @RequestParam(defaultValue = "0") @Min(0) page: Int,
        @RequestParam(defaultValue = "10") @Min(1) @Max(100) size: Int
    ): Page<MessageResponse> {
        val pageable = PageRequest.of(page, size)
        return service.getMessages(pageable).map { MessageResponse.from(it) }
    }

    @PostMapping
    fun createMessage(
        @RequestBody request: CreateMessageRequest,
        httpRequest: HttpServletRequest
    ): MessageResponse {
        val ipAddress = IpAddressUtils.getClientIpAddress(httpRequest)
        val message = service.createMessage(request.author, request.content, ipAddress = ipAddress)
        return MessageResponse.from(message)
    }
}