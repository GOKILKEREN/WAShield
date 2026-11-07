package com.yourname.washield.domain.rules

import com.yourname.washield.domain.RuleResult
import com.yourname.washield.domain.Severity

object NullByteRule {
    const val ID = "null_byte"
    fun inspect(text: String): RuleResult {
        val hit = text.contains('\u0000')
        return RuleResult(ID, hit, Severity.CRITICAL, "NUL character detected", if (hit) "NUL character detected" else "")
    }
}
