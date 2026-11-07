package com.yourname.washield.domain.rules

import com.yourname.washield.domain.RuleResult
import com.yourname.washield.domain.Severity

object RepeatCharRule {
    const val ID = "repeat_char"
    fun inspect(text: String): RuleResult {
        val hit = Regex("(.)\\1{500,}", RegexOption.DOT_MATCHES_ALL).containsMatchIn(text)
        return RuleResult(ID, hit, Severity.MEDIUM, "Repeated character run detected", if (hit) "Repeated character run detected" else "")
    }
}
