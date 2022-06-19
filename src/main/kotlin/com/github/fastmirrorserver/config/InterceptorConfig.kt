package com.github.fastmirrorserver.config

import com.github.fastmirrorserver.interceptor.RequestLimitInterceptor
import com.github.fastmirrorserver.interceptor.RequestLogInterceptor
import com.github.fastmirrorserver.interceptor.SubmitAuthorizationInterceptor
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
    @Autowired
    lateinit var submitAuthorizationInterceptor: SubmitAuthorizationInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        super.addInterceptors(registry)
        registry.addInterceptor(requestLogInterceptor)
            .addPathPatterns("/**")
        registry.addInterceptor(requestLimitInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/api/v2/commit")
            .excludePathPatterns("/api/v2/{*path}/commit")
        registry.addInterceptor(submitAuthorizationInterceptor)
            .addPathPatterns("/api/v2/commit")
            .addPathPatterns("/api/v2/{*path}/commit")
    }
}
