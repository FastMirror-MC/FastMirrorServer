package com.github.fastmirrorserver.entity

import com.github.fastmirrorserver.enums.CoreType
import com.github.fastmirrorserver.enums.McVersion
import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime

interface SubmitLog : Entity<SubmitLog> {
    companion object: Entity.Factory<SubmitLog>()
    var client: String
    var name: CoreType
    var version: McVersion
    var build: String
    var timestamp: LocalDateTime
}

object SubmitLogs: Table<SubmitLog>("t_submit_log") {
    val id = int("id").primaryKey()
    val client = varchar("client").bindTo { it.client }
    val name = enum<CoreType>("name").bindTo { it.name }
    val version = enum<McVersion>("version").bindTo { it.version }
    val build = varchar("build").bindTo { it.build }
    val timestamp = datetime("time").bindTo { it.timestamp }
}
