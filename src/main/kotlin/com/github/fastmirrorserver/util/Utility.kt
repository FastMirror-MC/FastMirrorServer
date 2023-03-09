package com.github.fastmirrorserver.util

import com.github.fastmirrorserver.dto.Metadata
import com.github.fastmirrorserver.entity.Manifest
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

fun String.urlTemplate(tuple: Metadata)
        = this.template(mapOf(
    "name" to tuple.name,
    "mc_version" to tuple.mc_version,
    "core_version" to tuple.core_version
))

private fun Iterator<String>.toHtml(title: String, subtitle: String): String {
    val builder = StringBuilder()
    builder.append("<html lang=\"en\">").append('\n')
        .append("<head>").append('\n')
        .append("<meta charset=\"UTF-8\">").append('\n')
        .append("<title>$title</title>").append('\n')
        .append("</head>").append('\n')
        .append("<body>").append('\n')
        .append("<h1>$title</h1>").append('\n')
        .append("<h2>$subtitle</h2>").append('\n')
    while(hasNext()) builder.append("<p>${next()}</p>").append('\n')
    builder.append("</body>").append('\n')
    return builder.toString()
}
fun Array<String>.toHtml(title: String, subtitle: String) = iterator().toHtml(title, subtitle)
fun Iterable<String>.toHtml(title: String, subtitle: String) = iterator().toHtml(title, subtitle)