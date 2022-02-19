package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.cores
import com.github.fastmirrorserver.dto.Detail
import com.github.fastmirrorserver.entity.Cores
import com.github.fastmirrorserver.entity.Homepage
import com.github.fastmirrorserver.entity.Homepages
import com.github.fastmirrorserver.projects
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.find
import org.ktorm.support.postgresql.ilike
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DetailsService {
    @Autowired
    lateinit var database: Database
    @Autowired
    lateinit var history: HistoryService
    @Autowired
    lateinit var download: DownloadService
    @Autowired
    lateinit var latest: LatestService

    fun summary(): List<Homepage> {
        return database.projects.query.map { Homepages.createEntity(it) }
    }

    fun versions(name: String): List<String> {
        return database.from(Cores).selectDistinct(Cores.version)
            .where { Cores.name ilike name }
            .map { it[Cores.version]!! }
    }

    fun coreVersion(name: String, version: String, offset: Int, limit: Int)
        = history.query(name, version, if(offset < 0) 0 else offset, if(limit > 25) 25 else if(limit <= 0) 1 else limit)

    fun artifact(name: String, version: String, coreVersion: String)
     = when(coreVersion) {
         "latest" -> latest.query(name, version)
         else -> Detail.ResponseUnit(database.cores.find { (it.name ilike name) and (it.version ilike version) and (it.coreVersion ilike coreVersion) }!!)
     }

    fun download(name: String, version: String, coreVersion: String)
     = when(coreVersion) {
         "latest" -> download.query(name, version, latest.query(name, version).coreVersion)
         else -> download.query(name, version, coreVersion)
    }
}
