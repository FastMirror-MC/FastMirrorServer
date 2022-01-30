package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.cores
import com.github.fastmirrorserver.dto.Download
import com.github.fastmirrorserver.dto.FileToken
import com.github.fastmirrorserver.entity.Cores
import org.ktorm.entity.find
import org.springframework.stereotype.Service
import java.io.OutputStream
import java.time.LocalDateTime
import java.util.*

@Service
class DownloadService : QueryService<Download.Param, Download.Response>() {
    private final val cache = TreeMap<String, FileToken>()

    private val uuid get() = UUID.randomUUID().toString().replace("-", "").uppercase()

    override fun query(param: Download.Param): Download.Response {
        return database.cores.find { param.query(Cores) }?.let {
            val signature = uuid
            val token = FileToken(it.name, it.version, it.build, it.hash, LocalDateTime.now().plusMinutes(10))
            cache[signature] = token
            Download.Response(token.artifact, token.hash, "/download/jar?token=$signature")
        } ?: throw NullPointerException()
    }

    fun download(token: String, stream: OutputStream) {
        try{ cache[token]!!.readFromLocal(stream) }
        finally { cache.remove(token) }
    }
}
