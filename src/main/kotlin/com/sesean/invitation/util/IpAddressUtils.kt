package com.sesean.invitation.util

import inet.ipaddr.IPAddressString
import jakarta.servlet.http.HttpServletRequest

object IpAddressUtils {

    fun getClientIpAddress(request: HttpServletRequest): String {
        val headers = listOf(
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP"
        )

        for (header in headers) {
            val headerValue = request.getHeader(header)
            if (!headerValue.isNullOrBlank() && !headerValue.equals("unknown", ignoreCase = true)) {
                val firstIp = headerValue.split(",")[0].trim()
                val ipAddressString = IPAddressString(firstIp)
                if (ipAddressString.isValid) {
                    return firstIp
                }
            }
        }

        val remoteAddr = request.remoteAddr ?: "unknown"

        val ipAddressString = IPAddressString(remoteAddr)
        return if (ipAddressString.isValid) remoteAddr else "unknown"
    }

    fun isValidIpAddress(ip: String): Boolean {
        return IPAddressString(ip).isValid
    }
}