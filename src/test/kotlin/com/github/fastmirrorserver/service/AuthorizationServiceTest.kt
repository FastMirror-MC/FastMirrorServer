package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.component.Cache
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
internal class AuthorizationServiceTest {
    @Autowired
    lateinit var service: AuthorizationService
    @Autowired
    lateinit var cache: Cache

    @Test
    fun signature() {
        val token = service.signature("signature")
        val access = service.verify(token.access)
        val refresh = service.verify(token.refresh)

        assertEquals(access.getClaim("aud").asString(), "signature")
        assertEquals(access.getClaim("method").asString(), "access_token")

        assertEquals(refresh.getClaim("aud").asString(), "signature")
        assertEquals(refresh.getClaim("method").asString(), "refresh_token")

        assertNotNull(cache.get(access.id))
    }

    @Test
    fun refresh() {
        val oldToken = service.signature("refresh")
        val oldAccess = service.verify(oldToken.access)
        val oldRefresh = service.verify(oldToken.refresh)

        val newToken = service.refresh(oldToken.refresh)
        val newAccess = service.verify(newToken.access)
        val newRefresh = service.verify(newToken.refresh)

        assertEquals(
            oldAccess.getClaim("aud").asString(),
            newAccess.getClaim("aud").asString()
        )
        assertEquals(
            oldAccess.getClaim("method").asString(),
            newAccess.getClaim("method").asString()
        )
        assertEquals(
            oldRefresh.getClaim("aud").asString(),
            newRefresh.getClaim("aud").asString()
        )
        assertEquals(
            oldRefresh.getClaim("method").asString(),
            newRefresh.getClaim("method").asString()
        )

        assertNotEquals(
            oldAccess.id,
            newAccess.id
        )

        assertNull(cache.get(oldAccess.id))
        assertNotNull(cache.get(newAccess.id))
    }
}