package com.zcchr.lab4.parser.elements

import com.zcchr.lab4.data.model.MarkdownElement
import com.zcchr.lab4.parser.ParseResult
import com.zcchr.lab4.parser.ElementParser

class HeaderParser : ElementParser {
    override fun parse(line: String, lines: List<String>, lineNumber: Int): ParseResult? {
        val trimmed = line.trim()
        if (!trimmed.startsWith("#")) return null

        val level = trimmed.takeWhile { it == '#' }.length
        if (level > 6) return null

        val text = trimmed.substring(level).trim()
        return ParseResult(MarkdownElement.Header(level, text))
    }
}
