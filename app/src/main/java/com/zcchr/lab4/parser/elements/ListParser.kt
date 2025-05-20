package com.zcchr.lab4.parser.elements

import com.zcchr.lab4.data.model.MarkdownElement
import com.zcchr.lab4.parser.ParseResult
import com.zcchr.lab4.parser.ElementParser

class ListParser : ElementParser {
    override fun parse(line: String, lines: List<String>, lineNumber: Int): ParseResult? {
        val trimmed = line.trimStart()

        val orderedMatch = Regex("""^(\d+)\.\s+(.*)""").find(trimmed)
        val unorderedMatch = Regex("""^([-*+])\s+(.*)""").find(trimmed)

        if (orderedMatch == null && unorderedMatch == null) return null

        val indentChars = line.takeWhile { it == ' ' || it == '\t' }
        val spacesCount = indentChars.count { it == ' ' }
        val tabsCount = indentChars.count { it == '\t' }
        val level = (spacesCount / 2) + tabsCount

        val (text, isOrdered, number) = when {
            orderedMatch != null -> {
                val num = orderedMatch.groupValues[1].toIntOrNull() ?: 0
                val content = orderedMatch.groupValues[2].trim()
                Triple(content, true, num)
            }

            unorderedMatch != null -> {
                val content = unorderedMatch.groupValues[2].trim()
                Triple(content, false, 0)
            }

            else -> return null
        }

        return ParseResult(
            MarkdownElement.ListItem(
                text = text,
                level = level,
                isOrdered = isOrdered,
                number = number
            )
        )
    }
}
