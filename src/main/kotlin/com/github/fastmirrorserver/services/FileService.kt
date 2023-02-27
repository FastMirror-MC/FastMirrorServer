package com.github.fastmirrorserver.services

import com.github.fastmirrorserver.exception.ApiException
import com.github.fastmirrorserver.utils.getSpecificArtifact
import org.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import javax.servlet.http.HttpServletRequest

@Service
class FileService {
    @Autowired
    private lateinit var database: Database
    
    fun send(name: String, core_version: String, mc_version: String): ResponseEntity<InputStreamResource> {
        val info = database.getSpecificArtifact(name, mc_version, core_version)
        val file = File(info.path)

        if(!file.exists()) throw ApiException.RESOURCE_NOT_FOUND

        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=${info.filename}")
            .header("Cache-Control", "no-cache, no-store, must-revalidate")
            .header("Pragma", "no-cache")
            .header("Expires", "0")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(file.length())
            .body(InputStreamResource(FileInputStream(file)))
    }
    
    fun receive(name: String, core_version: String, mc_version: String, request: HttpServletRequest) {
        
    }
}