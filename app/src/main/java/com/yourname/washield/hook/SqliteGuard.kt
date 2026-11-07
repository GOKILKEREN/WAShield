package com.yourname.washield.hook

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import com.yourname.washield.domain.Severity
import com.yourname.washield.domain.ThreatDetector
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

class SqliteGuard(private val context: android.content.Context, private val packageName: String) {
    private val detector = ThreatDetector()

    fun install() {
        val names = listOf("insert", "insertOrThrow", "insertWithOnConflict", "replace", "update")
        names.forEach { name ->
            runCatching {
                XposedBridge.hookAllMethods(SQLiteDatabase::class.java, name, object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val table = param.args.firstOrNull { it is String } as? String ?: return
                        if (!table.contains("message", true)) return
                        val values = param.args.firstOrNull { it is ContentValues } as? ContentValues ?: return
                        for (key in values.keySet()) {
                            val value = values.get(key)
                            val hits = when (value) {
                                is String -> detector.inspect(value)
                                is ByteArray -> detector.inspect(value)
                                else -> emptyList()
                            }
                            val hit = hits.firstOrNull { it.severity == Severity.CRITICAL || it.severity == Severity.HIGH }
                            if (hit != null) {
                                HookLogger.log(context, packageName, hit, value.toString(), "BLOCKED")
                                param.result = when (name) {
                                    "update" -> 0
                                    else -> -1L
                                }
                                return
                            }
                        }
                    }
                })
            }.onFailure { XposedBridge.log("WaShield SQLite hook $name unavailable") }
        }
    }
}
