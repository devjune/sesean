package com.sesean.invitation.message

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MessageIntegrationTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) : BehaviorSpec() {

    override fun extensions() = listOf(SpringExtension)

    init {
        given("방명록 API") {

            `when`("메시지를 생성하면") {
                val request = CreateMessageRequest("테스트 작성자", "테스트 내용")

                val result = mockMvc.post("/api/messages") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }

                then("메시지 정보와 IP가 저장된다") {
                    result.andExpect {
                        status { isOk() }
                        jsonPath("$.author") { value("테스트 작성자") }
                        jsonPath("$.content") { value("테스트 내용") }
                    }
                }
            }

            `when`("메시지 목록을 조회하면") {
                val result = mockMvc.get("/api/messages")

                then("메시지 목록을 반환한다") {
                    result.andExpect {
                        status { isOk() }
                        jsonPath("$.content") { isArray() }
                    }
                }
            }
        }
    }
}