package com.chanakya.testview.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chanakya.testview.R
import com.google.android.material.slider.Slider
import com.chanakya.testview.databinding.SliderContinuosWithLabelBinding
import java.text.DecimalFormat
import kotlin.math.roundToInt
import kotlin.properties.Delegates


/**
 * TODO: document your custom view class.
 */
class IMContinuousSliderWithLabel @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : LinearLayout(context, attrs, defStyleAttr) {
    private var sliderValue by Delegates.notNull<Float>()
    var indicatorView: View? = null
    val binding: SliderContinuosWithLabelBinding
    private var sliderLabelText = ""
    var isUnEventSteps: Boolean = false
    private var isDecimalFormat: Boolean = false
    var listItemArr: List<Int>? = null
    var onSliderChangeListener: ((Int) -> Unit)? = null
    var changeListener: SeekbarChangeListener? = null
    var sliderListener: OnSliderTouchListener? = null

    var decim: DecimalFormat = DecimalFormat("#,###.##")

    init {
        // Load attributes
        binding = SliderContinuosWithLabelBinding.inflate(
            LayoutInflater.from(context),
            this, true
        )

        setComponentValue(attrs, context)
        doTheMagicIn(context)
    }


    fun doTheMagicIn(context: Context, indicatorLayout: Int = R.layout.indicator) {
        indicatorView = LayoutInflater.from(context).inflate(indicatorLayout, null, false)

        binding.slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                sliderListener?.onStartTrackingTouch(slider)
                binding.seekBarIndicator.visibility = View.VISIBLE

                slider.thumbRadius = resources.getDimensionPixelSize(R.dimen.dimen_12dp)
                slider.thumbStrokeWidth = resources.getDimension(R.dimen.dimen_6dp)
                slider.setThumbStrokeColorResource(R.color.primary_orange_100)
            }


