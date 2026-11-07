package com.yourname.washield.data.repo

import com.yourname.washield.data.db.ThreatLogDao
import com.yourname.washield.data.db.ThreatLogEntity

class ThreatLogRepository(private val dao: ThreatLogDao) {
    fun all() = dao.getAll()
    suspend fun add(item: ThreatLogEntity) = dao.insert(item)
    suspend fun clear() = dao.deleteAll()
}
