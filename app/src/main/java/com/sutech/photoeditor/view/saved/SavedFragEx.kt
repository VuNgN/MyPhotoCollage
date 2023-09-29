package com.sutech.photoeditor.view.saved

import android.media.MediaScannerConnection
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.sutech.photoeditor.R
import com.sutech.photoeditor.util.*
import kotlinx.android.synthetic.main.fragment_saved.*
import kotlinx.android.synthetic.main.toolbar_base.*
import java.io.File

fun SavedFrag.loadImage() {
    val images = DeviceUtil.getImageFromFolderName(requireContext(), AppConstant.FOLDER_PHOTO_LAYOUT)
    if (images != null) {
        listImage.clear()
        listImage.addAll(images)
        adapterSaved.notifyDataSetChanged()
    }

}

fun SavedFrag.initOnBackPress() {
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        if (layoutFullImage.isShow()) {
            layoutFullImage.gone()

        } else {
            findNavController().popBackStack(R.id.homeFrag, false)
        }
    }
}

fun SavedFrag.initClick() {
    btnBackToolbar.setPreventDoubleClickScaleView(500) {
        findNavController().popBackStack(R.id.homeFrag, false)
    }

    ivBackFullCreated.setPreventDoubleClickScaleView(500) {
        layoutFullImage.gone()
    }
    ivShareCreated.setPreventDoubleClickScaleView(2000) {
        SharePhotorUtils.getInstance().sendShareMore(context, listImage[positon].pathImage)
    }
    ivDeleteCreated.setPreventDoubleClickScaleView(500) {
        DialogUtil.showDialogAlert(requireContext(), getString(R.string.want_delete), {
            val file = File(AppUtil.getRealPathFromURI(requireContext(), listImage[positon].pathImage))
            deleteImageFile(file.path)
        }, {})


    }

}

fun SavedFrag.deleteSuccess(imgUrl: String) {
    Toast.makeText(requireContext(), "Delete Success", Toast.LENGTH_SHORT).show()
    MediaScannerConnection.scanFile(
        requireContext(), arrayOf(imgUrl),
        null, null
    )
}

fun SavedFrag.deleteImageFile(imgUrl: String) {
    val file = File(imgUrl)
    try {
        if (file.exists()) {
            file.delete()
            file.canonicalFile.delete()
            requireActivity().application.applicationContext.deleteFile(file.name)
            if (!file.exists()) {
                deleteSuccess(imgUrl)
                listImage.removeAt(positon)
                adapterSaved.notifyDataSetChanged()
                layoutFullImage.gone()
            } else {
                Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {

    }
}