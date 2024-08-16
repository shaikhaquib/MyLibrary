package com.chanakya.dialoge

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chanakya.testview.R
import com.chanakya.testview.databinding.BottomSheetSingleSelectBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


/**
 * Created by AQUIB RASHID SHAIKH on 11-03-2023.
 */



class SingleSelectBottomSheetDialogFragment(
    private val context: Context,
    private val items: ArrayList<SearchAbleList>,
    private val title: String,
    private val selectedValue: Int? = 0,
    private val searchAble:Boolean = false,
    private val cancelAble:Boolean = false,
    private val listener: ((selectedValue: SearchAbleList) -> Unit)? = null
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSingleSelectBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SingleSelectAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSingleSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheet = view.parent as View
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        isCancelable = cancelAble
        binding.title.text = title
        adapter = SingleSelectAdapter(items, selectedValue?:0) { value ->
            listener?.invoke(value)
            dismiss()
        }
        binding.ivBottomSheetClose.setOnClickListener { dismiss() }
        binding.btnSubmit.isVisible = false
        adapter.setHasStableIds(true)
        // Set up RecyclerView with adapter
        if (searchAble == true){
            binding.edtSearch.visibility = View.VISIBLE
        }else {
            binding.edtSearch.visibility = View.GONE
        }
        binding.itemList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.itemList.adapter = adapter

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter(binding.edtSearch.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.edtSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {


                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.isFitToContents = false
                val layoutParams: ViewGroup.LayoutParams = bottomSheet.layoutParams
                val windowHeight = getWindowHeight()
                if (layoutParams != null) {
                    layoutParams.height = windowHeight
                }
                bottomSheet.layoutParams = layoutParams
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                /*// Hide the soft keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)*/
            }
        }
    }


    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (getContext() as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class SingleSelectAdapter(
        private val items: ArrayList<SearchAbleList>,
        private val selectedValue: Int = 0,
        private val onItemClick: (value: SearchAbleList) -> Unit
    ) : RecyclerView.Adapter<SingleSelectAdapter.ViewHolder>() {

        private var filteredItems: ArrayList<SearchAbleList> = ArrayList()

        init {
            filteredItems = items
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_single_select, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = filteredItems[position]
            holder.text.text = item.title
            holder.itemView.isSelected = position == selectedValue
            holder.itemView.setOnClickListener { onItemClick.invoke(item) }
            if (item.icon != 0){
                holder.imgIcon.setImageResource(item.icon)
            }else{
                holder.imgIcon.visibility = View.GONE
            }
        }

        override fun getItemCount(): Int {
            return filteredItems.size
        }

        fun filter(query: String) {
            filteredItems = ArrayList()
            if (query.isNotEmpty()) {
                for (item in items) {
                    if (item.title.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))) {
                        filteredItems.add(item)
                    }
                }
            } else {
                filteredItems = items
            }
            notifyDataSetChanged()
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val text = itemView.findViewById<AppCompatTextView>(R.id.itemText)
            val imgIcon = itemView.findViewById<AppCompatImageView>(R.id.imgIcon)
        }
    }


}
