# IMPieChart Component

IMPieChart is a custom Android view that combines a PieChart with a RecyclerView to display the chart's legend. It simplifies the process of displaying a pie chart alongside its legend within a single component.

## Features

- Customizable pie chart and legend display.
- Easy integration into any Android layout.
- Supports dynamic data updates.

## Installation

To use IMPieChart in your project, include the following dependencies in your `build.gradle` file:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
}
```
# Usage
1. Add IMPieChart to your layout XML:

```XML
<com.icici.dff.graph.IMPieChart
    android:id="@+id/imPieChart"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

2. Configure and display the chart in your Activity or Fragment:


```Kotlin
val chartData = listOf(
    LegendAdapter.LegendItem( "Item 1",getAssetColor(R.color.maroon),50f),
    LegendAdapter.LegendItem( "Item 2",getAssetColor(R.color.bronze),30f),
    LegendAdapter.LegendItem( "Longer item name 3",getAssetColor(R.color.warning),20f))
binding.pieChart.createChart(chartData)
```
