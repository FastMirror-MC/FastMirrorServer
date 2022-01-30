package com.github.fastmirrorserver.enums

import java.util.*

private val maps = TreeMap<String, CoreType>()

enum class CoreType {
    Spigot,
    PaperSpigot,
    Purpur,
    Arclight,
    Sponge,
    BungeeCore,
    Velocity,
    Waterfall,
    NukkitX,
    PocketMine,
    ;
    private val id: String = name.lowercase()

    init { maps[id] = this }

    companion object {
        fun get(name: String): CoreType {
            return maps[name.lowercase()]!!
        }
    }

    override fun toString(): String { return this.id }
}