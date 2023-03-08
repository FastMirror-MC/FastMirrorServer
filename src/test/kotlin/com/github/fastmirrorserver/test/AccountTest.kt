package com.github.fastmirrorserver.test

import com.github.fastmirrorserver.controller.AccountController
import com.github.fastmirrorserver.controller.TestController
import com.github.fastmirrorserver.dto.ApiResponse
import com.github.fastmirrorserver.exception.ApiException
import com.github.fastmirrorserver.pojo.AccountPOJO
import com.github.fastmirrorserver.util.*
import com.github.fastmirrorserver.util.enums.Permission
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AccountTest {
    @Autowired
    private lateinit var mock: MockMvc
    
    companion object {
        private val username = "tester"
        private val password = secureRandomString(16)//"vn,jtJdjh5UXyVCh5y=.Cz7^"
    }
    
//    @Test
//    fun `upload bytes, expect successful`() {
//        val data = "580BA888-81FE-4D47-BFF6-2C770E2CD811".toByteArray()
//        
//        mock.put(TestController.UPLOAD_TEST) {
//            headers { 
//                set("Range", "0-15/15")
//                set("Content-Length", data.size.toString())
//            }
//            content = data
//        }.andDo { print() }
//            .andExpect { 
//                status { is2xxSuccessful() }
//                content { data.signature() }
//            }
//    }
    
    @Test
    @Order(0)
    fun `register, expect successful`() {
        println(password)
        mock.post(AccountController.REGISTER) {
            contentType = MediaType.APPLICATION_JSON
            content = AccountPOJO(username, password, Permission.TESTER).serialize()
        }
//            .andDo { print() }
            .andExpect {
                status { is2xxSuccessful() }
            }
    }
    @Test
    @Order(0)
    fun `register but invalid password, expect failed`() {
        mock.post(AccountController.REGISTER) {
            contentType = MediaType.APPLICATION_JSON
            content = AccountPOJO("username", "q:23", Permission.TESTER).serialize()
        }
            .andExpect { 
                status { isForbidden() }
                content { json(ApiException.ACCOUNT_USERNAME_OR_PASSWORD_INVALID.toResponse().serialize()) }
            }
    }
    @Test
    @Order(1)
    fun `login, expect successful`() {
        mock.get(TestController.DEV_NULL) {
            header("Authorization", "Basic ${"$username:$password".b64encode()}")
            }
//            .andDo { print() }
            .andExpect { 
                status { is2xxSuccessful() }
                content { 
                    json(ApiResponse.success().serialize())
                }
            }
    }
    
    @Test
    @Order(1)
    fun `permission, expect failed`() {
        mock.get(TestController.PERMISSION_TEST) {
            header("Authorization", "Basic ${"$username:$password".b64encode()}")
        }
//            .andDo { print() }
            .andExpect { 
                status { isUnauthorized() }
                content { 
                    json(ApiException.PERMISSION_DENIED.toResponse().serialize())
                }
            }
    }

    @Test
    @Order(2)
    fun `login but wrong password, expect failed`() {
        mock.get(TestController.DEV_NULL) {
            header("Authorization", "Basic ${"$username:wrong_password".b64encode()}")
        }
//            .andDo { print() }
            .andExpect {
                status { isForbidden() }
                content {
                    json(ApiException.ACCOUNT_PASSWORD_INVALID.toResponse().serialize())
                }
            }
    }

    @Test
    @Order(2)
    fun `login but wrong username, expect failed`() {
        mock.get(TestController.DEV_NULL) {
            header("Authorization", "Basic ${"wrong_username:wrong_password".b64encode()}")
        }
//            .andDo { print() }
            .andExpect {
                status { isForbidden() }
                content {
                    json(ApiException.ACCOUNT_USERNAME_INVALID.toResponse().serialize())
                }
            }
    }
    
    @Test
    @Order(2)
    fun `illegal format, except failed`() {
        mock.get(TestController.DEV_NULL) {
            header("Authorization", "Basic-${"$username+$password".b64encode()}")
        }
//        .andDo { print() }
            .andExpect {
                status { isBadRequest() }
                content {
                    json(ApiException.AUTH_INVALID_FORMAT.toResponse().serialize())
                }
            }
    }
    
    @Test
    @Order(2)
    fun `empty or blank Authorization, except failed`() {
        mock.get(TestController.DEV_NULL) {
            header("Authorization", " ")
        }
//            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
                content {
                    json(ApiException.AUTH_INVALID_FORMAT.toResponse().serialize())
                }
            }
    }
    
    @Test
    @Order(2)
    fun `not support Authorization method, expect failed`() {
        mock.get(TestController.DEV_NULL) {
            header("Authorization", "Bearer nullptr")
        }
//            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
                content {
                    json(ApiException.AUTH_METHOD_NOT_SUPPORTED.toResponse().serialize())
                }
            }
    }
    @Test
    @Order(2)
    fun `login but invalid format, expect failed`() {
        mock.get(TestController.DEV_NULL) {
            header("Authorization", "Basic ${"$username+$password".b64encode()}")
        }
//            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
                content {
                    json(ApiException.AUTH_INVALID_FORMAT.toResponse().serialize())
                }
            }
    }

    @Test
    @Order(3)
    fun `request limit, expect failed`() {
        mock.get(TestController.DEV_NULL) {
            header("Authorization", "Basic ${"$username:$password".b64encode()}")
        }.andExpect { status { is2xxSuccessful() } }
        mock.get(TestController.DEV_NULL) {
            header("Authorization", "Basic ${"$username:$password".b64encode()}")
        }
        mock.get(TestController.DEV_NULL) {
            header("Authorization", "Basic ${"$username:$password".b64encode()}")
        }
//            .andDo { print() }
            .andExpect {
                status { isForbidden() }
                content {
                    json(ApiException.REQUEST_LIMIT.toResponse().serialize())
                }
            }
    }
}
