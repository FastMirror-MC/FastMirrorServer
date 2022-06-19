package com.github.fastmirrorserver

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.fastmirrorserver.entity.Cores
import com.github.fastmirrorserver.entity.Projects
import com.github.fastmirrorserver.entity.SubmitLogs
import com.github.fastmirrorserver.entity.Users
import com.github.fastmirrorserver.exception.Forbidden
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.servlet.http.HttpServletRequest

val Database.cores get() = this.sequenceOf(Cores)
val Database.submits get() = this.sequenceOf(SubmitLogs)
val Database.homepages get() = this.sequenceOf(Projects)
val Database.users get() = this.sequenceOf(Users)

private val UTC = ZoneId.of("UTC")
private val sha1_digest = MessageDigest.getInstance("SHA-1")

//private val mapper = ObjectMapper().registerModule(
//    KotlinModule.Builder()
//        .withReflectionCacheSize(512)
//        .configure(KotlinFeature.NullToEmptyCollection, false)
//        .configure(KotlinFeature.NullToEmptyMap, false)
//        .configure(KotlinFeature.NullIsSameAsDefault, false)
//        .configure(KotlinFeature.SingletonSupport, false)
//        .configure(KotlinFeature.StrictNullChecks, false)
//        .build()
//)
private val mapper = ObjectMapper().registerKotlinModule()

private fun getFileExt(name: String) = when (name.lowercase()) {
    "pocketmine" -> "phar"
    else -> "jar"
}
private fun toHex(byteArray: ByteArray): String {
    val result = with(StringBuilder()) {
        byteArray.forEach {
            val hex = it.toInt() and (0xFF)
            val hexStr = Integer.toHexString(hex)
            if (hexStr.length == 1) {
                this.append("0").append(hexStr)
            } else {
                this.append(hexStr)
            }
        }
        this.toString()
    }
    return result
}

private val zoneId: ZoneId = ZoneId.systemDefault()
fun utc(dateTime: LocalDateTime) = dateTime.atZone(zoneId).format(DateTimeFormatter.ISO_INSTANT)!!
fun utc(string: String): LocalDateTime = ZonedDateTime.parse(string).withZoneSameInstant(UTC).toLocalDateTime()
fun uuid() = UUID.randomUUID().toString().replace("-", "").uppercase()
fun getFilename(name: String, version: String, coreVersion: String) =
    "$name-$version-$coreVersion.${getFileExt(name)}"
fun getPath(name: String, version: String, coreVersion: String) =
    "$name/$version/${getFilename(name, version, coreVersion)}"
fun timestamp() = System.currentTimeMillis() / 1000
fun sha1(str: String): String  = toHex(sha1_digest.digest(str.toByteArray()))
fun getAuthorization(request: HttpServletRequest)
        = (request.getHeader("Authorization")
    ?: throw Forbidden(
        errcode = ErrorCodes.Auth.illegal_field,
        message = "Cannot get `Authorization` field in header"
    ))
    .let {
        val (method, body) = it.split(' ', limit = 2)
        if(body.isEmpty() || method.isEmpty())
            throw Forbidden(
                errcode = ErrorCodes.Auth.illegal_format,
                message = "Authorization field is illegal."
            )
        if(method != "Basic" && method != "Bearer")
            throw Forbidden(
                errcode = ErrorCodes.Auth.unsupported_method_or_protocol,
                message = "Authorization field is illegal."
            )
        Pair(method, body)
    }

object Json {
    fun <T> serialization(value: T): String = mapper.writeValueAsString(value)
    fun <T> deserialization(src: String, type: Class<T>): T = mapper.readValue(src, type)
}