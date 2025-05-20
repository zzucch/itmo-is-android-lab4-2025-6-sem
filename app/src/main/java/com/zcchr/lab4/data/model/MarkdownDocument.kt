package com.zcchr.lab4.data.model

data class MarkdownDocument(
    val title: String,
    val content: String,
    val elements: List<MarkdownElement> = emptyList(),
    val lastModified: Long = System.currentTimeMillis()
)
