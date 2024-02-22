# DistributedLineGraph

`DistributedLineGraph` is a custom view for Android that extends `MaterialCardView` to display a line chart, leveraging the MPAndroidChart library. This component simplifies integrating line charts into your application by encapsulating common chart configurations and data management within a reusable view.

## Features

- Easy Configurabel
- Customizable X and Y axes, including label formatting.
- Configurable line color and style for different datasets.
- Supports zoom and drag functionalities out of the box.

## Installation

To use `DistributedLineGraph` in your project, ensure you have the MPAndroidChart library added to your `build.gradle` file:

```groovy
dependencies {
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
}
```

# XML
```xml
<com.yourpackagename.DistributedLineGraph
    android:id="@+id/customLineChartView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```




# Usage
To use `DistributedLineGraph`:

- *Initialize the View*: Obtain a reference to the view in your activity or fragment.
- *Set Data*: Use the `setDataToLineChart(chartData: List<List<Entry>>)` method to set the data for the chart.
- *Customize Appearance*: Utilize methods like `setXAxisLabels(labels: ArrayList<String>)` to customize the chart's appearance according to your needs.

