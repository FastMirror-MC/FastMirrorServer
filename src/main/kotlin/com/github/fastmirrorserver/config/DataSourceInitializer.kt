package com.github.fastmirrorserver.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import javax.sql.DataSource

@Configuration
class DataSourceInitializer {
    @Value("classpath:table.sql")
    private lateinit var _script_create_table: Resource
    
    @Bean
    fun initializer(source: DataSource) = with(DataSourceInitializer()) {
        this.setDataSource(source)
        this.setDatabasePopulator(databasePopulator())
        return@with this
    }
    
    private fun databasePopulator() = with(ResourceDatabasePopulator()) {
        this.addScript(_script_create_table)
        return@with this
    }
}