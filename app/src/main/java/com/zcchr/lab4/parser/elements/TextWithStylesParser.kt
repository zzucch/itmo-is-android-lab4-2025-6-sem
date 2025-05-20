package com.zcchr.lab4.parser.elements

import com.zcchr.lab4.data.model.MarkdownElement
import com.zcchr.lab4.parser.ParseResult
import com.zcchr.lab4.parser.ElementParser

class TextWithStylesParser : ElementParser {
    override fun parse(line: String, lines: List<String>, lineNumber: Int): ParseResult? {
        if (line.isEmpty()) return null

        val results = mutableListOf<MarkdownElement.Text>()

        val boldItalicStrikethroughPattern = Regex("""(\*\*|__)(.+?)\1|([*_])(.+?)\3|~~(.+?)~~""")

        var lastIndex = 0

        for (match in boldItalicStrikethroughPattern.findAll(line)) {
            if (match.range.first > lastIndex) {
                val plainText = line.substring(lastIndex, match.range.first)
                if (plainText.isNotEmpty()) {
                    results.add(MarkdownElement.Text(plainText))
                }
            }

            val styledText: String
            val styles = mutableListOf<MarkdownElement.TextStyle>()

            when {
                match.groups[1] != null -> {
                    styledText = match.groups[2]?.value ?: ""
                    styles.add(MarkdownElement.TextStyle.BOLD)
                }

                match.groups[3] != null -> {
                    styledText = match.groups[4]?.value ?: ""
                    styles.add(MarkdownElement.TextStyle.ITALIC)
                }

                match.groups[5] != null -> {
                    styledText = match.groups[5]?.value ?: ""
                    styles.add(MarkdownElement.TextStyle.STRIKETHROUGH)
                }

                else -> styledText = ""
            }

            if (styledText.isNotEmpty()) {
                results.add(MarkdownElement.Text(styledText, styles))
            }

            lastIndex = match.range.last + 1
        }

        if (lastIndex < line.length) {
            val plainText = line.substring(lastIndex)
            if (plainText.isNotEmpty()) {
                results.add(MarkdownElement.Text(plainText))
            }
        }

        if (results.isEmpty()) return null

        return ParseResult(MarkdownElement.StyledLine(results))
    }
}
