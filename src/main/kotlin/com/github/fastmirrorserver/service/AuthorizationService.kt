package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.component.Cache
import com.github.fastmirrorserver.dto.Traceback
import com.github.fastmirrorserver.entity.Account
import com.github.fastmirrorserver.entity.Accounts
import com.github.fastmirrorserver.exception.ApiException
import com.github.fastmirrorserver.pojo.AccountPOJO
import com.github.fastmirrorserver.util.*
import com.github.fastmirrorserver.util.enums.Permission
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.firstOrNull
import org.ktorm.support.postgresql.bulkInsertOrUpdate
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class AuthorizationService {
    private val logger = LoggerFactory.getLogger(AuthorizationService::class.java)
    @Autowired
    private lateinit var database: Database
    @Autowired
    private lateinit var cache: Cache
    private val regex = Regex("""^(?<username>[A-Za-z0-9_-]{4,32}):(?<password>[^:\\ /]{8,32})$""")
    private fun getAccount(authorization: String): Account {
        val match = regex.find(authorization.b64decode().trim())
            ?: throw ApiException.AUTH_INVALID_FORMAT
        val username = match.groups["username"]?.value
            ?: throw ApiException.AUTH_INVALID_FORMAT
        val password = match.groups["password"]?.value
            ?: throw ApiException.AUTH_INVALID_FORMAT
        
        return database.accounts.firstOrNull { it.name eq username }?.also {
            if(!it.verify(password)) throw ApiException.ACCOUNT_PASSWORD_INVALID
            it.last_login = LocalDateTime.now()
            it.flushChanges()
        } ?: throw ApiException.ACCOUNT_USERNAME_INVALID
    }

    /**
     * 验证请求是否合法
     * 同时检查账号密码(如果有)和session
     */
    fun verification(request: HttpServletRequest, response: HttpServletResponse): Traceback {
        trySetBasicURI(request) { 
            if((scheme == "http" && serverPort == 80) ||(scheme == "https" && serverPort == 443))
                "$scheme://$serverName"
            else
                "$scheme://$serverName:$serverPort"
        }
        try { request.getAttribute("SESSION_ENTITY")?.let { return it as Traceback } } catch (_: Exception) { }
        val ip = request.remoteAddr
        val account = request.authorization?.let { getAccount(authorization = it) }
        val cookie = request.cookies?.first { it.name == Traceback.COOKIE_NAME }
        val token = cookie?.value
            ?: account?.let { cache.get(it.name, String::class.java) }
            ?: cache.get(ip, String::class.java)
        
        val (username, permission) = account?.let { it.name to it.permission } ?: ("guest" to Permission.NONE)
        
        val traceback = token?.let {
            cache.get(it, Traceback::class.java)?.let { session -> if(session.user != username) null else session }
        } ?: Traceback(username, ip, permission)
        
        request.setAttribute("SESSION_ENTITY", traceback)
        if(cookie == null) response.addCookie(traceback.toCookie())
        return traceback
    }

    fun registerOrUpdate(pojo: AccountPOJO) {
        if(!regex.matches("${pojo.username}:${pojo.password}"))
            throw ApiException.ACCOUNT_USERNAME_OR_PASSWORD_INVALID
        val method = "SHA256"
        val salt = secureRandomString(16)
        val signature = "${pojo.password}:$salt".signature(method)
        
        val authorization_string = "\$$method:$signature:$salt"
        
        database.bulkInsertOrUpdate(Accounts) {
            item {
                set(it.name, pojo.username)
                set(it.authorization_string, authorization_string)
                set(it.permission, pojo.permission)
            }
            onConflict {
                set(it.authorization_string, authorization_string)
                set(it.permission, pojo.permission)
            }
        }
    }
}