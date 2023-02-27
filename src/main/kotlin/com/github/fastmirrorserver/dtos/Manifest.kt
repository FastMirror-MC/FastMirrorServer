package com.github.fastmirrorserver.dtos

import com.github.fastmirrorserver.entitys.Cores
import java.time.LocalDateTime

open class Manifest(
    name: String,
    mc_version: String,
    core_version: String,
    val update_time: LocalDateTime,
    val sha1: String,
    val filetype: String
): Metadata(name, mc_version, core_version)
{
    val filename get() = "$name-$mc_version-$core_version.$filetype"
    val filepath get() = "${Cores.cores_root_path}/$name/$mc_version/$filename"
}