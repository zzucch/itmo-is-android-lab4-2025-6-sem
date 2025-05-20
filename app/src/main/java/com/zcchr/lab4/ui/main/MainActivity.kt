package com.zcchr.lab4.ui.main

import android.os.Bundle
import com.zcchr.lab4.R
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.zcchr.lab4.databinding.ActivityMainBinding
import com.zcchr.lab4.ui.download.DownloadDialog
import com.zcchr.lab4.ui.editor.EditorFragment
import com.zcchr.lab4.ui.preview.PreviewFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        observeViewModel()
        showEditorFragment()
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_editor -> showEditorFragment()
                R.id.nav_preview -> showPreviewFragment()
                R.id.nav_download -> showDownloadDialog()
            }
            true
        }
    }

    private fun showEditorFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, EditorFragment(), "editor")
            .commit()
    }

    private fun showPreviewFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PreviewFragment(), "preview")
            .commit()
    }

    private fun showDownloadDialog() {
        DownloadDialog().show(supportFragmentManager, "DownloadDialog")
    }

    private fun observeViewModel() {
        viewModel.currentDocument.observe(this) { document ->
            document?.let {
                (supportFragmentManager.findFragmentByTag("editor") as? EditorFragment)?.updateContent(
                    it.content
                )
                (supportFragmentManager.findFragmentByTag("preview") as? PreviewFragment)?.updatePreview(
                    it.elements
                )
            }
        }
    }

    fun loadDocument(title: String) {
        viewModel.loadDocument(title)
    }
}
