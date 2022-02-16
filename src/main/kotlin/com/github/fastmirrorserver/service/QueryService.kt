package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.entity.Cores
import org.ktorm.database.Database
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
abstract class QueryService<in P, out R> {
    @Autowired
    protected lateinit var database: Database
    protected val ref = Cores.aliased("_ref_${this::class.java.name}")
    protected val log: Logger = LoggerFactory.getLogger(this::class.java)

    abstract fun query(param: P): R
}