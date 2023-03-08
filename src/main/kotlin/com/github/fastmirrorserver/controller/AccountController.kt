package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.annotation.Authority
import com.github.fastmirrorserver.pojo.AccountPOJO
import com.github.fastmirrorserver.service.AuthorizationService
import com.github.fastmirrorserver.util.enums.Permission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController {
    companion object {
        const val REGISTER = "/api/v3/auth/register"
    }
    @Autowired
    private lateinit var authorization: AuthorizationService
    @PostMapping(REGISTER, consumes= ["application/json"])
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Authority(Permission.ROOT)
    fun register(@RequestBody pojo: AccountPOJO) = authorization.registerOrUpdate(pojo)
}
