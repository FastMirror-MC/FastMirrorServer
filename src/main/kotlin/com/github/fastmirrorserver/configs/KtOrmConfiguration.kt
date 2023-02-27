package com.github.fastmirrorserver.configs

import org.ktorm.database.Database
import org.ktorm.jackson.KtormModule
import org.ktorm.support.postgresql.PostgreSqlDialect
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KtOrmConfiguration {
    @Value("\${spring.datasource.url}")
    private lateinit var url: String
    @Value("\${spring.datasource.username}")
    private lateinit var user: String
    @Value("\${spring.datasource.password}")
    private lateinit var password: String
    @Value("\${spring.datasource.driver-class-name}")
    private lateinit var driver: String
    
    @Bean
    fun database(): Database = Database.connect(url, driver, user, password, dialect = PostgreSqlDialect())
    
    @Bean
    fun ktOrmModule(): KtormModule = KtormModule()
}