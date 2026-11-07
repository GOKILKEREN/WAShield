package com.yourname.washield.hook

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.util.Collections
import java.util.WeakHashMap

object BootstrapHook {
    private val started = Collections.newSetFromMap(WeakHashMap<ClassLoader, Boolean>())

    fun install(packageName: String, classLoader: ClassLoader) {
        runCatching {
            XposedHelpers.findAndHookMethod(
                ContextWrapper::class.java, "attachBaseContext", Context::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val app = param.thisObject as Application
                        startOnce(packageName, app.classLoader ?: classLoader, app)
                    }
                }
            )
        }.onFailure { XposedBridge.log("WaShield: Application.attach hook failed: ${it.message}") }
    }

    private fun startOnce(pkg: String, loader: ClassLoader, app: Application) {
        synchronized(started) {
            if (!started.add(loader)) return
        }
        runCatching {
            SqliteGuard(app, pkg).install()
            ClassDiscovery(app, pkg, loader).start()
            XposedBridge.log("WaShield: bootstrap completed for $pkg")
        }.onFailure { XposedBridge.log("WaShield bootstrap error: ${it.message}") }
    }
}
