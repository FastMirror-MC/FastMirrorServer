package com.github.fastmirrorserver.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.DatabasePopulator
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import javax.sql.DataSource

@Configuration
class CustomDataSourceInitializer {
    @Value("classpath:table.sql")
    lateinit var scriptCreateTable: Resource

    @Bean
    public fun initializer(source: DataSource): DataSourceInitializer {
        val dsi = DataSourceInitializer()
        dsi.setDataSource(source)
        dsi.setDatabasePopulator(databasePopulator())
        return dsi
    }

    private fun databasePopulator(): DatabasePopulator {
        val populator = ResourceDatabasePopulator()
        populator.addScript(scriptCreateTable)
        return populator
    }
}