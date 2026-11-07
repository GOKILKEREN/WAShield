package com.yourname.washield.domain.rules

import com.yourname.washield.domain.RuleResult
import com.yourname.washield.domain.Severity

object ZeroWidthRule {
    const val ID = "zero_width"
    fun inspect(text: String): RuleResult {
        val hit = text.count { it == '\u200B' || it == '\u200C' || it == '\u200D' || it == '\uFEFF' } > 50
        return RuleResult(ID, hit, Severity.HIGH, "Zero-width characters detected", if (hit) "Zero-width characters detected" else "")
    }
}
