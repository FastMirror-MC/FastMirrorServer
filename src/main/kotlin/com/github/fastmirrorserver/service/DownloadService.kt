package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.component.Cache
import com.github.fastmirrorserver.cores
import com.github.fastmirrorserver.dto.CoreInfo
import com.github.fastmirrorserver.dto.Download
import com.github.fastmirrorserver.utc
import com.github.fastmirrorserver.uuid
import org.ktorm.database.Database
import org.ktorm.entity.find
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.concurrent.TimeoutException

@Service
class DownloadService {
    @Autowired
    protected lateinit var database: Database
    @Qualifier("memoryCacheImpl")
    @Autowired
    private lateinit var cache: Cache

    fun createTask(info: CoreInfo): Download
    =  database.cores.find {
        info.toQueryExpression(it)
    }?.let {
        val token = uuid()
        cache.add(token, info, 60 * 10)
        Download(
            name = it.name,
            version = it.version,
            coreVersion = it.coreVersion,
            update = utc(it.update),
            artifact = info.filename,
            sha1 = it.sha1,
            url = "/download?token=$token"
        )
    } ?: throw IllegalArgumentException()

    fun readFile(token: String): Pair<CoreInfo, ByteArray> {
        cache.get(token, CoreInfo::class.java)?.let {
            val file = File(it.path)
            if(!file.exists()) throw FileNotFoundException()
            val buffer: ByteArray
            FileInputStream(file).use { fp ->
                buffer = fp.readAllBytes()
            }
            return Pair(it, buffer)
        } ?: throw TimeoutException()
    }
}
