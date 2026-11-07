package com.yourname.washield.domain

import com.yourname.washield.domain.rules.*

class ThreatDetector(
    private val enabledRules: Set<String> = DEFAULT_ENABLED
) {
    fun inspect(value: Any?): List<RuleResult> {
        val results = when (value) {
            is String -> listOf(
                TextLengthRule.inspect(value), ZeroWidthRule.inspect(value),
                RtlOverrideRule.inspect(value), NullByteRule.inspect(value),
                ControlCharRule.inspect(value), EmojiFloodRule.inspect(value),
                Base64BlobRule.inspect(value), VCardSuspiciousRule.inspect(value),
                UnicodeAnomalyRule.inspect(value), RepeatCharRule.inspect(value),
                SuspiciousLinkRule.inspect(value)
            )
            is ByteArray -> listOf(PayloadSizeRule.inspect(value))
            else -> emptyList()
        }
        return results.filter { it.triggered && it.ruleId in enabledRules }
    }

    companion object {
        val ALL_RULES = setOf(
            "text_length", "zero_width", "rtl_override", "null_byte",
            "control_chars", "emoji_flood", "base64_blob", "vcard_suspicious",
            "unicode_private_use", "repeat_char", "long_url", "payload_size"
        )
        val DEFAULT_ENABLED = setOf(
            "zero_width", "rtl_override", "null_byte", "base64_blob",
            "vcard_suspicious", "payload_size"
        )
    }
}
