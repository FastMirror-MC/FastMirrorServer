package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.util.UTC
import com.github.fastmirrorserver.util.toHtml
import com.github.fastmirrorserver.util.uuid
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.PrintWriter
import java.time.LocalDateTime

@Service
class ErrorReportService {
    @Value("\${server.error-report.path}")
    private lateinit var report_root_path: String
    init {
        File(report_root_path).mkdirs()
    }
    
    private fun File.readAsHtml(): String {
        val lines = readLines()
        return lines.drop(1).toHtml("Error Report", lines.first())
    }
    
    fun set(err: Throwable): String {
        val id = uuid()
        val file = File("$report_root_path/$id")
        file.createNewFile()
        if(file.exists())
            file.bufferedWriter().use { 
                it.write(LocalDateTime.now().UTC)
                it.newLine()
                it.write("${err::class.java.canonicalName}: ${err.message}")
                it.newLine()
                PrintWriter(it).also {writer ->
                    err.printStackTrace(writer)
                }.flush()
            }
        return id
    }
    
    fun get(id: String): String {
        val file = File("$report_root_path/$id")
        return if(file.exists()) 
            file.readAsHtml()
        else 
            arrayOf("error report not found.").toHtml("Error Report", LocalDateTime.now().UTC)
    }
}