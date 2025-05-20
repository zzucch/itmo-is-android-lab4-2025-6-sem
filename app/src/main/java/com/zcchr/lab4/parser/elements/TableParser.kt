package com.zcchr.lab4.parser.elements

import com.zcchr.lab4.data.model.MarkdownElement
import com.zcchr.lab4.parser.ParseResult
import com.zcchr.lab4.parser.ElementParser

class TableParser : ElementParser {
    override fun parse(line: String, lines: List<String>, lineNumber: Int): ParseResult? {
        if (!line.trim().startsWith("|")) return null

        val headerLine = line
        val separatorLine = lines.getOrNull(lineNumber + 1) ?: return null
        if (!separatorLine.trim().startsWith("|")) return null

        val headers = parseTableRow(headerLine)
        val rows = mutableListOf<MarkdownElement.Table.TableRow>()

        var i = lineNumber + 2
        while (i < lines.size) {
            val currentLine = lines[i]
            if (!currentLine.trim().startsWith("|")) break

            rows.add(MarkdownElement.Table.TableRow(parseTableRow(currentLine)))
            i++
        }

        return ParseResult(
            MarkdownElement.Table(headers, rows),
            linesConsumed = rows.size + 2
        )
    }

    private fun parseTableRow(line: String): List<String> {
        return line.split("|")
            .drop(1)
            .dropLast(1)
            .map { it.trim() }
    }
}
