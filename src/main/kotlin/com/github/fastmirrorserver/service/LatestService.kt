package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.dto.Latest
import com.github.fastmirrorserver.entity.Cores
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import org.springframework.stereotype.Service

@Service
class LatestService : QueryService<Latest.Param, Map<String, Map<String, Latest.ResponseUnit>>>() {
    val ref1 = Cores.aliased("_ref_latest")

    private fun latest(it: Cores, release: Boolean): ColumnDeclaring<Boolean> {
        return it.update inList database.from(ref1).select(max(ref1.update)).where {
            if(release)
                (it.name eq ref1.name) and (it.version eq ref1.version) and ref1.release
            else
                (it.name eq ref1.name) and (it.version eq ref1.version)
        }
    }

    override fun query(param: Latest.Param): Map<String, Map<String, Latest.ResponseUnit>> {
        val lastBuild = Cores.aliased("t1")
        val lastRelease = Cores.aliased("t2")
        val query = database.from(lastBuild)
            .leftJoin(lastRelease, on = (lastBuild.name eq lastRelease.name) and (lastBuild.version eq lastRelease.version))
            .select(lastBuild.name, lastBuild.version, lastBuild.build, lastBuild.update, lastRelease.build, lastRelease.update)
            .where {
                latest(lastBuild, release = false) and param.query(lastBuild) and
                latest(lastRelease, release = true)  and param.query(lastRelease) and lastRelease.release
            }
        log.info("\n{}", query.sql)
        val value = query.map {
            Latest.ResponseUnit(it = it, build = lastBuild, release = lastRelease)
        }.groupBy { it.name }
            .mapValues { entry ->
                entry.value.groupBy { it.version }.mapValues { it.value[0] }
            }
        return value
    }
}
