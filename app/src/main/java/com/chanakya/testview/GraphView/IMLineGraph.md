
# IMLineGraph

## Overview
IMLineGraph is a custom view extending `MaterialCardView` designed to encapsulate the functionality of displaying line charts using the MPAndroidChart library. It simplifies integrating line charts into your application by handling common configuration and setup required for displaying data graphically.

## Features
- Encapsulates MPAndroidChart line chart setup and configuration.
- Simplifies data management with easy-to-use methods for setting and updating chart data.
- Customizable X-axis labels and chart appearance.
- Integrated marker view for displaying data point values on selection.
- Supports gradient fills under lines to visually enhance charts.

## Usage
To use IMLineGraph in your application, follow these steps:

1. **Include IMLineGraph in Your Layout XML**
   ```xml
   <package.IMLineGraph
       android:id="@+id/imLineGraph"
       android:layout_width="match_parent"
       android:layout_height="wrap_content" />
   ```

2. **Configure and Set Data in Your Activity or Fragment**
   Kotlin example to set data and customize the chart:
   ```kotlin
   val imLineGraph: IMLineGraph = findViewById(R.id.imLineGraph)
   val entries = listOf(Entry(1f, 2f), Entry(2f, 3f), ...)
         imLineGraph.setXAxisLabels(
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

   imLineGraph.invalidateChart()
   imLineGraph.setDataToLineChart(entries)
   ```

3. **Customize as Needed**
   Utilize provided methods such as `setXAxisLabels` for further customization.

## Customization
- X-axis labels can be customized through `setXAxisLabels`.
- Chart appearance such as colors and gradients can be adjusted in the `setDataToLineChart` method.

For more details on customization and usage, refer to the provided method documentation within the IMLineGraph class.
