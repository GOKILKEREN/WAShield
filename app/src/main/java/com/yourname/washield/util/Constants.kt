package com.yourname.washield.util

object Constants {
    const val AUTHORITY = "com.yourname.washield.logs"
    const val LOG_URI = "content://$AUTHORITY/logs"
    val DEFAULT_TARGETS = setOf(
        "com.whatsapp", "com.whatsapp.w4b", "com.gbwhatsapp",
        "com.fmwhatsapp", "com.yowhatsapp", "com.obwhatsapp"
    )
    const val ACTION_LOG = "com.yourname.washield.LOG_THREAT"
}
