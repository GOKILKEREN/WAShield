package com.yourname.washield.domain.rules

import com.yourname.washield.domain.RuleResult
import com.yourname.washield.domain.Severity

object VCardSuspiciousRule {
    const val ID = "vcard_suspicious"
    fun inspect(text: String): RuleResult {
        val hit = text.contains("BEGIN:VCARD", true) && (Regex("(?im)^PHOTO[^\\n]*:[^\\n]{51200,}").containsMatchIn(text) || text.lines().count { it.contains(':') } > 100)
        return RuleResult(ID, hit, Severity.HIGH, "Unusually large or complex vCard", if (hit) "Unusually large or complex vCard" else "")
    }
}
