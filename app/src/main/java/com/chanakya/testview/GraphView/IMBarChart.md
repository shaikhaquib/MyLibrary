# IMBarChart
IMBarChart is a custom view component for Android, built on top of the powerful MPAndroidChart library, designed to simplify the process of displaying bar charts with rounded corners, custom bar colors, and added features like average line display. It extends MaterialCardView for consistent material design styling.

# Features
- Easy integration into any Android project.
- Customizable bar colors for individual bars.
- Rounded corners for bars.
- Automatic calculation and display of an average value line.
- Customizable X-axis labels.
- Simple API for data updating and customization.
- Prerequisites
- Ensure you have the MPAndroidChart library added to your project. If not, include it in your build.gradle file:

``` groovy
dependencies {
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
}
```
# Integration
To use IMBarChart in your project, follow these steps:

# XML Layout
Include IMBarChart in your XML layout:

```xml
<com.chanakya.testview.GraphView.IMBarChart
    android:id="@+id/imBarChart"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```
# Initialize and Populate Data
In your activity or fragment, initialize the chart and populate it with data:

``` kotlin
val barEntries = ArrayList<BarEntry>().apply {
    add(BarEntry(1f, 1f))
    add(BarEntry(2f, 2f))
    // Add more entries as needed
}

val barColors = ArrayList<Int>().apply {
    add(Color.RED)
    add(Color.BLUE)
    // Add more colors as needed
}
findViewById<IMBarChart>(R.id.imBarChart).apply {
    initializedBarChart(barEntries, barColors)
    setXAxisLabels(arrayListOf("Label 1", "Label 2"))
}
```
# Customization
Bar Colors
Pass an ArrayList<Int> of color resources to initializedBarChart to set custom colors for each bar.

# Average Line
The average line is calculated automatically based on the bar values. You can customize its appearance in the initializedBarChart method.

# X-Axis Labels
Use setXAxisLabels to define custom labels for the X-axis.