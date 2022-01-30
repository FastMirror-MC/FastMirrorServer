package com.github.fastmirrorserver.dto

import com.github.fastmirrorserver.entity.Core
import com.github.fastmirrorserver.entity.SubmitLog
import com.github.fastmirrorserver.enums.CoreType
import com.github.fastmirrorserver.enums.McVersion
import com.github.fastmirrorserver.utc
import java.time.LocalDateTime

data class Submit(
    val name: String,
    val version: String,
    val build: String,
    val updateTime: String,
    val release: Boolean,
    val hash: String,
){
    lateinit var token: String
    lateinit var clientId: String
    fun toEntity(): Core {
        return Core{
            this@Core.name = CoreType.get(this@Submit.name)
            this@Core.version = McVersion.get(this@Submit.version)
            this@Core.build = this@Submit.build
            this@Core.update = utc(this@Submit.updateTime)
            this@Core.release = this@Submit.release
            this@Core.hash = this@Submit.hash
        }
    }

    fun toSubmitLog(): SubmitLog {
        return SubmitLog {
            this@SubmitLog.client = this@Submit.clientId
            this@SubmitLog.name = CoreType.get(this@Submit.name)
            this@SubmitLog.version = McVersion.get(this@Submit.version)
            this@SubmitLog.build = this@Submit.build
            this@SubmitLog.timestamp = LocalDateTime.now()
        }
    }
}