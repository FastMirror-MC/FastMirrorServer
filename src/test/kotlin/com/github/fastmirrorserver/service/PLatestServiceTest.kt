package com.github.fastmirrorserver.service

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fastmirrorserver.dto.History
import com.github.fastmirrorserver.dto.Latest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.ktorm.database.Database
import org.ktorm.database.use
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.sql.PreparedStatement

@SpringBootTest
internal class PLatestServiceTest {
//    @Autowired
//    lateinit var service: LatestService
//    val mapper: ObjectMapper = ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
//    @Test
//    fun test1() {
//        println(mapper.writeValueAsString(service.query(Latest.Param("spigot", "1.18.1"))))
//    }
//
//    @Autowired
//    lateinit var historyService: HistoryService
//
//    @Test
//    fun test2() {
//        println(mapper.writeValueAsString(historyService.query(History.Param("spigot", "1.18.1,1.18", 1, 3))))
//    }
}