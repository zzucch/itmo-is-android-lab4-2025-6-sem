package com.zcchr.lab4.ui.preview

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zcchr.lab4.R
import com.zcchr.lab4.data.model.MarkdownElement
import com.zcchr.lab4.databinding.ItemMarkdownElementBinding

class MarkdownAdapter : ListAdapter<MarkdownElement, MarkdownAdapter.ViewHolder>(DiffCallback()) {
    class ViewHolder(private val binding: ItemMarkdownElementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(element: MarkdownElement) {
            binding.textView.visibility = View.GONE
            binding.imageView.visibility = View.GONE
            binding.tableLayout.visibility = View.GONE
            binding.tableLayout.removeAllViews()

            when (element) {
                is MarkdownElement.Text -> {
                    binding.textView.visibility = View.VISIBLE
                    bindText(element)
                }

                is MarkdownElement.StyledLine -> {
                    binding.textView.visibility = View.VISIBLE
                    bindStyledLine(element)
                }

                is MarkdownElement.Header -> {
                    binding.textView.visibility = View.VISIBLE
                    bindHeader(element)
                }

                is MarkdownElement.ListItem -> {
                    binding.textView.visibility = View.VISIBLE
                    bindListItem(element)
                }

                is MarkdownElement.Table -> {
                    binding.tableLayout.visibility = View.VISIBLE
                    bindTable(element)
                }

                is MarkdownElement.Image -> {
                    binding.imageView.visibility = View.VISIBLE
                    bindImage(element)
                }
            }
        }

        private fun bindText(element: MarkdownElement.Text) {
            val spannable = SpannableStringBuilder(element.content)
            element.styles.forEach { style ->
                applyStyleSpan(spannable, 0, element.content.length, style)
            }
            binding.textView.text = spannable
            binding.textView.textSize = 16f
            binding.textView.setPadding(8, 8, 8, 8)
        }


        private fun bindStyledLine(element: MarkdownElement.StyledLine) {
            val spannable = SpannableStringBuilder()

            element.fragments.forEach { fragment ->
                val start = spannable.length
                spannable.append(fragment.content)
                val end = spannable.length

                fragment.styles.forEach { style ->
                    applyStyleSpan(spannable, start, end, style)
                }
            }

            binding.textView.text = spannable
            binding.textView.textSize = 16f
            binding.textView.setPadding(8, 8, 8, 8)
        }

        private fun applyStyleSpan(
            spannable: SpannableStringBuilder,
            start: Int,
            end: Int,
            style: MarkdownElement.TextStyle
        ) {
            when (style) {
                MarkdownElement.TextStyle.BOLD -> {
                    spannable.setSpan(
                        StyleSpan(Typeface.BOLD),
                        start,
                        end,
                        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                MarkdownElement.TextStyle.ITALIC -> {
                    spannable.setSpan(
                        StyleSpan(Typeface.ITALIC),
                        start,
                        end,
                        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                MarkdownElement.TextStyle.STRIKETHROUGH -> {
                    spannable.setSpan(
                        StrikethroughSpan(),
                        start,
                        end,
                        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }

        private fun bindHeader(element: MarkdownElement.Header) {
            binding.textView.text = element.text
            binding.textView.textSize = when (element.level) {
                1 -> 24f
                2 -> 20f
                else -> 16f
            }
            binding.textView.setTypeface(null, Typeface.BOLD)
            binding.textView.setPadding(8, 16, 8, 8)
        }

        private fun bindListItem(element: MarkdownElement.ListItem) {
            val prefix = if (element.isOrdered) "${element.number}." else "•"
            binding.textView.text = buildString {
                append(prefix)
                append(" ")
                append(element.text)
            }
            binding.textView.textSize = 16f

            val paddingStart = 30 + element.level * 90
            binding.textView.setPadding(paddingStart, 8, 8, 8)
        }

        private fun bindTable(element: MarkdownElement.Table) {
            binding.textView.visibility = View.GONE
            binding.tableLayout.visibility = View.VISIBLE
            binding.tableLayout.removeAllViews()

            val context = binding.tableLayout.context

            val headerRow = TableRow(context)
            element.headers.forEach { header ->
                val headerView = TextView(context).apply {
                    text = header
                    textSize = 16f
                    setTypeface(null, Typeface.BOLD)
                    setPadding(8, 8, 8, 8)
                    setBackgroundResource(R.drawable.cell_border)
                }
                headerRow.addView(headerView)
            }
            binding.tableLayout.addView(headerRow)

            element.rows.forEach { rowModel ->
                val tableRow = TableRow(context)
                rowModel.cells.forEach { cell ->
                    val cellView = TextView(context).apply {
                        text = cell
                        textSize = 14f
                        setPadding(8, 8, 8, 8)
                        setBackgroundResource(R.drawable.cell_border)
                    }
                    tableRow.addView(cellView)
                }
                binding.tableLayout.addView(tableRow)
            }
        }

        private fun bindImage(element: MarkdownElement.Image) {
            binding.textView.visibility = View.GONE
            binding.imageView.visibility = View.VISIBLE

            Glide.with(binding.imageView.context)
                .load(element.url)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.imageView)
            binding.imageView.contentDescription = element.altText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMarkdownElementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<MarkdownElement>() {
        override fun areItemsTheSame(oldItem: MarkdownElement, newItem: MarkdownElement): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MarkdownElement,
            newItem: MarkdownElement
        ): Boolean {
            return oldItem == newItem
        }
    }
}
