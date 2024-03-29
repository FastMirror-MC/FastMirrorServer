﻿package com.github.fastmirrorserver.util

import com.github.fastmirrorserver.dto.*
import com.github.fastmirrorserver.entity.*
import com.github.fastmirrorserver.exception.ApiException
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.ktorm.expression.ArgumentExpression
import org.ktorm.schema.BooleanSqlType
import org.ktorm.schema.ColumnDeclaring

fun Project.toSummary() = Summary(this.id, this.tag, this.recommend)

fun Manifest.toArtifactSummary()
 = mapOf(
            "name" to this.name,
      "mc_version" to this.mc_version,
    "core_version" to this.core_version,
     "update_time" to this.update_time,
            "sha1" to this.sha1
 )

fun EntitySequence<Manifest, Manifests>.toProjectMcVersionSummary(offset: Int, limit: Int)
= mapOf(
    "builds" to this.drop(offset).take(limit).map { it.toArtifactSummary() },
    "offset" to offset,
    "limit" to limit,
    "count" to this.totalRecords
)

val Database.all_cores get() = sequenceOf(Manifests)
    .sortedBy({ it.name.asc() }, { it.mc_version.desc() }, { it.update_time.desc() })
val Database.available_cores get() = all_cores.filter { it.enable }

val Database.accounts get() = sequenceOf(Accounts)


fun EntitySequence<Manifest, Manifests>.queryAllCoreVersionOfMcVersion(name: String, mc_version: String)
        = filter { it.name eq name }.filter { it.mc_version eq mc_version }
fun EntitySequence<Manifest, Manifests>.querySpecificArtifact(name: String, mc_version: String, core_version:String)
        = queryAllCoreVersionOfMcVersion(name, mc_version).filter { it.core_version eq core_version }
fun EntitySequence<Manifest, Manifests>.querySpecificArtifact(tuple: Metadata)
        = querySpecificArtifact(tuple.name, tuple.mc_version, tuple.core_version)

private fun Database.queryProjectBy(condition: () -> ColumnDeclaring<Boolean>)
= from(Manifests).leftJoin(Projects, on = Projects.id eq Manifests.name)
    .selectDistinct(Manifests.name, Manifests.mc_version, Projects.url, Projects.tag, Projects.recommend)
    .where { Manifests.enable }
    .where (condition)
    .orderBy(Manifests.name.asc(), Manifests.mc_version.desc())
    .mapNotNull{
        val project = it[Manifests.name] ?: return@mapNotNull null
        val homepage = it[Projects.url] ?: return@mapNotNull null
        val tag = it[Projects.tag] ?: return@mapNotNull null
        val recommend = it[Projects.recommend] ?: return@mapNotNull null
        val version = it[Manifests.mc_version] ?: return@mapNotNull null
        return@mapNotNull Tuple4(project, tag, homepage, recommend) to version
    }.groupBy { it.first }
    .mapValues { entry -> entry.value.map { it.second } }
    .map { mapOf(
        "name" to it.key.element1,
        "tag" to it.key.element2,
        "homepage" to it.key.element3,
        "recommend" to it.key.element4,
        "mc_versions" to it.value
    ) }

fun Database.getAllProjects() = queryProjectBy { ArgumentExpression(true, BooleanSqlType) }

fun Database.getSupportedMcVersionOfProjects(projects: ArrayList<String>) = queryProjectBy {
    if(projects.any())
        projects.map { it eq Manifests.name }.reduce { a, b -> a or b }
    else
        ArgumentExpression(false, BooleanSqlType)
}

fun Database.getSupportedMcVersionOfProject(name: String) = queryProjectBy { Manifests.name eq name }
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