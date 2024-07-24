package com.chanakya.testview

import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.chanakya.dialoge.ImagePickerButton
import com.chanakya.testview.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    companion object {
        private const val REQUEST_PERMISSIONS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textInputLayout.setDefaultStrokeColor(resources.getColor(R.color.success))

        /*val customStrokeColor = resources.getColor(R.color.success)
        binding.textInputLayout.setBoxStrokeColor(customStrokeColor)

        // Set a focus change listener to maintain the stroke color
        binding.textInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // When not focused, reset the stroke color
                binding.textInputLayout.setBoxStrokeColor(customStrokeColor)
            }
        }*/
    }

    fun TextInputLayout.setDefaultStrokeColor(
        color: Int
    ) {
        try {
            val defaultStrokeColor = TextInputLayout::class.java.getDeclaredField("defaultStrokeColor")
            defaultStrokeColor.isAccessible = true
            defaultStrokeColor.set(this, color)
        } catch (e: NoSuchFieldException) {
            // failed to change the color
        }
    }

}

