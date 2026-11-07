package com.yourname.washield.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ThreatLogDao {
    @Insert suspend fun insert(item: ThreatLogEntity): Long
    @Query("SELECT * FROM threat_logs ORDER BY timestamp DESC") fun getAll(): Flow<List<ThreatLogEntity>>
    @Query("SELECT * FROM threat_logs WHERE timestamp BETWEEN :from AND :to ORDER BY timestamp DESC") fun getByDateRange(from: Long, to: Long): Flow<List<ThreatLogEntity>>
    @Query("SELECT * FROM threat_logs WHERE packageName = :pkg ORDER BY timestamp DESC") fun getByPackage(pkg: String): Flow<List<ThreatLogEntity>>
    @Query("DELETE FROM threat_logs") suspend fun deleteAll()
    @Query("DELETE FROM threat_logs WHERE id = :id") suspend fun deleteById(id: Long)
    @Query("SELECT COUNT(*) FROM threat_logs WHERE timestamp >= :start AND timestamp < :end") suspend fun countToday(start: Long, end: Long): Int
    @Query("SELECT severity, COUNT(*) AS total FROM threat_logs WHERE timestamp >= :start AND timestamp < :end GROUP BY severity") suspend fun countBySeverity(start: Long, end: Long): List<SeverityCount>
}

data class SeverityCount(val severity: String, val total: Int)
