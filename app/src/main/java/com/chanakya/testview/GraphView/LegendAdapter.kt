package com.chanakya.testview.GraphView

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chanakya.testview.R
import com.google.android.material.card.MaterialCardView

class LegendAdapter(private val items: List<LegendItem>) : RecyclerView.Adapter<LegendAdapter.ViewHolder>() {

    private var selectedPosition = -1
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val labelTextView: TextView = view.findViewById(R.id.labelTextView)
        val valueTextView: TextView = view.findViewById(R.id.valueTextView)
        val colorView: CardView = view.findViewById(R.id.colorView)
        val backCard: MaterialCardView = view.findViewById(R.id.cardRoot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.legend_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.labelTextView.text = item.label
        holder.valueTextView.text = item.value.toString()
        holder.colorView.setCardBackgroundColor(item.color)

        if (selectedPosition == position){
            holder.backCard.strokeColor = ContextCompat.getColor(holder.colorView.context,R.color.cardBorder)
            holder.backCard.setBackgroundColor(ContextCompat.getColor(holder.colorView.context,R.color.cardFill))
            holder.backCard.strokeWidth = 1
        }else{
            holder.backCard.strokeColor = ContextCompat.getColor(holder.colorView.context,android.R.color.transparent)
            holder.backCard.setBackgroundColor(ContextCompat.getColor(holder.colorView.context,android.R.color.transparent))
            holder.backCard.strokeWidth = 0
        }
    }

    override fun getItemCount() = items.size

    fun setSelection(position: Int){
        selectedPosition = position
        notifyDataSetChanged()
    }

    data class LegendItem(val label: String, val color: Int, val value: Float)

}
