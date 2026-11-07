package com.yourname.washield.domain.rules

import com.yourname.washield.domain.RuleResult
import com.yourname.washield.domain.Severity

object SuspiciousLinkRule {
    const val ID = "long_url"
    fun inspect(text: String): RuleResult {
        val hit = Regex("https?://\\S{2001,}", RegexOption.IGNORE_CASE).containsMatchIn(text)
        return RuleResult(ID, hit, Severity.MEDIUM, "Extremely long URL detected", if (hit) "Extremely long URL detected" else "")
    }
}
