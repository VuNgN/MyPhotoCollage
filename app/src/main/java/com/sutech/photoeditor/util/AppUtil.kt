package com.sutech.photoeditor.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import android.os.SystemClock
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.sutech.photoeditor.R
import com.sutech.photoeditor.model.ImageGalleryObj
import java.io.File
import java.time.YearMonth
import java.util.*
import java.util.regex.Pattern

object AppUtil {


    const val SKU_FOREVER_FAKE= "remove_ads_fake"
    const val SKU_FOREVER="remove_ads"
      var PRICE_FOREVER= "remove_ads_fake"
      var PRICE_FOREVER_FAKE="remove_ads"
    const val EXTRA_TYPE= "EXTRA_TYPE"
    const val EXTRA_TYPE_COLLAGE= "EXTRA_TYPE_COLLAGE"
    const val EXTRA_TYPE_EDIT= "EXTRA_TYPE_EDIT"

    const val TYPE_MP3 = ".mp3"
    const val AudioName = "LastRecord"

    const val VOICE_CHANGER_FOLDER = "AudioNote"
    const val CACHE_FOLDER = ".Voice"
    const val NO_MEDIA_FILE = ".nomedia"

    const val NONE = "none"
    const val START = "start"
    const val STOP = "stop"
    const val RESUME = "RESUME"

    var isShowRateHome = 0

    var countAdsEdit = 1
    var countAds = 1
    var isIAP = false
    var listImageSelected = mutableListOf<ImageGalleryObj>()
    var imageSelected : ImageGalleryObj ?= null
    var savedImage : Uri ?= null


    private var mLastClickTime: Long = 0
    val listTheme = mutableListOf(
        R.color.white,
        R.color.colorTextBlack,
        R.color.blue,
        R.color.red,
        R.color.green,
        R.color.purple,
        R.color.yellow,
        R.color.orange,
        R.color.pink
    )
    val listColor =  mutableListOf(
        R.color.note_white,
        R.color.note_blue,
        R.color.note_red,
        R.color.note_green,
        R.color.note_purple,
        R.color.note_yellow,
        R.color.note_orange,
        R.color.note_pink
    )
    fun getWidthScreen(context: Activity): Int {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }


    fun getRealPathFromURI(context:Context,contentUri: Uri): String {
        var result: String ?= ""
       val proj =  arrayOf( MediaStore.Images.Media.DATA )
        val cursor: Cursor? =  context.contentResolver.query(contentUri, proj, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentUri.path
        } else {
            cursor.moveToFirst()
            try {
                if (cursor.count>0){
                val idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                result = cursor.getString(idx)
            }else{
                    result = contentUri.path
                }
            }catch (e:Exception){
                Log.e("TAG", "getRealPathFromURI: ${e.message}" )
            }
            cursor.close()
        }
        if (result == null){
            result = ""
        }
        return result
    }


    fun getHeightScreen(context: Activity): Int {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    val currentTime: String
        get() = Calendar.getInstance().time.toString() + ""

    fun clickOneSecond(): Boolean {
        return if (SystemClock.elapsedRealtime() - mLastClickTime >= 1000) {
            mLastClickTime = SystemClock.elapsedRealtime()
            true
        }else{
            false
        }
    }

    fun showToast(context: Context?, message: String?) {
        if (clickOneSecond()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun logE(TAG: String?, message: String?) {
        Log.e(TAG, message!!)
    }

    fun showToast(context: Context?, message: Int) {
        if (clickOneSecond()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun numberCart(count: Int): String {
        return if (count < 99) {
            count.toString() + ""
        } else {
            99.toString() + "+"
        }
    }

    fun isStrValid(text: String?): Boolean {
        return text != null && text.isNotEmpty()
    }

    fun isPhoneValid(text: String?): Boolean {
        return text != null && text.isNotEmpty() && text.length >= 9 && text.length <= 11
    }

    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern =
            Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isPassWordValid(password: String): Boolean {
        return password.length > 5
    }

    fun confirmPassWordValid(password: String, rePassword: String): Boolean {
        return password == rePassword.trim { it <= ' ' }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val conMgr =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val i = conMgr.activeNetworkInfo
        if (i == null) {
            showToast(context, R.string.no_internet_connected)
            return false
        }
        if (!i.isConnected) {
            showToast(context, R.string.no_internet_connected)
            return false
        }
        return i.isAvailable
    }

//    fun coppyText(context: Context, text: String?) {
//        val cm =
//            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//        cm.text = text
//        showToast(context, R.string.copy_successful)
//    }

    fun countDayInMonth(month: Int, year: Int): Int {
        var daysInMonth: Int = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val yearMonthObject: YearMonth = YearMonth.of(year, month)
            daysInMonth = yearMonthObject.lengthOfMonth()
        } else {
            // Create a calendar object and set year and month
            val mycal: Calendar = GregorianCalendar(year, month, 1)
            // Get the number of days in that month
            daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
        return daysInMonth
    }

    fun deleteFileFromInternalStorage(imagePath: String): Boolean {
        val file = File(imagePath)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }


      fun openMarket(context: Context, packageName: String) {
        val i = Intent(Intent.ACTION_VIEW)
        try {
            i.data = Uri.parse("market://details?id=$packageName")
            context.startActivity(i)
        } catch (ex: ActivityNotFoundException) {
            openBrowser(
                context,
                "https://play.google.com/store/apps/details?id=\"$packageName"
            )
        }
    }

      fun openBrowser(context: Context, urlApp: String) {
        var url = urlApp
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://$url"
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            context.startActivity(browserIntent)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    fun sendEmailFeedBack(
        context: Context,
        mail: Array<String>,
        subject: String,
        body: String
    ) {
        disableExposure()
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, mail)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)
        try {
            context.startActivity(intent)
        }catch (e:Exception){
            Toast.makeText(context, "you need install gmail", Toast.LENGTH_SHORT).show()
        }
    }
    fun disableExposure() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}