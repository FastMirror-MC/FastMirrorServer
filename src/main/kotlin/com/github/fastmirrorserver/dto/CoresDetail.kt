package com.github.fastmirrorserver.dto

import com.github.fastmirrorserver.entity.Core
import com.github.fastmirrorserver.utc

data class CoresDetail(
    val builds: List<ResponseUnit>,
    val offset: Int,
    val limit: Int,
    val count: Int
) {
    data class ResponseUnit(
        val name: String,
        val version: String,
        val coreVersion: String,
        val update: String
    ) {
        constructor(o: Core) : this(
            name = o.name,
            version = o.version,
            coreVersion = o.coreVersion,
            update = utc(o.update)
        )
    }
}