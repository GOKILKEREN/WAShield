package com.yourname.washield.domain.rules

import com.yourname.washield.domain.RuleResult
import com.yourname.washield.domain.Severity

object TextLengthRule {
    const val ID = "text_length"
    fun inspect(text: String): RuleResult {
        val hit = text.length > 5000
        return RuleResult(ID, hit, if (text.length > 65000) Severity.CRITICAL else if (text.length > 30000) Severity.HIGH else Severity.MEDIUM, "Text length ${text.length}", if (hit) "Text length ${text.length}" else "")
    }
}
