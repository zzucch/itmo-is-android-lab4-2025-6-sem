package com.zcchr.lab4.parser

import com.zcchr.lab4.data.model.MarkdownElement
import com.zcchr.lab4.parser.elements.HeaderParser
import com.zcchr.lab4.parser.elements.ImageParser
import com.zcchr.lab4.parser.elements.ListParser
import com.zcchr.lab4.parser.elements.TableParser
import com.zcchr.lab4.parser.elements.TextWithStylesParser

class MarkdownParser {
    private val elementParsers = listOf(
        HeaderParser(),
        ListParser(),
        TableParser(),
        ImageParser(),
        TextWithStylesParser(),
    )

    fun parse(content: String): List<MarkdownElement> {
        val lines = content.lines()
        val elements = mutableListOf<MarkdownElement>()

        var i = 0
        while (i < lines.size) {
            val line = lines[i]

            if (line.isBlank()) {
                i++
                continue
            }

            var parsed = false
            for (parser in elementParsers) {
                val result = parser.parse(line, lines, i)
                if (result != null) {
                    elements.add(result.element)
                    i += result.linesConsumed
                    parsed = true

                    break
                }
            }

            if (!parsed) {
                elements.add(MarkdownElement.Text(line))
                i++
            }
        }

        return elements
    }
}

data class ParseResult(val element: MarkdownElement, val linesConsumed: Int = 1)
