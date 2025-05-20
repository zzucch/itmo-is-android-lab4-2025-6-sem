package com.zcchr.lab4.data.model

sealed class MarkdownElement {
    data class StyledLine(val fragments: List<Text>) : MarkdownElement()

    data class Text(val content: String, val styles: List<TextStyle> = emptyList()) :
        MarkdownElement()

    data class Header(val level: Int, val text: String) : MarkdownElement()

    data class ListItem(
        val text: String,
        val level: Int,
        val isOrdered: Boolean,
        val number: Int = 0
    ) : MarkdownElement()

    data class Table(val headers: List<String>, val rows: List<TableRow>) : MarkdownElement() {
        data class TableRow(val cells: List<String>)
    }

    data class Image(val altText: String, val url: String) : MarkdownElement()

    enum class TextStyle {
        BOLD, ITALIC, STRIKETHROUGH
    }
}
