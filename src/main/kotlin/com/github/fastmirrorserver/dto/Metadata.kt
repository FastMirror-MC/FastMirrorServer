package com.github.fastmirrorserver.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.fastmirrorserver.controller.UploadTaskController
import com.github.fastmirrorserver.util.assemblyURL
import javax.servlet.http.HttpServletRequest

open class Metadata(
    val name: String,
    val mc_version: String,
    val core_version: String
) {
    @get:JsonIgnore
    val key: String get() = "$name-$mc_version-$core_version"
    
    fun uploadUrl(request: HttpServletRequest): String
    = request.assemblyURL(UploadTaskController.UPLOAD, mapOf(
        "name" to name,
        "mc_version" to mc_version,
        "core_version" to core_version
    ))
}
