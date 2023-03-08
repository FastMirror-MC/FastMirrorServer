package com.github.fastmirrorserver.annotation

import com.github.fastmirrorserver.util.enums.Permission

annotation class Authority(val permission: Permission = Permission.NONE, val ignore_in_debug:Boolean = true)