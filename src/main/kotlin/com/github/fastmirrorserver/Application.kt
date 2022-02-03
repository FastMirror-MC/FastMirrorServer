package com.github.fastmirrorserver

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import springfox.documentation.oas.annotations.EnableOpenApi
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
open class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}