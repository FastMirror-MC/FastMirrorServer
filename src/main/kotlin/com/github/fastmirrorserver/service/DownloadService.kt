package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.cores
import com.github.fastmirrorserver.dto.Download
import com.github.fastmirrorserver.dto.FileToken
import com.github.fastmirrorserver.entity.Cores
import com.github.fastmirrorserver.utc
import com.github.fastmirrorserver.uuid
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.entity.find
import org.ktorm.support.postgresql.ilike
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.OutputStream
import java.time.LocalDateTime
import java.util.*

@Service
class DownloadService {
    @Autowired
    protected lateinit var database: Database
    private final val cache = TreeMap<String, FileToken>()

    private val uuid get() = uuid()

    fun query(name: String, version: String, coreVersion: String): Download {
        return database.cores.find {
            (Cores.name ilike name) and
            (Cores.version ilike version) and
            (Cores.coreVersion ilike coreVersion)
        }?.let {
            val signature = uuid
            val token = FileToken(it.path, it.sha1, LocalDateTime.now().plusMinutes(10))
            cache[signature] = token
            Download(
                name = it.name,
                version = it.version,
                coreVersion = it.coreVersion,
                update = utc(it.update),
                artifact = token.artifact,
                sha1 = token.sha1,
                url = "/download?token=$signature"
            )
        } ?: throw NullPointerException()
    }

    fun getFilename(token: String): String {
        return cache[token]!!.artifact
    }

    fun download(token: String, stream: OutputStream) {
        try{ cache[token]!!.readFromLocal(stream) }
        finally { cache.remove(token) }
    }
}
