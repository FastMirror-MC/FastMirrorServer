package com.github.fastmirrorserver.config

import com.github.fastmirrorserver.interceptor.RequestContextInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@Configuration
class InterceptorConfig : WebMvcConfigurationSupport() {
    @Autowired
    lateinit var requestContextInterceptor: RequestContextInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        super.addInterceptors(registry)
        registry.addInterceptor(requestContextInterceptor).addPathPatterns("/**")
    }
}
