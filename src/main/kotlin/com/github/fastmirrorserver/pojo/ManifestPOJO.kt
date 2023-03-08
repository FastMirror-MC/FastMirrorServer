package com.github.fastmirrorserver.pojo

import com.github.fastmirrorserver.dto.Metadata
import com.github.fastmirrorserver.entity.Manifests
import java.time.LocalDateTime

open class ManifestPOJO(
    name: String,
    mc_version: String,
    core_version: String,
    val update_time: LocalDateTime,
    val sha1: String,
    filetype: String
): Metadata(name, mc_version, core_version)
{
    val filename = "$name-$mc_version-$core_version.$filetype"
    val filepath = "${Manifests.cores_root_path}/$name/$mc_version/$filename"
}