package com.github.fastmirrorserver.utils

import com.github.fastmirrorserver.dtos.Metadata
import com.github.fastmirrorserver.entitys.Core
import java.lang.StringBuilder
import java.math.BigInteger
import java.util.*
import java.util.regex.Pattern

fun ByteArray.hex() = BigInteger(1, this).toString(16).uppercase()

fun uuid() = UUID.randomUUID().toString().uppercase()

fun String.b64encode(): String = String(Base64.getEncoder().encode(this.toByteArray()))
fun String.b64decode(): String = String(Base64.getDecoder().decode(this))

fun String.template(map: Map<String, String>): String {
    val pattern = Pattern.compile("""\{([A-Za-z0-9_-]+)}""")
    val matcher = pattern.matcher(this)
    val sb = StringBuilder()
    while(matcher.find()) {
        val k = matcher.group()
        (map[k.substring(1, k.length - 1)] ?: k)
            .let { matcher.appendReplacement(sb, it) } 
    }
    matcher.appendTail(sb)
    return sb.toString()
}

fun String.urlTemplate(core: Core) 
= this.template(mapOf(
    "name" to core.name,
    "mc_version" to core.mc_version,
    "core_version" to core.core_version
))

fun String.urlTemplate(tuple: Metadata)
        = this.template(mapOf(
    "name" to tuple.name,
    "mc_version" to tuple.mc_version,
    "core_version" to tuple.core_version
))