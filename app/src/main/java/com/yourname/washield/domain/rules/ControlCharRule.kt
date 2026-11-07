package com.yourname.washield.domain.rules

import com.yourname.washield.domain.RuleResult
import com.yourname.washield.domain.Severity

object ControlCharRule {
    const val ID = "control_chars"
    fun inspect(text: String): RuleResult {
        val hit = text.count { it.code in 0..31 && it != '\n' && it != '\t' } > 50
        return RuleResult(ID, hit, Severity.MEDIUM, "Excessive ASCII control characters", if (hit) "Excessive ASCII control characters" else "")
    }
}
