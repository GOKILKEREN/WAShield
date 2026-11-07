package com.yourname.washield

import com.yourname.washield.hook.BootstrapHook
import com.yourname.washield.util.Constants
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import de.robv.android.xposed.XposedBridge

class WaShieldModule : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName !in Constants.DEFAULT_TARGETS) return
        XposedBridge.log("WaShield: target process loaded: ${lpparam.packageName}")
        BootstrapHook.install(lpparam.packageName, lpparam.classLoader)
    }
}
