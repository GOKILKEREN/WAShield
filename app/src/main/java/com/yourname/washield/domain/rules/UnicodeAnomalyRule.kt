package com.yourname.washield.domain.rules

import com.yourname.washield.domain.RuleResult
import com.yourname.washield.domain.Severity

object UnicodeAnomalyRule {
    const val ID = "unicode_private_use"
    fun inspect(text: String): RuleResult {
        val hit = text.count { it.code in 0xE000..0xF8FF } > 10
        return RuleResult(ID, hit, Severity.MEDIUM, "Private Use Area characters detected", if (hit) "Private Use Area characters detected" else "")
    }
}
