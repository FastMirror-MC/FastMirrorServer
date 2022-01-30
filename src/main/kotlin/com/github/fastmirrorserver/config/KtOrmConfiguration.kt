package com.github.fastmirrorserver.config

import org.ktorm.database.Database
import org.ktorm.jackson.KtormModule
import org.ktorm.support.postgresql.PostgreSqlDialect
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KtOrmConfiguration {
    @Value("\${spring.datasource.url}")
    lateinit var url: String
    @Value("\${spring.datasource.username}")
    lateinit var user: String
    @Value("\${spring.datasource.password}")
    lateinit var password: String
    @Value("\${spring.datasource.driver-class-name}")
    lateinit var driver: String
    @Bean
    fun database(): Database {
        return Database.connect(
            url = url,
            user = user,
            password = password,
            driver = driver,
            dialect = PostgreSqlDialect()
        )
    }

    @Bean
    fun ktOrmModule(): KtormModule {
        return KtormModule()
    }
}