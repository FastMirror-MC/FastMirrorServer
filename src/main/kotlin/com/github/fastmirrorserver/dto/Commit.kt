package com.github.fastmirrorserver.dto

import com.github.fastmirrorserver.entity.Core
import com.github.fastmirrorserver.entity.SubmitLog
import com.github.fastmirrorserver.sha1
import com.github.fastmirrorserver.timestamp
import com.github.fastmirrorserver.utc
import java.time.LocalDateTime

open class Commit(
    name: String,
    version: String,
    coreVersion: String,
) : CoreInfo(name, version, coreVersion) {
    val digest: String = sha1("${name}-${version}-${coreVersion}")

    fun toResponse(ttl: Int) = Response(
        url = "/api/v2/${name}/${version}/${coreVersion}/commit",
        expired_at = timestamp() + ttl
    )

    class Request(
        name: String,
        version: String,
        coreVersion: String,
        val clientId: String,
        val build: Int,
        val updateTime: String,
        val release: Boolean,
        val sha1: String
    ) : Commit(name, version, coreVersion) {
        fun toEntity() = Core {
            this@Core.name = this@Request.name
            this@Core.version = this@Request.version
            this@Core.coreVersion = this@Request.coreVersion
            this@Core.build = this@Request.build
            this@Core.update = utc(this@Request.updateTime)
            this@Core.release = this@Request.release
            this@Core.sha1 = this@Request.sha1
            this@Core.path = this@Request.path
        }

        fun toCommitLog() = SubmitLog {
            this@SubmitLog.client = this@Request.clientId
            this@SubmitLog.name = this@Request.name
            this@SubmitLog.version = this@Request.version
            this@SubmitLog.coreVersion = this@Request.coreVersion
            this@SubmitLog.timestamp = LocalDateTime.now()
        }
    }
    data class Response(val url: String, val expired_at: Long)
}
