package com.yourname.washield.hook

import android.content.Context
import android.os.Bundle
import android.net.Uri
import com.yourname.washield.domain.RuleResult
import com.yourname.washield.util.Constants
import de.robv.android.xposed.XposedBridge

object HookLogger {
    fun log(context: Context, packageName: String, result: RuleResult, payload: String, action: String) {
        try {
            val b = Bundle().apply {
                putLong("timestamp", System.currentTimeMillis())
                putString("ruleTriggered", result.ruleId)
                putString("severity", result.severity.name)
                putString("preview", payload.take(200))
                putString("fullText", null)
                putString("actionTaken", action)
            }
            context.contentResolver.call(Uri.parse(Constants.LOG_URI), "insertThreat", null, b)
        } catch (t: Throwable) {
            XposedBridge.log("WaShield: log IPC failed: ${t.javaClass.simpleName}")
        }
    }
}
