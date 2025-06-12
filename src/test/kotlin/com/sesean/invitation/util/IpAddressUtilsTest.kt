package com.sesean.invitation.util

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.http.HttpServletRequest

class IpAddressUtilsTest : BehaviorSpec({
    
    given("HttpServletRequest에서 IP 주소 추출할 때") {
        
        `when`("X-Forwarded-For 헤더가 있으면") {
            val request = mockk<HttpServletRequest>()
            every { request.getHeader("X-Forwarded-For") } returns "192.168.1.100"
            every { request.getHeader("X-Real-IP") } returns null
            every { request.getHeader("Proxy-Client-IP") } returns null
            every { request.getHeader("WL-Proxy-Client-IP") } returns null
            
            val ip = IpAddressUtils.getClientIpAddress(request)
            
            then("해당 IP를 반환한다") {
                ip shouldBe "192.168.1.100"
            }
        }
        
        `when`("X-Forwarded-For에 여러 IP가 있으면") {
            val request = mockk<HttpServletRequest>()
            every { request.getHeader("X-Forwarded-For") } returns "203.0.113.195, 70.41.3.18, 150.172.238.178"
            every { request.getHeader("X-Real-IP") } returns null
            every { request.getHeader("Proxy-Client-IP") } returns null
            every { request.getHeader("WL-Proxy-Client-IP") } returns null
            
            val ip = IpAddressUtils.getClientIpAddress(request)
            
            then("첫 번째 IP를 반환한다") {
                ip shouldBe "203.0.113.195"
            }
        }
        
        `when`("모든 헤더가 없으면") {
            val request = mockk<HttpServletRequest>()
            every { request.getHeader(any()) } returns null
            every { request.remoteAddr } returns "127.0.0.1"
            
            val ip = IpAddressUtils.getClientIpAddress(request)
            
            then("remoteAddr을 반환한다") {
                ip shouldBe "127.0.0.1"
            }
        }
        
        `when`("IPv6 주소가 있으면") {
            val request = mockk<HttpServletRequest>()
            every { request.getHeader("X-Forwarded-For") } returns "2001:db8::1"
            every { request.getHeader("X-Real-IP") } returns null
            every { request.getHeader("Proxy-Client-IP") } returns null
            every { request.getHeader("WL-Proxy-Client-IP") } returns null
            
            val ip = IpAddressUtils.getClientIpAddress(request)
            
            then("IPv6 주소를 반환한다") {
                ip shouldBe "2001:db8::1"
            }
        }
    }
    
    given("IP 주소 유효성 검증할 때") {
        
        `when`("유효한 IPv4 주소이면") {
            val isValid = IpAddressUtils.isValidIpAddress("192.168.1.1")
            
            then("true를 반환한다") {
                isValid shouldBe true
            }
        }
        
        `when`("유효한 IPv6 주소이면") {
            val isValid = IpAddressUtils.isValidIpAddress("2001:db8::1")
            
            then("true를 반환한다") {
                isValid shouldBe true
            }
        }
        
        `when`("유효하지 않은 IP 주소이면") {
            val isValid = IpAddressUtils.isValidIpAddress("invalid-ip")
            
            then("false를 반환한다") {
                isValid shouldBe false
            }
        }
    }
})