package com.chanakya.dialoge

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chanakya.testview.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PickerOptionsBottomSheet(
    private val onOptionSelected: (ImagePickerButton.SourceType) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_picker_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.optionCamera).setOnClickListener {
            onOptionSelected(ImagePickerButton.SourceType.CAMERA)
            dismiss()
        }

        view.findViewById<View>(R.id.optionGallery).setOnClickListener {
            onOptionSelected(ImagePickerButton.SourceType.GALLERY)
            dismiss()
        }
    }
}
