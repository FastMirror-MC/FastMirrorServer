package com.github.fastmirrorserver.dto

import com.github.fastmirrorserver.entity.Core
import com.github.fastmirrorserver.entity.SubmitLog
import com.github.fastmirrorserver.utc
import java.time.LocalDateTime

data class Submit(
    val name: String,
    val version: String,
    val build: String,
    val updateTime: String,
    val release: Boolean,
    val sha1: String,
){
    lateinit var token: String
    lateinit var clientId: String
    fun toEntity(): Core {
        return Core{
            this@Core.name = this@Submit.name.lowercase()
            this@Core.version = this@Submit.version.lowercase()
            this@Core.build = this@Submit.build.lowercase()
            this@Core.update = utc(this@Submit.updateTime)
            this@Core.release = this@Submit.release
            this@Core.sha1 = this@Submit.sha1.lowercase()
        }
    }

    fun toSubmitLog(): SubmitLog {
        return SubmitLog {
            this@SubmitLog.client = this@Submit.clientId
            this@SubmitLog.name = this@Submit.name
            this@SubmitLog.version = this@Submit.version
            this@SubmitLog.build = this@Submit.build
            this@SubmitLog.timestamp = LocalDateTime.now()
        }
    }
}