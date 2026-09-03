package dev.frozenmilk.publishing

data class GitData(
    val clean: Boolean,
    val ref: String,
    val version: String,
    val snapshot: Boolean,
)