package com.github.fastmirrorserver.controller

import org.springframework.core.annotation.Order
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder


@ControllerAdvice
@Order(10000)
class GlobalControllerAdvice {
    @InitBinder
    fun setAllowedFields(dataBinder: WebDataBinder) {
        val abd = arrayOf("class.*", "Class.*", "*.class.*", "*.Class.*")
        dataBinder.setDisallowedFields(*abd)
    }
}
