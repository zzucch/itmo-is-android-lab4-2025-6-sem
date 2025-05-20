package com.zcchr.lab4.data.repository

import com.zcchr.lab4.data.model.MarkdownDocument
import com.zcchr.lab4.parser.MarkdownParser
import javax.inject.Inject
import kotlin.collections.mutableListOf
import kotlin.text.substringAfterLast

class DocumentRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    private val documents = mutableListOf<MarkdownDocument>()

    suspend fun loadFromUrl(url: String): MarkdownDocument {
        val content = remoteDataSource.downloadMarkdownFile(url)
        return MarkdownDocument(
            title = url.substringAfterLast('/'),
            content = content,
        )
    }

    fun saveDocument(document: MarkdownDocument) {
        documents.removeAll { it.title == document.title }
        documents.add(
            document.copy(
                lastModified = System.currentTimeMillis()
            )
        )
    }

    fun parseAndSaveDocument(document: MarkdownDocument, parser: MarkdownParser) {
        val parsedElements = parser.parse(document.content)
        saveDocument(
            document.copy(
                elements = parsedElements
            )
        )
    }

    fun getDocument(title: String): MarkdownDocument? {
        return documents.firstOrNull { it.title == title }
    }
}
