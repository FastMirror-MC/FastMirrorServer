package com.github.fastmirrorserver.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.fastmirrorserver.controller.UploadTaskController
import com.github.fastmirrorserver.util.assemblyURI

open class Metadata(
    val name: String,
    val mc_version: String,
    val core_version: String
) {
    @get:JsonIgnore
    val key: String get() = "$name-$mc_version-$core_version"
    
    @get:JsonIgnore
    val upload_uri: String get() = assemblyURI(
        UploadTaskController.UPLOAD, mapOf(
    "name" to name,
    "mc_version" to mc_version,
    "core_version" to core_version
    ))
}
