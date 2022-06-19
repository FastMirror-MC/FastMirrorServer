package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.getAuthorization
import com.github.fastmirrorserver.service.AuthorizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v2/authorize")
class AuthorizationController {
    @Autowired
    private lateinit var service: AuthorizationService

    @GetMapping("login")
    fun login(request: HttpServletRequest) = service.login(getAuthorization(request).second)

    @GetMapping("refresh")
    fun refresh(request: HttpServletRequest) = service.refresh(getAuthorization(request).second)

    @GetMapping("logout")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun logout(request: HttpServletRequest) = service.logout(getAuthorization(request).second)
}