            override fun onStopTrackingTouch(slider: Slider) {
                sliderListener?.onStopTrackingTouch(slider)
                slider.thumbRadius = resources.getDimensionPixelSize(R.dimen.dimen_10dp)
                slider.thumbStrokeWidth = resources.getDimension(R.dimen.dimen_2dp)
                if (slider.value == 0f) {
                    slider.setThumbStrokeColorResource(R.color.grey_130)
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    binding.seekBarIndicator.visibility = View.INVISIBLE
                }, 500)

            }
        })

        binding.slider.addOnChangeListener(
            Slider.OnChangeListener { slider, value, fromUser ->
                binding.seekBarIndicator.progress = slider.value.toInt()
            })

        binding.seekBarIndicator.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progressValue: Int, p2: Boolean) {
                Log.d(
                    "progressValue",
                    "" + progressValue + " start ${seekBar?.progress} end ${seekBar?.max}"
                )
                //binding.seekBarIndicator.progress = progressValue
                changeListener?.onProgress(seekBar, progressValue)
                if (isUnEventSteps) {
                    onSliderChangeListener?.invoke(listItemArr?.get(progressValue) ?: 0)
                    seekBar!!.thumb =
                        getThumb(listItemArr?.get(progressValue) ?: 0, indicatorView!!)
                } else {
                    onSliderChangeListener?.invoke(progressValue)
                    seekBar!!.thumb = getThumb(progressValue, indicatorView!!)
                }
                /* if (progressValue.toFloat() >= 0.0)
                  binding.distanceSeekBar.value = progressValue.toFloat()*/
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                changeListener?.onStartTracking(seekBar)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                changeListener?.onStopTracking(seekBar)
            }
        })
        binding.seekBarIndicator.setOnTouchListener(OnTouchListener { view, motionEvent -> true })
        // to invalidate the change of the thumb
        binding.seekBarIndicator.thumb = getThumb(binding.slider.valueFrom.toInt(), indicatorView!!)
    }

    private fun getThumb(progress: Int, indicator: View): Drawable {
        (indicator.findViewById<View>(R.id.progress_text) as TextView).text = if (isDecimalFormat) {
            "${decim.format(progress)}$sliderLabelText"
        } else {
            "$progress$sliderLabelText"
        }

        indicator.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bitmap = Bitmap.createBitmap(
            indicator.measuredWidth,
            indicator.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        indicator.layout(0, 0, indicator.measuredWidth, indicator.measuredHeight)
        indicator.draw(canvas)

        return BitmapDrawable(resources, bitmap)
    }

    private fun setComponentValue(attrs: AttributeSet?, context: Context) {
        val attrsDataArray = context.obtainStyledAttributes(attrs, R.styleable.ContinuousSliderDff)

        binding.slider.stepSize =
            attrsDataArray.getFloat(R.styleable.ContinuousSliderDff_android_stepSize, 1.0F)
        binding.slider.valueFrom =
            attrsDataArray.getFloat(R.styleable.ContinuousSliderDff_android_valueFrom, 0.0F)
        binding.slider.valueTo =
            attrsDataArray.getFloat(R.styleable.ContinuousSliderDff_android_valueTo, 10.0F)

        binding.seekBarIndicator.max =
            attrsDataArray.getFloat(R.styleable.ContinuousSliderDff_android_valueTo, 10.0F)
                .roundToInt()

        binding.slider.tickActiveTintList =
            attrsDataArray.getColorStateList(R.styleable.ContinuousSliderDff_tickColorActive)
                ?: ContextCompat.getColorStateList(context, R.color.primary_orange)!!
        binding.slider.tickInactiveTintList =
            attrsDataArray.getColorStateList(R.styleable.ContinuousSliderDff_tickColorInactive)
                ?: ContextCompat.getColorStateList(context, R.color.grey)!!
        binding.slider.trackActiveTintList =
            attrsDataArray.getColorStateList(R.styleable.ContinuousSliderDff_trackColorActive)
                ?: ContextCompat.getColorStateList(context, R.color.primary_orange)!!
        binding.slider.trackInactiveTintList =
            attrsDataArray.getColorStateList(R.styleable.ContinuousSliderDff_trackColorInActive)
                ?: ContextCompat.getColorStateList(context, R.color.divider_color)!!

        val labelBehaviour =
            attrsDataArray.getBoolean(R.styleable.ContinuousSliderDff_sliderLabel, false)

        val trackHeight =
            attrsDataArray.getDimensionPixelSize(R.styleable.ContinuousSliderDff_trackHeight, -1)

        if (trackHeight != -1) {
            binding.slider.trackHeight = trackHeight
        }

        sliderLabelText(
            attrsDataArray.getString(R.styleable.ContinuousSliderDff_sliderLabelText).orEmpty()
        )


        val textPadding = attrsDataArray.getDimensionPixelSize(
            R.styleable.ContinuousSliderDff_thumbTextPadding,
            context.resources.getDimensionPixelSize(R.dimen.dimen_36dp)
        )
        if (textPadding > 0) {
            setTextPadding(textPadding)
        }

        val thumbOffset = attrsDataArray.getDimensionPixelSize(
            R.styleable.ContinuousSliderDff_android_thumbOffset,
            0
        )
        if (thumbOffset > 0) {
            setThumbOffset(thumbOffset)
        }

        isDecimalFormat =
            attrsDataArray.getBoolean(R.styleable.ContinuousSliderDff_decimalFormat, false)

        // if(thu)
        attrsDataArray.recycle()

    }

    fun setStepSize(stepSize: Float) {
        binding.slider.stepSize = stepSize
    }

    fun setValueFrom(valueFrom: Float) {
        binding.slider.valueFrom = valueFrom
        // Update SeekBar's minimum to match Slider's valueFrom if applicable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.seekBarIndicator.min = valueFrom.toInt()
        } // Make sure SeekBar supports this method or adjust accordingly
        // Update any dependent UI or logic here if needed
    }

    fun setValueTo(valueTo: Float) {
        binding.slider.valueTo = valueTo
        // Update SeekBar's maximum to match Slider's valueTo
        binding.seekBarIndicator.max = valueTo.roundToInt()
        // Update any dependent UI or logic here if needed
    }


    fun setThumbOffset(thumbOffset: Int) {
        binding.seekBarIndicator.thumbOffset = thumbOffset
    }

    fun setTextPadding(textPadding: Int) {
        binding.seekBarIndicator.setPadding(textPadding, 0, textPadding, 0)
    }

    fun sliderLabelText(sliderLabel: String) {
        sliderLabelText = sliderLabel
    }

    //    this function is used to getSlider values
    fun getSliderValue(value: Float) {
        sliderValue = value
    }

    fun setUnEventSteps(isEvenStep: Boolean = true, list: List<Int>) {
        isUnEventSteps = true
        binding.slider.stepSize = 1.0F
        binding.slider.valueTo = list.size - 1.0F
        binding.slider.valueFrom = 0.0F

        binding.seekBarIndicator.progress = 0
        binding.seekBarIndicator.max = list.size - 1

        listItemArr = list

        binding.seekBarIndicator!!.thumb = getThumb(listItemArr?.get(0) ?: 0, indicatorView!!)

    }

    /**
     * This function is used to set Slider value in Float
     */
    fun setSeekBarDefaultValues(end: Float) {
        binding.slider.setValue(end)
    }

    /**
     * This function is used to get Slider value in Float
     */
    fun getValues(): Float = if (isUnEventSteps) {
        listItemArr?.get(binding.slider.value.toInt())!!.toFloat()
    } else binding.slider.value

    fun setDecimalFormat(pattern: String) {
        decim = DecimalFormat(pattern)
    }

    fun setOnTrackerListener(listener: SeekbarChangeListener) {
        changeListener = listener
    }

    fun addOnSliderTouchListener(listener: OnSliderTouchListener) {
        sliderListener = listener
    }

    interface SeekbarChangeListener {
        fun onProgress(seekBar: SeekBar?, progressValue: Int)
        fun onStartTracking(seekBar: SeekBar?)
        fun onStopTracking(seekBar: SeekBar?)
    }

    interface OnSliderTouchListener {
        fun onStartTrackingTouch(slider: Slider)
        fun onStopTrackingTouch(slider: Slider)
    }

}