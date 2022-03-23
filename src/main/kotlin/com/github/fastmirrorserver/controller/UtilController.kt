package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.exception.NotFound
import com.github.fastmirrorserver.homepages
import org.ktorm.database.Database
import org.ktorm.entity.find
import org.ktorm.support.postgresql.ilike
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2")
class UtilController {
    @Autowired
    lateinit var database: Database

    @GetMapping("/{name}/homepage")
    fun homepage(@PathVariable name: String)
     = database.homepages.find { it.id ilike name } ?: throw NotFound(
        errcode = 501,
        message = "server name `${name}` is not found."
    )
}
