package com.yourname.washield.domain

data class RuleResult(
    val ruleId: String,
    val triggered: Boolean,
    val severity: Severity,
    val reason: String,
    val detail: String
)
