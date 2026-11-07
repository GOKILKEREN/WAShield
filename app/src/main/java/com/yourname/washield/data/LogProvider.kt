package com.yourname.washield.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.yourname.washield.data.db.AppDatabase
import com.yourname.washield.data.db.ThreatLogEntity
import com.yourname.washield.util.Constants
import kotlinx.coroutines.runBlocking

class LogProvider : ContentProvider() {
    private val matcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(Constants.AUTHORITY, "logs", 1)
    }

    override fun onCreate() = true

    override fun call(method: String, arg: String?, extras: android.os.Bundle?): android.os.Bundle {
        val result = android.os.Bundle()
        if (method != "insertThreat" || extras == null) return result
        val caller = callingPackage.orEmpty()
        if (caller !in Constants.DEFAULT_TARGETS) return result
        val item = ThreatLogEntity(
            timestamp = extras.getLong("timestamp"),
            packageName = caller,
            senderName = extras.getString("senderName"),
            senderNumber = extras.getString("senderNumber"),
            ruleTriggered = extras.getString("ruleTriggered").orEmpty(),
            severity = extras.getString("severity").orEmpty(),
            preview = extras.getString("preview").orEmpty().take(200),
            fullText = extras.getString("fullText"),
            actionTaken = extras.getString("actionTaken").orEmpty()
        )
        runBlocking { AppDatabase.get(requireNotNull(context)).threatLogDao().insert(item) }
        result.putBoolean("saved", true)
        return result
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? = null
    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
