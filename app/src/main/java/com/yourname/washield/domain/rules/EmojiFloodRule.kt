package com.yourname.washield.domain.rules

import com.yourname.washield.domain.RuleResult
import com.yourname.washield.domain.Severity

object EmojiFloodRule {
    const val ID = "emoji_flood"
    fun inspect(text: String): RuleResult {
        val hit = text.length > 500 && text.count { Character.getType(it) == Character.OTHER_SYMBOL.toInt() }.toDouble() / text.length > 0.80
        return RuleResult(ID, hit, Severity.MEDIUM, "Emoji/symbol density exceeds 80%", if (hit) "Emoji/symbol density exceeds 80%" else "")
    }
}
