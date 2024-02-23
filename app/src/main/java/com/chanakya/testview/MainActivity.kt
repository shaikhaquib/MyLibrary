package com.chanakya.testview

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chanakya.testview.GraphView.LegendAdapter
import com.chanakya.testview.databinding.ActivityMainBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
val chartData = listOf(
    LegendAdapter.LegendItem( "Item 1",getAssetColor(R.color.maroon),50f),
    LegendAdapter.LegendItem( "Item 2",getAssetColor(R.color.bronze),30f),
    LegendAdapter.LegendItem( "Longer item name 3",getAssetColor(R.color.warning),20f)
)
binding.pieChart.createChart(chartData)
    }


    private fun getAssetColor(color: Int): Int {
        return ContextCompat.getColor(applicationContext, color)
    }




}
