package com.yourname.washield.domain.rules

import com.yourname.washield.domain.RuleResult
import com.yourname.washield.domain.Severity

object PayloadSizeRule {
    const val ID = "payload_size"
    fun inspect(bytes: ByteArray): RuleResult {
        val size = bytes.size
        val hit = size > 1024 * 1024
        val severity = if (size > 5 * 1024 * 1024) Severity.CRITICAL else Severity.HIGH
        return RuleResult(ID, hit, severity, "Oversized byte payload", "Payload size: $size bytes")
    }
}
