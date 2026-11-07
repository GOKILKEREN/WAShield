package com.yourname.washield.domain.rules

import com.yourname.washield.domain.RuleResult
import com.yourname.washield.domain.Severity

object RtlOverrideRule {
    const val ID = "rtl_override"
    fun inspect(text: String): RuleResult {
        val hit = text.count { it == '\u202E' || it == '\u202D' } > 20
        return RuleResult(ID, hit, Severity.HIGH, "Bidirectional override characters detected", if (hit) "Bidirectional override characters detected" else "")
    }
}
