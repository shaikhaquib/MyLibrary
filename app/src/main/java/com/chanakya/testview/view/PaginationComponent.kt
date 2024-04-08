package com.chanakya.testview.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chanakya.testview.databinding.PaginationComponentBinding
import kotlin.reflect.KFunction1

class PaginationComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var binding: PaginationComponentBinding
    private var pageCount: Int = 0
    private var currentPage: Int = 0
    private val pageAdapter = PageAdapter(this::onPageSelected)

    init {
        // Inflate the view and obtain an instance of the binding class
        binding = PaginationComponentBinding.inflate(LayoutInflater.from(context), this, true)

        with(binding) {
            rvPageNumbers.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = pageAdapter
            }

            btnPrev.setOnClickListener {
                updateCurrentPage(currentPage - 1)
            }

            btnNext.setOnClickListener {
                updateCurrentPage(currentPage + 1)
            }
        }
    }

    fun setPageCount(count: Int) {
        this.pageCount = count
        updatePages()
    }

    private fun updatePages() {
        val pagesToShow = calculatePagesToShow()
        pageAdapter.pages = pagesToShow

        binding.btnPrev.isEnabled = currentPage > 1
        binding.btnNext.isEnabled = currentPage < pageCount
    }

    private fun calculatePagesToShow(): List<String> {
        // Implement the logic to calculate which pages to show based on currentPage and pageCount
        return listOf("1", "2", "...", "$pageCount") // Example, replace with actual logic
    }

    private fun onPageSelected(page: Int) {
        updateCurrentPage(page)
        // Trigger any listener for page change if necessary
    }

    private fun updateCurrentPage(page: Int) {
        currentPage = page
        updatePages()
    }
}

class PageAdapter(private val onPageSelected: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var pages: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PageViewHolder).bind(pages[position])
    }

    override fun getItemCount(): Int = pages.size

    inner class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(page: String) {
            (itemView as TextView).text = page
            itemView.setOnClickListener {
                if (page != "...") {
                    onPageSelected(page.toInt())
                }
            }
        }
    }
}

