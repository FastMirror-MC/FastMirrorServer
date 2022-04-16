package com.github.fastmirrorserver.config

import com.github.fastmirrorserver.interceptor.RequestLimitInterceptor
import com.github.fastmirrorserver.interceptor.RequestLogInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@Configuration
class InterceptorConfig : WebMvcConfigurationSupport() {
    @Autowired
    lateinit var requestLimitInterceptor: RequestLimitInterceptor
    @Autowired
    lateinit var requestLogInterceptor: RequestLogInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        super.addInterceptors(registry)
        registry.addInterceptor(requestLogInterceptor)
            .addPathPatterns("/**")
        registry.addInterceptor(requestLimitInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/submit/**")
    }
}
