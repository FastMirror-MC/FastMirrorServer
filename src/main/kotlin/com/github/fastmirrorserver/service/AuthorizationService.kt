package com.github.fastmirrorserver.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.github.fastmirrorserver.ErrorCodes
import com.github.fastmirrorserver.component.Cache
import com.github.fastmirrorserver.dto.JWTEntity
import com.github.fastmirrorserver.exception.Forbidden
import com.github.fastmirrorserver.exception.Unauthorized
import com.github.fastmirrorserver.users
import com.github.fastmirrorserver.uuid
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthorizationService {
    private val logger = LoggerFactory.getLogger(AuthorizationService::class.java)
    @Value("\${server.auth.jwt-secret-key}")
    private lateinit var secretKey: String
    @Autowired
    lateinit var database: Database
    @Qualifier("memoryCacheImpl")
    @Autowired
    private lateinit var cache: Cache

    private final val ttl = 60 * 60 * 24 * 5 // 5天

    private val builder = JWT.create()
    private val lazyVerifier = lazy { JWT.require(Algorithm.HMAC512(secretKey)).build() }
    private val base64 = Base64.getDecoder()

    private fun time(field: Int, amount: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(field, amount)
        return calendar.time
    }

    fun login(token: String): JWTEntity {
        val (user, password) = try{
            String(base64.decode(token)).split(':', limit = 2)
        }
        catch(e: IllegalAccessException) {
            throw Forbidden(errcode = ErrorCodes.Auth.illegal_field, message = "Authorization field is illegal.")
        }

        if(password.isEmpty() || user.isEmpty())
            throw Forbidden(errcode = ErrorCodes.Auth.too_few_param, "Authorization field is illegal.")
        try {
            return login(user, password)
        }
        catch (e: IllegalArgumentException) {
            throw Forbidden(errcode = ErrorCodes.Auth.login, message = e.message!!)
        }
    }

    fun login(user: String, passwd: String): JWTEntity {
        val u = database.users.find { it.name eq user } ?: throw IllegalArgumentException("The specified user could not be found.")
        if(!u.verify(passwd)) throw IllegalArgumentException("Password error.")

        return (cache.get(user)?.let {
            // 如果缓存里面没有uuid对应的jwt，就新建一组并返回
            cache.get(it, JWTEntity::class.java)?.let { jwt ->
                try {
                    verify(jwt.access_token)
                    verify(jwt.refresh_token)
                    jwt
                } catch(e: Exception){
                    logger.warn("error occurred when verify local jwt.", e)
                    signature(user)
                }
            } ?: signature(user)
        } ?: signature(user))
    }

    private fun signature(user: String)
     = uuid().let {
        val expiresAt = time(Calendar.HOUR, 1)
        val jwt = JWTEntity(
            access_token = builder
                .withClaim("aud", user)
                .withClaim("jti", it)
                .withClaim("method", "access_token")
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC512(secretKey)),
            refresh_token = builder
                .withClaim("aud", user)
                .withClaim("jti", it)
                .withClaim("method", "refresh_token")
                .withExpiresAt(time(Calendar.DATE, 5))
                .sign(Algorithm.HMAC512(secretKey)),
            expires_at = expiresAt.time
        )
        cache.get(user)?.let { uuid -> cache.del(uuid); cache.del(user) }
        cache.add(user, it, ttl)
        cache.add(it, jwt, ttl)
        jwt
    }

    fun refresh(refreshToken: String): JWTEntity {
        val (jti, aud) = verify(refreshToken)
        if(cache.get(jti) == null)
            throw Unauthorized(
                errcode = ErrorCodes.Auth.refresh_token_expired,
                message = "The token has expired. Please try logging in again."
            )
        cache.del(jti)
        return signature(aud)
    }

    fun logout(token: String) {
        val (jti, aud) = verify(token)
        cache.get(aud)?.let { cache.del(it); cache.del(aud) }
        cache.del(jti)
    }

    fun verify(token: String): Pair<String, String> {
        val jwt = lazyVerifier.value.verify(token)
        return Pair(jwt.id, jwt.getClaim("aud").asString())
    }
}