package io.github.halleypay.entity

data class ConfigModel(
    val prefix: String,
    val phoneNumber: String,
    val appKey: String,
    val token: String
)
