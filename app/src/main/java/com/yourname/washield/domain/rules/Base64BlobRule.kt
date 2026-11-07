package com.yourname.washield.domain.rules

import com.yourname.washield.domain.RuleResult
import com.yourname.washield.domain.Severity

object Base64BlobRule {
    const val ID = "base64_blob"
    fun inspect(text: String): RuleResult {
        val hit = Regex("[A-Za-z0-9+/=]{2001,}").containsMatchIn(text)
        return RuleResult(ID, hit, Severity.HIGH, "Large base64-like sequence detected", if (hit) "Large base64-like sequence detected" else "")
    }
}
