package com.zcchr.lab4.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.io.readText

class RemoteDataSource {
    suspend fun downloadMarkdownFile(url: String): String {
        return try {
            withContext(Dispatchers.IO) {
                URL(url).readText()
            }
        } catch (e: Exception) {
            throw Exception("Failed to download file: ${e.message} $e")
        }
    }
}
