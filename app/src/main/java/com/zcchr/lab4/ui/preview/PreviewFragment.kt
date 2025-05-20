package com.zcchr.lab4.ui.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zcchr.lab4.data.model.MarkdownElement
import com.zcchr.lab4.databinding.FragmentPreviewBinding
import com.zcchr.lab4.ui.main.MainViewModel

class PreviewFragment : Fragment() {
    private lateinit var binding: FragmentPreviewBinding
    private lateinit var adapter: MarkdownAdapter
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        mainViewModel.currentDocument.observe(viewLifecycleOwner) { document ->
            document?.let { updatePreview(it.elements) }
        }
    }

    fun updatePreview(elements: List<MarkdownElement>) {
        adapter.submitList(elements)
    }

    private fun setupRecyclerView() {
        adapter = MarkdownAdapter()
        binding.previewRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PreviewFragment.adapter
        }
    }
}
