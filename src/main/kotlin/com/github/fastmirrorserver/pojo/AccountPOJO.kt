package com.github.fastmirrorserver.pojo

import com.github.fastmirrorserver.util.enums.Permission

data class AccountPOJO(
    val username: String,
    val password: String,
    val permission: Permission
)
