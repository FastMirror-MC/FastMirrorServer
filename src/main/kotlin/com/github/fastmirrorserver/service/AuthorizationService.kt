package com.github.fastmirrorserver.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.github.fastmirrorserver.component.Cache
import com.github.fastmirrorserver.dto.JWTEntity
import com.github.fastmirrorserver.exception.Unauthorized
import com.github.fastmirrorserver.uuid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthorizationService {
    @Value("\${server.auth.jwt-secret-key}")
    private lateinit var secretKey: String

    @Autowired
    private lateinit var cache: Cache

    private final val TTL = 60 * 60 * 24 * 5

    private val builder = JWT.create()
    private val lazyVerifier = lazy { JWT.require(Algorithm.HMAC512(secretKey)).build() }

    private fun time(field: Int, amount: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(field, amount)
        return calendar.time
    }

    fun signature(user: String)
     = uuid().let {
        cache.add(it, it, TTL)
         JWTEntity(
             access = builder
                 .withClaim("aud", user)
                 .withClaim("jti", it)
                 .withClaim("method", "access_token")
                 .withExpiresAt(time(Calendar.HOUR, 1))
                 .sign(Algorithm.HMAC512(secretKey)),
             refresh = builder
                 .withClaim("jti", it)
                 .withClaim("method", "refresh_token")
                 .withExpiresAt(time(Calendar.DATE, 5))
                 .sign(Algorithm.HMAC512(secretKey))
         )
    }

    fun refresh(refreshToken: String): JWTEntity {
        val jwt = verify(refreshToken)
        val jti = jwt.id
        val aud = jwt.getClaim("aud").asString()
        if(cache.get(jti) == null)
            throw Unauthorized(
                errcode = 5001,
                message = "The token has expired. Please try logging in again."
            )
        cache.del(jti)
        return signature(aud)
    }

    fun verify(token: String): DecodedJWT = lazyVerifier.value.verify(token)
}