package com.yourname.washield.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "threat_logs")
data class ThreatLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val packageName: String,
    val senderName: String?,
    val senderNumber: String?,
    val ruleTriggered: String,
    val severity: String,
    val preview: String,
    val fullText: String?,
    val actionTaken: String
)
