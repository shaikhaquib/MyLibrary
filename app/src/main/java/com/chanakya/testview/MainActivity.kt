package com.chanakya.testview

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.chanakya.testview.databinding.ActivityMainBinding
import com.chanakya.testview.shadow.ShadowProperty
import com.chanakya.testview.shadow.ShadowViewDrawable


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // all side shadow
        // all side shadow


// only all sides except top shadow

// only all sides except top shadow
        val sp = ShadowProperty()
            .setShadowColor(0x770000FF)
            .setShadowDy(10)
            .setShadowRadius(10)
            .setShadowColor(Color.RED)
            .setShadowSide(ShadowProperty.BOTTOM)
        val sd = ShadowViewDrawable(sp, Color.TRANSPARENT, 0f, 0f)
        ViewCompat.setBackground(binding.lil, sd)
        ViewCompat.setLayerType(binding.lil, View.LAYER_TYPE_SOFTWARE, null)


    }


}

