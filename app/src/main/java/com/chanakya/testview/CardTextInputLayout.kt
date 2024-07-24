package com.chanakya.testview

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CardTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val textInputLayout: TextInputLayout
    private val editText: TextInputEditText

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.card_text_input_layout, this, true)
        textInputLayout = view.findViewById(R.id.text_input_layout)
        editText = view.findViewById(R.id.edit_text)

        applyAttributes(attrs, defStyleAttr)
    }

    private fun applyAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CardTextInputLayout,
            defStyleAttr,
            0
        )

        try {
            // TextInputLayout attributes
            textInputLayout.hint = typedArray.getString(R.styleable.CardTextInputLayout_ctl_hint)
            textInputLayout.isHintEnabled = typedArray.getBoolean(R.styleable.CardTextInputLayout_ctl_hintEnabled, true)
            typedArray.getResourceId(R.styleable.CardTextInputLayout_ctl_hintTextAppearance, 0).takeIf { it != 0 }?.let {
                textInputLayout.setHintTextAppearance(it)
            }
            typedArray.getColorStateList(R.styleable.CardTextInputLayout_ctl_hintTextColor)?.let {
                textInputLayout.hintTextColor = it
            }
            textInputLayout.boxBackgroundMode = typedArray.getInt(R.styleable.CardTextInputLayout_ctl_boxBackgroundMode, TextInputLayout.BOX_BACKGROUND_FILLED)
            typedArray.getColor(R.styleable.CardTextInputLayout_ctl_boxBackgroundColor, -1).takeIf { it != -1 }?.let {
                textInputLayout.boxBackgroundColor = it
            }
            textInputLayout.setBoxCornerRadii(
                typedArray.getDimension(R.styleable.CardTextInputLayout_ctl_boxCornerRadiusTopStart, 0f),
                typedArray.getDimension(R.styleable.CardTextInputLayout_ctl_boxCornerRadiusTopEnd, 0f),
                typedArray.getDimension(R.styleable.CardTextInputLayout_ctl_boxCornerRadiusBottomStart, 0f),
                typedArray.getDimension(R.styleable.CardTextInputLayout_ctl_boxCornerRadiusBottomEnd, 0f)
            )
            typedArray.getColor(R.styleable.CardTextInputLayout_ctl_boxStrokeColor, -1).takeIf { it != -1 }?.let {
                textInputLayout.boxStrokeColor = it
            }
            textInputLayout.boxStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CardTextInputLayout_ctl_boxStrokeWidth, 0)
            textInputLayout.boxStrokeWidthFocused = typedArray.getDimensionPixelSize(R.styleable.CardTextInputLayout_ctl_boxStrokeWidthFocused, 0)
            textInputLayout.isCounterEnabled = typedArray.getBoolean(R.styleable.CardTextInputLayout_ctl_counterEnabled, false)
            textInputLayout.counterMaxLength = typedArray.getInt(R.styleable.CardTextInputLayout_ctl_counterMaxLength, -1)
            typedArray.getResourceId(R.styleable.CardTextInputLayout_ctl_counterTextAppearance, 0).takeIf { it != 0 }?.let {
                textInputLayout.setCounterTextAppearance(it)
            }
            typedArray.getResourceId(R.styleable.CardTextInputLayout_ctl_counterOverflowTextAppearance, 0).takeIf { it != 0 }?.let {
                textInputLayout.setCounterOverflowTextAppearance(it)
            }
            textInputLayout.isErrorEnabled = typedArray.getBoolean(R.styleable.CardTextInputLayout_ctl_errorEnabled, false)
            typedArray.getResourceId(R.styleable.CardTextInputLayout_ctl_errorTextAppearance, 0).takeIf { it != 0 }?.let {
                textInputLayout.setErrorTextAppearance(it)
            }
            textInputLayout.isHelperTextEnabled = typedArray.getBoolean(R.styleable.CardTextInputLayout_ctl_helperTextEnabled, false)
            textInputLayout.helperText = typedArray.getString(R.styleable.CardTextInputLayout_ctl_helperText)
            typedArray.getResourceId(R.styleable.CardTextInputLayout_ctl_helperTextTextAppearance, 0).takeIf { it != 0 }?.let {
                textInputLayout.setHelperTextTextAppearance(it)
            }
            textInputLayout.placeholderText = typedArray.getString(R.styleable.CardTextInputLayout_ctl_placeholderText)
            typedArray.getResourceId(R.styleable.CardTextInputLayout_ctl_placeholderTextAppearance, 0).takeIf { it != 0 }?.let {
                textInputLayout.setPlaceholderTextAppearance(it)
            }
            textInputLayout.prefixText = typedArray.getString(R.styleable.CardTextInputLayout_ctl_prefixText)
            typedArray.getResourceId(R.styleable.CardTextInputLayout_ctl_prefixTextAppearance, 0).takeIf { it != 0 }?.let {
                textInputLayout.setPrefixTextAppearance(it)
            }
            textInputLayout.suffixText = typedArray.getString(R.styleable.CardTextInputLayout_ctl_suffixText)
            typedArray.getResourceId(R.styleable.CardTextInputLayout_ctl_suffixTextAppearance, 0).takeIf { it != 0 }?.let {
                textInputLayout.setSuffixTextAppearance(it)
            }
            typedArray.getDrawable(R.styleable.CardTextInputLayout_ctl_startIconDrawable)?.let {
                textInputLayout.startIconDrawable = it
            }
            textInputLayout.setStartIconContentDescription(typedArray.getString(R.styleable.CardTextInputLayout_ctl_startIconContentDescription))
            textInputLayout.endIconMode = typedArray.getInt(R.styleable.CardTextInputLayout_ctl_endIconMode, TextInputLayout.END_ICON_NONE)
            typedArray.getDrawable(R.styleable.CardTextInputLayout_ctl_endIconDrawable)?.let {
                textInputLayout.endIconDrawable = it
            }
            textInputLayout.setEndIconContentDescription(typedArray.getString(R.styleable.CardTextInputLayout_ctl_endIconContentDescription))

            // TextInputEditText attributes
            editText.inputType = typedArray.getInt(R.styleable.CardTextInputLayout_ctl_inputType, InputType.TYPE_CLASS_TEXT)
            editText.setText(typedArray.getString(R.styleable.CardTextInputLayout_ctl_text))
            typedArray.getColorStateList(R.styleable.CardTextInputLayout_ctl_textColor)?.let {
                editText.setTextColor(it)
            }
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimension(R.styleable.CardTextInputLayout_ctl_textSize, editText.textSize))
            editText.setTypeface(editText.typeface, typedArray.getInt(R.styleable.CardTextInputLayout_ctl_textStyle, Typeface.NORMAL))
            editText.maxLines = typedArray.getInt(R.styleable.CardTextInputLayout_ctl_maxLines, Int.MAX_VALUE)
            editText.minLines = typedArray.getInt(R.styleable.CardTextInputLayout_ctl_minLines, 1)
            editText.filters = arrayOf(InputFilter.LengthFilter(typedArray.getInt(R.styleable.CardTextInputLayout_ctl_maxLength, Int.MAX_VALUE)))
            editText.imeOptions = typedArray.getInt(R.styleable.CardTextInputLayout_ctl_imeOptions, EditorInfo.IME_NULL)
            editText.isSingleLine = typedArray.getBoolean(R.styleable.CardTextInputLayout_ctl_singleLine, false)
        } finally {
            typedArray.recycle()
        }
    }

    // Delegate methods for TextInputLayout
    fun setHint(hint: CharSequence?) {
        textInputLayout.hint = hint
    }

    fun getHint(): CharSequence? = textInputLayout.hint

    fun setError(error: CharSequence?) {
        textInputLayout.error = error
    }

    fun setHelperText(helperText: CharSequence?) {
        textInputLayout.helperText = helperText
    }

    fun setStartIconDrawable(startIconDrawable: Drawable?) {
        textInputLayout.startIconDrawable = startIconDrawable
    }

    fun setEndIconDrawable(endIconDrawable: Drawable?) {
        textInputLayout.endIconDrawable = endIconDrawable
    }

    // Delegate methods for TextInputEditText
    fun setText(text: CharSequence?) {
        editText.setText(text)
    }

    fun getText(): Editable? = editText.text

    fun setInputType(type: Int) {
        editText.inputType = type
    }

    fun setMaxLines(maxLines: Int) {
        editText.maxLines = maxLines
    }

    fun setFilters(filters: Array<InputFilter>) {
        editText.filters = filters
    }

    // Add more delegate methods as needed
}