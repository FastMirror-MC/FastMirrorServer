package com.github.fastmirrorserver.util

import com.github.fastmirrorserver.exception.ApiException
import javax.servlet.http.HttpServletRequest

private val regex = Regex("""^(?<method>[A-Z][a-z]+) (?<body>[A-Za-z0-9+/=]+)$""")

val HttpServletRequest.authorization get() = this
    .getHeader("Authorization")?.let { 
        val match = regex.find(it.trim())
            ?: throw ApiException.AUTH_INVALID_FORMAT
        val method = match.groups["method"]?.value 
            ?: throw ApiException.AUTH_INVALID_FORMAT
        val body = match.groups["body"]?.value
            ?: throw ApiException.AUTH_INVALID_FORMAT
        
        if(method != "Basic")
            throw ApiException.AUTH_METHOD_NOT_SUPPORTED
        return@let body
    }
val HttpServletRequest.host get() = 
    if((scheme == "http" && serverPort == 80) ||(scheme == "https" && serverPort == 443))
        "$scheme://$serverName"
    else
        "$scheme://$serverName:$serverPort"
fun HttpServletRequest.assemblyURL(path: String, map: Map<String, String>)
= "$host$path".template(map)
