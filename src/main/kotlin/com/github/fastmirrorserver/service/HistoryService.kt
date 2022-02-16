package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.dto.History
import org.ktorm.database.asIterable
import org.ktorm.database.use
import org.springframework.stereotype.Service
import java.util.*

@Service
class HistoryService : QueryService<History.Param,Map<String, Map<String, List<History.Response>>>>() {
    private fun template(len: Int): String {
        val joiner = StringJoiner(", ")
        for (i in 1 .. len) joiner.add("?")
        return joiner.toString()
    }

    override fun query(param: History.Param): Map<String, Map<String, List<History.Response>>> {
        val query = database.useConnection { connection ->
            val nl = param.nameArray()
            val vl = param.versionArray()

            //TODO 寻找可替代的sql语句或尝试实现row_number()...
            connection.prepareStatement("""
select T."name", T."version", T."core_version", T.update, T.release
from (
    select *, row_number() over (partition by "name", "version" order by "update" desc) vn from t_core
) T
where (T.vn between ? and ?) and T."name" in (${template(nl.size)}) and T."version" in (${template(vl.size)})
                """).use { statement ->
                statement.setInt(1, param.offset + 1)
                statement.setInt(2, param.offset + param.limit)
                // 这玩意儿太弱智了
                var i = 2
                for (x in nl) statement.setString(++i, x)
                for (x in vl) statement.setString(++i, x)
                println(statement)
                val query = statement.executeQuery()
                query.asIterable()
                    .map {
                    History.Response(
                        name = it.getString(1),
                        version = it.getString(2),
                        coreVersion = it.getString(3),
                        update = it.getTimestamp(4).toLocalDateTime(),
                        release = it.getBoolean(5)
                    )
                }
            }
        }.groupBy { it.name }
            .mapValues { entry ->
                entry.value.groupBy { it.version }
            }
        return query
    }
}