package com.github.fastmirrorserver.dto

import com.github.fastmirrorserver.entity.Core
import com.github.fastmirrorserver.entity.SubmitLog
import com.github.fastmirrorserver.getPath
import com.github.fastmirrorserver.utc
import java.time.LocalDateTime

data class Submit(
    val name: String,
    val version: String,
    val coreVersion: String,
    val build: Int,
    val updateTime: String,
    val release: Boolean,
    val sha1: String,
){
    lateinit var token: String
    lateinit var clientId: String
    fun toEntity(): Core {
        val n = this@Submit.name
        val v = this@Submit.version
        val c = this@Submit.coreVersion
        return Core{
            this@Core.name = n
            this@Core.version = v
            this@Core.coreVersion = c
            this@Core.build = this@Submit.build
            this@Core.update = utc(this@Submit.updateTime)
            this@Core.release = this@Submit.release
            this@Core.sha1 = this@Submit.sha1
            this@Core.path = "./core/${getPath(n, v, c)}"
        }
    }

    fun toSubmitLog(): SubmitLog {
        return SubmitLog {
            this@SubmitLog.client = this@Submit.clientId
            this@SubmitLog.name = this@Submit.name
            this@SubmitLog.version = this@Submit.version
            this@SubmitLog.coreVersion = this@Submit.coreVersion
            this@SubmitLog.timestamp = LocalDateTime.now()
        }
    }
}