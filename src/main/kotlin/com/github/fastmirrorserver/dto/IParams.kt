package com.github.fastmirrorserver.dto

import com.github.fastmirrorserver.entity.Cores
import org.ktorm.expression.ScalarExpression

interface IParams { fun query(alias: Cores): ScalarExpression<Boolean> }