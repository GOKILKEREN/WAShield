package com.yourname.washield.hook

import android.content.Context
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import java.util.Collections
import java.util.WeakHashMap

class ClassDiscovery(
    private val context: Context,
    private val packageName: String,
    private val classLoader: ClassLoader
) {
    private val seen = Collections.newSetFromMap(WeakHashMap<Class<*>, Boolean>())
    private val installer = MessageHookInstaller(context, packageName)

    fun start() {
        try {
            XposedBridge.hookAllMethods(ClassLoader::class.java, "loadClass", object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val name = param.args.firstOrNull() as? String ?: return
                    if (!candidateName(name)) return
                    val clazz = param.result as? Class<*> ?: return
                    synchronized(seen) {
                        if (seen.add(clazz)) installer.inspectAndHook(clazz)
                    }
                }
            })
            XposedBridge.log("WaShield: class discovery active for $packageName")
        } catch (t: Throwable) {
            XposedBridge.log("WaShield: class discovery unavailable: ${t.message}")
        }
    }

    private fun candidateName(name: String): Boolean {
        val n = name.lowercase()
        return listOf("message", "parser", "proto", "fmessage", "usermessage", "conversation")
            .any { n.contains(it) }
    }
}
