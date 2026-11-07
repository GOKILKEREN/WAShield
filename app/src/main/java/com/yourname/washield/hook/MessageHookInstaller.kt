package com.yourname.washield.hook

import android.content.Context
import com.yourname.washield.domain.Severity
import com.yourname.washield.domain.ThreatDetector
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Method

class MessageHookInstaller(
    private val context: Context,
    private val packageName: String
) {
    private val detector = ThreatDetector()
    private val installed = HashSet<String>()

    fun inspectAndHook(clazz: Class<*>) {
        val className = clazz.name
        if (className.startsWith("java.") || className.startsWith("android.") ||
            className.startsWith("kotlin.") || className.startsWith("com.yourname.washield")) return
        val methods = runCatching { clazz.declaredMethods }.getOrDefault(emptyArray())
        for (method in methods) {
            if (!looksRelevant(method)) continue
            val key = method.toGenericString()
            if (!installed.add(key)) continue
            runCatching { hook(method) }.onFailure { XposedBridge.log("WaShield hook skipped: $key") }
        }
    }

    private fun looksRelevant(method: Method): Boolean {
        val name = method.name.lowercase()
        val named = listOf("parse", "decode", "read", "handlemessage", "processmessage", "onmessagereceived")
            .any { name.contains(it) }
        val typed = method.parameterTypes.any {
            it == ByteArray::class.java || it == String::class.java ||
                it.name.contains("protobuf", true)
        }
        return named && typed
    }

    private fun hook(method: Method) {
        XposedBridge.hookMethod(method, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                for (arg in param.args) {
                    val payload = when (arg) {
                        is String -> arg
                        is ByteArray -> null
                        else -> continue
                    }
                    val hits = if (arg is ByteArray) detector.inspect(arg) else detector.inspect(payload)
                    val dangerous = hits.filter { it.severity == Severity.CRITICAL || it.severity == Severity.HIGH }
                    if (dangerous.isEmpty()) continue
                    val preview = payload ?: "[binary payload: ${(arg as ByteArray).size} bytes]"
                    dangerous.forEach { HookLogger.log(context, packageName, it, preview, "BLOCKED") }
                    param.result = safeDefault(method.returnType)
                    return
                }
            }
        })
    }

    private fun safeDefault(type: Class<*>): Any? = when (type) {
        java.lang.Void.TYPE -> null
        java.lang.Boolean.TYPE -> false
        java.lang.Byte.TYPE -> 0.toByte()
        java.lang.Short.TYPE -> 0.toShort()
        java.lang.Integer.TYPE -> 0
        java.lang.Long.TYPE -> 0L
        java.lang.Float.TYPE -> 0f
        java.lang.Double.TYPE -> 0.0
        java.lang.Character.TYPE -> '\u0000'
        else -> null
    }
}
