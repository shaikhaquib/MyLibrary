package com.chanakya.testview.GraphView


import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chanakya.testview.databinding.ImPieChartBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.card.MaterialCardView

/**
 * Created by AQUIB RASHID SHAIKH on 23-02-2024.
 * A custom MaterialCardView that displays a pie chart along with a legend, represented as a RecyclerView.
 * This view is designed to encapsulate the functionality of rendering a pie chart with its corresponding legend items.
 *
 * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
 * @param attrs The attributes of the XML tag that is inflating the view.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource that supplies default values for the view. Can be 0 to not look for defaults.
 */
class IMPieChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: ImPieChartBinding
    private val xAxisValues = ArrayList<String>()

    init {
        binding = ImPieChartBinding.inflate(LayoutInflater.from(context), this, true)
    }

    /**
     * Creates and configures the pie chart based on the provided chart data.
     * The method sets up the chart data, colors, and other configurations to display the chart.
     *
     * @param chartData A list of LegendItem objects that contains the data to be displayed in the pie chart and its legend.
     */
    fun createChart(chartData: List<LegendAdapter.LegendItem>) {
        // Data values and labels
        val pieChart = binding.pieChart
        val entries = ArrayList<PieEntry>()
        val colorList = ArrayList<Int>()
        for (data in chartData){
            entries.add(PieEntry(data.value,data.label))
            colorList.add(data.color)
        }


        // Creating dataset
        val dataSet = PieDataSet(entries, "").apply {
            // colors for the entries
            setDrawValues(false)
            colors = colorList
            valueTextSize = 12f // Set the size of the value texts
        }

        // Creating pie data object
        val data = PieData(dataSet)

        // Assigning data to the pie chart and refreshing it
        pieChart.apply {
            this.data = data
            setUsePercentValues(true)
            description.isEnabled = false
            isDrawHoleEnabled = true // If you want the hole in the middle
            setHoleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            setDrawCenterText(false)
            setCenterTextSize(12f)
            setCenterTextColor(Color.BLACK)
            animateY(600)
            legend.isEnabled = false // Show legend
            setTransparentCircleColor(ContextCompat.getColor(context,android.R.color.transparent))
            setDrawEntryLabels(false)
            holeRadius = 58f
            transparentCircleRadius = 0f
        }

        // Invalidate the chart to refresh it
        val adapter = LegendAdapter(chartData)
        binding.myRecyclerView.adapter = adapter
        pieChart.invalidate()

        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                for (i in chartData.indices){
                    if (e is PieEntry) {
                        val label = e.label
                        // Do something with the label
                        if (chartData[i].label.equals(label)){
                            adapter.setSelection(i)
                        }
                    }

                }
            }

            override fun onNothingSelected() {
                // This method is called when no slice is selected or the selection is cleared.
            }
        })
    }

    /**
     * Returns the PieChart view for further customization or interaction.
     *
     * @return The PieChart instance contained within this custom view.
     */
    fun getChartView(): PieChart = binding.pieChart

    /**
     * Returns the RecyclerView used as the legend for the pie chart.
     *
     * @return The RecyclerView instance that displays the legend items.
     */
    fun getLegendView(): RecyclerView = binding.myRecyclerView

}
