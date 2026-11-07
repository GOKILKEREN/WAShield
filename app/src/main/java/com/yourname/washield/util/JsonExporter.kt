package com.yourname.washield.util

import android.content.Context
import com.yourname.washield.data.db.ThreatLogEntity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

object JsonExporter {
    fun write(context: Context, logs: List<ThreatLogEntity>): File {
        val array = JSONArray()
        logs.forEach { item ->
            array.put(JSONObject().apply {
                put("id", item.id); put("timestamp", item.timestamp)
                put("packageName", item.packageName); put("senderName", item.senderName)
                put("senderNumber", item.senderNumber); put("ruleTriggered", item.ruleTriggered)
                put("severity", item.severity); put("preview", item.preview)
                put("fullText", item.fullText); put("actionTaken", item.actionTaken)
            })
        }
        return File(context.cacheDir, "washield-export.json").apply { writeText(array.toString(2)) }
    }
}
