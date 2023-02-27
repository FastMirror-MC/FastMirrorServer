package com.github.fastmirrorserver.utils

import com.github.fastmirrorserver.dtos.*
import com.github.fastmirrorserver.entitys.*
import com.github.fastmirrorserver.exception.ApiException
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*

fun Project.toSummary() = Summary(this.name, this.tag, this.recommend)

fun Core.toArtifactSummary()
 = mapOf(
            "name" to this.name,
      "mc_version" to this.mc_version,
    "core_version" to this.core_version,
     "update_time" to this.update_time,
            "sha1" to this.sha1
 )

fun EntitySequence<Core, Cores>.toProjectMcVersionSummary(offset: Int, limit: Int)
= mapOf(
    "builds" to this.drop(offset).take(limit).map { it.toArtifactSummary() },
    "offset" to offset,
    "limit" to limit,
    "count" to this.totalRecords
)

val Database.all_cores get() = sequenceOf(Cores)
    .sortedBy({ it.name.asc() }, { it.mc_version.desc() }, { it.update_time.desc() })
val Database.available_cores get() = all_cores.filter { it.enable }

val Database.accounts get() = sequenceOf(Accounts)


fun EntitySequence<Core, Cores>.queryAllCoreVersionOfMcVersion(name: String, mc_version: String)
        = filter { it.name eq name }.filter { it.mc_version eq mc_version }
fun EntitySequence<Core, Cores>.querySpecificArtifact(name: String, mc_version: String, core_version:String)
        = queryAllCoreVersionOfMcVersion(name, mc_version).filter { it.core_version eq core_version }
fun EntitySequence<Core, Cores>.querySpecificArtifact(tuple: Metadata)
        = querySpecificArtifact(tuple.name, tuple.mc_version, tuple.core_version)


fun Database.getAllProject() = sequenceOf(Projects).map { it.toSummary() }

fun Database.getSupportedMcVersionOfProject(name: String) = from(Cores).leftJoin(Projects, on = Projects.name eq Cores.name)
    .selectDistinct(Cores.name, Cores.mc_version, Projects.url)
    .where { Cores.enable }
    .where { Cores.name eq name }
    .orderBy(Cores.name.asc(), Cores.mc_version.desc())
    .mapNotNull{
        val project = it[Cores.name] ?: return@mapNotNull null
        val homepage = it[Projects.url] ?: return@mapNotNull null
        val version = it[Cores.mc_version] ?: return@mapNotNull null
        return@mapNotNull (project to homepage) to version
    }.groupBy { it.first }
    .mapValues { entry -> entry.value.map { it.second } }
    .map { mapOf(
               "name" to it.key.first,
           "homepage" to it.key.second,
        "mc_versions" to it.value
    ) }
    .firstOrNull() ?: throw ApiException.ARTIFACT_INFO_NOT_FOUND

fun Database.getAllCoreVersionOfMcVersion(name: String, mc_version: String, offset: Int?, limit: Int?)
= available_cores.queryAllCoreVersionOfMcVersion(name, mc_version)
    .toProjectMcVersionSummary(
        offset = if(offset == null) 0 else if (offset < 0)  0 else offset,
        limit = if(limit == null)   1 else if (limit > 25) 25 else if (limit <= 0) 1 else limit
    )

fun Database.getSpecificArtifact(name: String, mc_version: String, core_version:String)
= try {
    available_cores.querySpecificArtifact(name, mc_version, core_version)
        .first()
}catch (e: NoSuchElementException) {
    throw ApiException.ARTIFACT_INFO_NOT_FOUND
}