package com.chanakya.testview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chanakya.testview.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var barChart: BarChart

    // on below line we are creating
    // a variable for bar data
    lateinit var barData: BarData

    // on below line we are creating a
    // variable for bar data set
    lateinit var barDataSet: BarDataSet

    // on below line we are creating array list for bar data
    lateinit var barEntriesList: ArrayList<BarEntry>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getBarChartData()
        val barColors = ArrayList<Int>()
        barColors.add(ContextCompat.getColor(this, R.color.bronze))
        barColors.add(ContextCompat.getColor(this, R.color.bronze))
        barColors.add(ContextCompat.getColor(this, R.color.shimmer_background_color))
        barColors.add(ContextCompat.getColor(this, R.color.greyish_brown))
        barColors.add(ContextCompat.getColor(this, R.color.greyish_brown))
        binding.barChart.setXAxisLabels(
            arrayListOf(
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"
            )
        )
        binding.barChart.initializedBarChart(barEntriesList,barColors) // Refresh the chart to show changes
    }


    // Custom renderer class for rounded bars

    private fun getBarChartData() {
        barEntriesList = ArrayList()

        // on below line we are adding data
        // to our bar entries list
        barEntriesList.add(BarEntry(1f, 1f))
        barEntriesList.add(BarEntry(2f, 2f))
        barEntriesList.add(BarEntry(3f, 3f))
        barEntriesList.add(BarEntry(4f, 4f))
        barEntriesList.add(BarEntry(5f, 5f))

    }


    /*
        private fun drawLineChart() {
            binding.lineChart.setXAxisLabels(
                arrayListOf(
                    "Jan",
                    "Feb",
                    "Mar",
                    "Apr",
                    "May",
                    "Jun",
                    "Jul",
                    "Aug",
                    "Sep",
                    "Oct",
                    "Nov",
                    "Dec"
                )
            )
            binding.lineChart.invalidateChart()
            setData()
        }
    */


    /*  private fun setData() {
          val entries = mutableListOf<Entry>()
          // Dummy data
          entries.add(Entry(1f, 0f))
          entries.add(Entry(2f, 20f))
          entries.add(Entry(3f, 10f))
          entries.add(Entry(4f, 30f))
          entries.add(Entry(5f, 20f))
          entries.add(Entry(6f, 35f))
          entries.add(Entry(7f, 30f))
          entries.add(Entry(8f, 45f))
          entries.add(Entry(9f, 50f))
          binding.lineChart.setDataToLineChart(entries)
      }
    */

    /* pieChart

     *//*val chartData = listOf(
           LegendAdapter.LegendItem( "Item 1",getAssetColor(R.color.maroon),50f),
           LegendAdapter.LegendItem( "Item 2",getAssetColor(R.color.bronze),30f),
           LegendAdapter.LegendItem( "Longer item name 3",getAssetColor(R.color.warning),20f)
       )
       binding.pieChart.createChart(chartData)*/

    private fun getAssetColor(color: Int): Int {
        return ContextCompat.getColor(applicationContext, color)
    }






}
