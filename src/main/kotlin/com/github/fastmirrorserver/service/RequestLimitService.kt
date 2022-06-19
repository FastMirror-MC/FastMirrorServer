package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.component.Cache
import com.github.fastmirrorserver.entity.RequestLimit
import com.github.fastmirrorserver.uuid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class RequestLimitService {
    @Qualifier("memoryCacheImpl")
    @Autowired
    private lateinit var cache: Cache

    fun get(address: String, token: String?): RequestLimit
     = (token ?: uuid()).let {
        cache.get(address, RequestLimit::class.java)
            ?: cache.get(it, RequestLimit::class.java)?.let { requestLimit ->
                cache.add(requestLimit.address, requestLimit, requestLimit.expire)
                requestLimit
            }
            ?: let { _ ->
                val unit = RequestLimit(
                    address = address,
                    token = it
                )
                cache.add(unit.address, unit, unit.expire)
                cache.add(unit.token, unit, unit.expire)
                unit
            }
    }
}
