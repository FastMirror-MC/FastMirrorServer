package com.github.fastmirrorserver.config

import com.github.fastmirrorserver.interceptor.AuthorizationInterceptor
import com.github.fastmirrorserver.interceptor.ResponseResultInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfiguration : WebMvcConfigurer {
    @Autowired
    private lateinit var authorization_interceptor: AuthorizationInterceptor
    @Autowired
    private lateinit var response_result_interceptor: ResponseResultInterceptor
    
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authorization_interceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/static/**")
            .excludePathPatterns("/error")
        registry.addInterceptor(response_result_interceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/static/**")
            .excludePathPatterns("/error")
    }
}