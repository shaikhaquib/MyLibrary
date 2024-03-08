
# IMDataTableView

`IMDataTableView` is a custom view class that extends `MaterialCardView` to create a scrollable and flexible table view for displaying data. It is designed to handle both horizontal and vertical scrolling, and it can display dividers between rows and columns.

## Features

- Synchronized horizontal and vertical scrolling for both the main table and the headers.
- Customizable cell height, minimum and maximum cell width, and divider size.
- Optional display of vertical and horizontal dividers.
- Optional display of top and left headers with customizable background colors and text colors.
- Alternating row background colors for better readability.
- Data-driven design, allowing easy updates to the table content.

## Usage

To use `IMDataTableView`, you can include it in your XML layout file and then configure it programmatically in your activity or fragment.

### XML Layout

```xml
<com.yourpackage.IMDataTableView
    android:id="@+id/im_data_table_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

### Activity or Fragment

In your activity or fragment, you can configure the `IMDataTableView` by creating a `TableData` object and calling the `createTable` method.

```kotlin
val imDataTableView = findViewById<IMDataTableView>(R.id.im_data_table_view)

val columnTitles = listOf("Column 1", "Column 2", "Column 3")
val rowTitles = listOf("Row 1", "Row 2", "Row 3")
val tableData = listOf(
    listOf("Data 1-1", "Data 1-2", "Data 1-3"),
    listOf("Data 2-1", "Data 2-2", "Data 2-3"),
    listOf("Data 3-1", "Data 3-2", "Data 3-3")
)

val tableDataObject = IMDataTableView.TableData(columnTitles, rowTitles, tableData)

imDataTableView.createTable(
    tableData = tableDataObject,
    showVerticalDivider = true, // Optional
    showHorizontalDivider = true, // Optional
    CELL_HEIGHT = resources.getDimensionPixelSize(R.dimen.dimen_40dp), // Optional
    MIN_CELL_WIDTH = resources.getDimensionPixelSize(R.dimen.dimen_122dp), // Optional
    MAX_CELL_WIDTH = null, // Optional
    DIVIDER_SIZE = resources.getDimensionPixelSize(R.dimen.dimen_1dp), // Optional
    displayTopHeader = true, // Optional
    displayLeftHeader = true, // Optional
    topHeaderBackGroundColor = ContextCompat.getColor(this, R.color.top_header_bg), // Optional
    leftHeaderBackGroundColor = ContextCompat.getColor(this, R.color.left_header_bg), // Optional
    rowBackgroundColor = null, // Optional
    topHeaderTextColor = ContextCompat.getColor(this, R.color.top_header_text), // Optional
    leftHeaderTextColor = ContextCompat.getColor(this, R.color.left_header_text) // Optional
)
```

### Customization

You can customize the appearance of the table by adjusting the parameters in the `createTable` method. For example, you can set `showVerticalDivider` and `showHorizontalDivider` to `false` if you don't want to display dividers. You can also customize the colors and dimensions of the headers and cells.
