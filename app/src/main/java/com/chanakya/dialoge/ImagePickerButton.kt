package com.chanakya.dialoge

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chanakya.testview.R
import java.io.File

class ImagePickerButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val button: Button
    private val previewList: RecyclerView
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private val selectedImages = mutableListOf<Uri>()
    private lateinit var config: Config
    private lateinit var callback: (List<File>, List<Uri>) -> Unit
    private var tempImageUri: Uri? = null

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.image_picker_button, this, true)
        button = findViewById(R.id.btnIMage)
        previewList = findViewById(R.id.previewList)
        previewList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        button.setOnClickListener {
            showPickerOptions()
        }
    }

    fun configure(
        config: Config,
        activity: ComponentActivity,
        callback: (List<File>, List<Uri>) -> Unit
    ) {
        this.config = config
        this.callback = callback
        setupLaunchers(activity)
    }

    fun configure(
        config: Config,
        fragment: Fragment,
        callback: (List<File>, List<Uri>) -> Unit
    ) {
        this.config = config
        this.callback = callback
        setupLaunchers(fragment)
    }

    private fun setupLaunchers(activity: ComponentActivity) {
        imagePickerLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result.resultCode, result.data)
        }

        cameraLauncher = activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            handleCameraResult(success)
        }
    }

    private fun setupLaunchers(fragment: Fragment) {
        imagePickerLauncher = fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result.resultCode, result.data)
        }

        cameraLauncher = fragment.registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            handleCameraResult(success)
        }
    }

    private fun handleActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val clipData = data?.clipData
            val uriList = mutableListOf<Uri>()
            val fileList = mutableListOf<File>()
            clipData?.let {
                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    uriList.add(uri)
                    fileList.add(File(uri.path ?: ""))
                }
            } ?: data?.data?.let {
                uriList.add(it)
                fileList.add(File(it.path ?: ""))
            }
            selectedImages.clear()
            selectedImages.addAll(uriList)
            updatePreview()
            callback(fileList, uriList)
        }
    }

    private fun handleCameraResult(success: Boolean) {
        if (success) {
            tempImageUri?.let { uri ->
                val file = File(uri.path ?: "")
                selectedImages.add(uri)
                updatePreview()
                callback(listOf(file), listOf(uri))
            }
        }
    }

    private fun showPickerOptions() {
        when (config.sourceType) {
            SourceType.CAMERA -> openCamera()
            SourceType.GALLERY -> openGallery()
            SourceType.BOTH -> showBottomSheet()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, config.maxPhotos > 1)
        imagePickerLauncher.launch(intent)
    }

    private fun openCamera() {
        val storageDir = config.storageDirectory ?: context.getExternalFilesDir(null)
        val file = File.createTempFile("IMG_", ".jpg", storageDir)
        tempImageUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        tempImageUri?.let {
            cameraLauncher.launch(it)
        }
    }

    private fun showBottomSheet() {
        val bottomSheet = PickerOptionsBottomSheet { sourceType ->
            when (sourceType) {
                SourceType.CAMERA -> openCamera()
                SourceType.GALLERY -> openGallery()
                else ->{openCamera()}
            }
        }
        bottomSheet.show((context as AppCompatActivity).supportFragmentManager, bottomSheet.tag)
    }

    private fun updatePreview() {
        previewList.isVisible = selectedImages.isNotEmpty()
        previewList.adapter = ImagePreviewAdapter(selectedImages)
    }

    data class Config(
        val maxPhotos: Int = 1,
        val storageDirectory: File? = null,
        val sourceType: SourceType = SourceType.BOTH,
        val compressionLevel: Int = 0 // 0 means no compression
    )

    enum class SourceType {
        CAMERA, GALLERY, BOTH
    }
}
