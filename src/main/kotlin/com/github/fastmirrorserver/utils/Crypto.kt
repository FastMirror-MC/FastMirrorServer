package com.github.fastmirrorserver.utils

import java.security.MessageDigest
import java.security.SecureRandom

private val digests = mutableMapOf<String, MessageDigest>(
    "SHA1" to MessageDigest.getInstance("SHA-1"),
    "SHA256" to MessageDigest.getInstance("SHA-256")
)
private val sequence = "abcdefghijklmnopqrstuvwxy0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZz"
private val secureRandom = SecureRandom()

fun ByteArray.signature(digest: String = "SHA1") = digests[digest]!!.digest(this).hex()
fun String.signature(digest: String) = this.toByteArray().signature(digest)

fun secureRandomString(len: Int): String {
    val str = CharArray(len)
    for(i in 0 until len)
        str[i] = sequence[secureRandom.nextInt(sequence.length)]
    return String(str)
}
