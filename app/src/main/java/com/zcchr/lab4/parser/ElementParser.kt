package com.zcchr.lab4.parser

interface ElementParser {
    fun parse(line: String, lines: List<String>, lineNumber: Int): ParseResult?
}
