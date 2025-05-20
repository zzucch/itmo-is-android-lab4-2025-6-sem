package com.zcchr.lab4.ui.main

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
class MainViewModel @Inject constructor(
    private val repository: DocumentRepository,
    private val parser: MarkdownParser
) : ViewModel() {
    private val _currentDocument = MutableLiveData<MarkdownDocument?>()
    val currentDocument: LiveData<MarkdownDocument?> = _currentDocument

    fun loadDocument(title: String) {
        viewModelScope.launch {
            repository.getDocument(title)?.let { doc ->
                _currentDocument.value = doc.copy(elements = parser.parse(doc.content))
            }
        }
    }

    fun saveDocument(content: String) {
        _currentDocument.value?.let { current ->
            val elements = parser.parse(content)
            val updatedDoc = current.copy(content = content, elements = elements)
            repository.saveDocument(updatedDoc)
            _currentDocument.value = updatedDoc
        } ?: run {
            val elements = parser.parse(content)
            val newDoc = MarkdownDocument(
                title = "New Document",
                content = content,
                elements = elements
            )
            repository.saveDocument(newDoc)
            _currentDocument.value = newDoc
        }
    }
}
