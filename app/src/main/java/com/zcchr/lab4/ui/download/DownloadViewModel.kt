package com.zcchr.lab4.ui.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zcchr.lab4.data.model.MarkdownDocument
import com.zcchr.lab4.data.repository.DocumentRepository
import com.zcchr.lab4.parser.MarkdownParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val repository: DocumentRepository,
    private val parser: MarkdownParser
) : ViewModel() {
    private val _downloadStatus = MutableLiveData<DownloadStatus>()
    val downloadStatus: LiveData<DownloadStatus> = _downloadStatus

    fun downloadFile(url: String) {
        viewModelScope.launch {
            _downloadStatus.value = DownloadStatus.Loading
            try {
                val document = repository.loadFromUrl(url)
                repository.parseAndSaveDocument(document, parser)
                _downloadStatus.value = DownloadStatus.Success(document)
            } catch (e: Exception) {
                _downloadStatus.value = DownloadStatus.Error(e.message ?: "Download failed")
            }
        }
    }
}

sealed class DownloadStatus {
    object Loading : DownloadStatus()
    data class Success(val document: MarkdownDocument) : DownloadStatus()
    data class Error(val message: String) : DownloadStatus()
}
