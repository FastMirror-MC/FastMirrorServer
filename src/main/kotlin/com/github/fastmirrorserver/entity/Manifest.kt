package com.github.fastmirrorserver.entity

import com.github.fastmirrorserver.controller.QueryController
import com.github.fastmirrorserver.util.assemblyURL
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

interface Manifest : Entity<Manifest> {
    companion object: Entity.Factory<Manifest>()

    val name: String
    val mc_version: String
    val core_version: String
    var update_time: LocalDateTime
    val sha1: String
    val filename: String
    val path: String
    var enable: Boolean
}

fun Manifest.toResponse(request: HttpServletRequest) = mapOf<String, Any>(
    "name" to this.name,
    "mc_version" to this.mc_version,
    "core_version" to this.core_version,
    "update_time" to this.update_time,
    "sha1" to this.sha1,
    "filename" to this.filename,
    "download_url" to request.assemblyURL(QueryController.DOWNLOAD, mapOf(
        "name" to this.name,
        "mc_version" to this.mc_version,
        "core_version" to this.core_version,
    ))
)

object Manifests : Table<Manifest>("t_manifest") {
    val name = varchar("name").primaryKey().bindTo { it.name }
    val mc_version = varchar("mc_version").primaryKey().bindTo { it.mc_version }
    val core_version = varchar("core_version").primaryKey().bindTo { it.core_version }
    val filename = varchar("filename").bindTo { it.filename } 
    val enable = boolean("enable").bindTo { it.enable }
    val update_time = datetime("update_time").bindTo { it.update_time }
    val sha1 = varchar("sha1").bindTo { it.sha1 }
    val path = varchar("path").bindTo { it.path }
    const val cores_root_path: String = "./core"
}
