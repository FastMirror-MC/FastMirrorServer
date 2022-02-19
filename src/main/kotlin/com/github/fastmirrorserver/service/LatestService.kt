package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.cores
import com.github.fastmirrorserver.dto.Detail
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.desc
import org.ktorm.entity.first
import org.ktorm.entity.sortedBy
import org.ktorm.support.postgresql.ilike
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LatestService {
    @Autowired
    private lateinit var database: Database

    fun query(name: String, version: String)
     = Detail.ResponseUnit(database.cores.sortedBy { it.update.desc() }.first { (it.name ilike name) and (it.version ilike version) })
}
