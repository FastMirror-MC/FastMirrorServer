package com.github.fastmirrorserver.entity

import com.github.fastmirrorserver.util.enums.Permission
import com.github.fastmirrorserver.util.signature
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.enum
import org.ktorm.schema.varchar
import java.time.LocalDateTime

interface Account : Entity<Account> {
    companion object: Entity.Factory<Account>()
    var name: String
    var authorization_string: String
    var permission: Permission
    var last_login: LocalDateTime
    
    fun verify(password: String): Boolean {
        val (method, digest, salt) = authorization_string.substring(1).split(":")
        return digest == "$password:$salt".signature(method)
    }
}

object Accounts : Table<Account>("t_account") {
    val name = varchar("name").primaryKey().bindTo { it.name }
    val authorization_string = varchar("authorization_string").bindTo { it.authorization_string }
    val permission = enum<Permission>("permission").bindTo { it.permission }
    val last_login = datetime("last_login").bindTo { it.last_login }
}
