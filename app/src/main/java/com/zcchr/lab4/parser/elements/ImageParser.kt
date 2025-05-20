package com.zcchr.lab4.parser.elements

import com.zcchr.lab4.data.model.MarkdownElement
import com.zcchr.lab4.parser.ParseResult
import com.zcchr.lab4.parser.ElementParser

class ImageParser : ElementParser {
    override fun parse(line: String, lines: List<String>, lineNumber: Int): ParseResult? {
        val regex = """!\[(.*?)]\((.*?)\)""".toRegex()
        val match = regex.find(line) ?: return null

        val altText = match.groups[1]?.value ?: ""
        val url = match.groups[2]?.value ?: return null

        return ParseResult(MarkdownElement.Image(altText, url))
    }
}
