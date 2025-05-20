package com.zcchr.lab4.ui.editor

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.zcchr.lab4.databinding.FragmentEditorBinding
import com.zcchr.lab4.ui.main.MainViewModel

class EditorFragment : Fragment() {
    private lateinit var binding: FragmentEditorBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private var isUpdatingFromViewModel = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextWatcher()
        observeViewModel()
    }

    fun updateContent(content: String) {
        if (binding.editor.text.toString() != content) {
            isUpdatingFromViewModel = true
            binding.editor.setText(content)
            isUpdatingFromViewModel = false
        }
    }

    private fun observeViewModel() {
        mainViewModel.currentDocument.observe(viewLifecycleOwner) { doc ->
            doc?.let { updateContent(it.content) }
        }
    }

    private fun setupTextWatcher() {
        binding.editor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdatingFromViewModel && s != null) {
                    mainViewModel.saveDocument(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
