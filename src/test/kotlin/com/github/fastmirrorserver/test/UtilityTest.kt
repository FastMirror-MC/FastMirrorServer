package com.github.fastmirrorserver.test

import com.github.fastmirrorserver.dtos.Metadata
import com.github.fastmirrorserver.utils.template
import com.github.fastmirrorserver.utils.urlTemplate
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.Assert

@SpringBootTest
class UtilityTest {
    @Test
    fun `String template by map, expect success`() {
        val ans = "/api/v3/download/{name}/{mc_version}/{core_version}".template(mapOf(
            "name" to "Arclight",
            "mc_version" to "1.19.1",
            "core_version" to "1.0.0"
        ))
        Assert.isTrue(ans == "/api/v3/download/Arclight/1.19.1/1.0.0", ans)
    }
    @Test
    fun `String template by object, expect success`() {
        val ans = "/api/v3/download/{name}/{mc_version}/{core_version}".urlTemplate(
            Metadata(
                name = "Arclight",
                mc_version = "1.19.1",
                core_version = "1.0.0"
            )
        )
        Assert.isTrue(ans == "/api/v3/download/Arclight/1.19.1/1.0.0", ans)
    }
}
