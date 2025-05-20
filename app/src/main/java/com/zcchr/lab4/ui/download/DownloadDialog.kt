package com.zcchr.lab4.ui.download

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.zcchr.lab4.databinding.DialogDownloadBinding
import com.zcchr.lab4.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadDialog : DialogFragment() {
    private lateinit var binding: DialogDownloadBinding
    private val viewModel: DownloadViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDownloadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.downloadButton.setOnClickListener {
            viewModel.downloadFile(binding.urlInput.text.toString())
        }
    }

    private fun observeViewModel() {
        viewModel.downloadStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is DownloadStatus.Success -> {
                    (activity as? MainActivity)?.loadDocument(status.document.title)
                    dismiss()
                }

                is DownloadStatus.Error -> {
                    binding.errorText.visibility = View.VISIBLE
                    binding.errorText.text = status.message
                    binding.progressBar.visibility = View.GONE
                }

                DownloadStatus.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }
}
