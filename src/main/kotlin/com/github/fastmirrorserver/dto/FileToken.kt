package com.github.fastmirrorserver.dto

import com.github.fastmirrorserver.utc
import java.io.*
import java.time.LocalDateTime
import java.util.concurrent.TimeoutException

data class FileToken (
    private val path: File,
    val sha1: String,
    private val expiration: LocalDateTime?
    ) {
    constructor(
        name: String,
        version: String,
        build: String,
        sha1: String,
        expiration: LocalDateTime? = null
    ) : this(
        File("./core/$name/$version/$name-$version-$build.jar"),
        sha1,
        expiration
    )

    fun writeToLocal(stream: InputStream) {
        path.parentFile.mkdirs()
        val output = FileOutputStream(path)
        output.write(stream.readAllBytes())
        output.flush()
        output.close()
    }

    val exists: Boolean get() = path.exists()

    val artifact: String get() = path.name

    fun readFromLocal(stream: OutputStream) {
        if(expiration == null || expiration < LocalDateTime.now())
            throw TimeoutException("resources expired since ${utc(expiration ?: LocalDateTime.now())}.")
        val input = FileInputStream(path)
        stream.write(input.readAllBytes())
    }
}