package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.cores
import com.github.fastmirrorserver.dto.Detail
import com.github.fastmirrorserver.entity.Cores
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.ktorm.support.postgresql.ilike
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HistoryService {
    @Autowired
    protected lateinit var database: Database

    fun query(name: String, version: String, offset: Int, limit: Int)
     = Detail(
        offset = if(offset < 0) 0 else offset,
        limit = if(limit > 25) 25 else if(limit <= 0) 1 else limit,
        builds =  database.cores.filter { (Cores.name ilike name) and (Cores.version ilike version) }
            .sortedBy { Cores.update.desc() }
            .drop(offset).take(limit)
            .map { Detail.ResponseUnit(it) },
        count = database.cores.count { (it.name ilike name) and (it.version ilike version) }
    )
}
