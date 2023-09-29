package com.sutech.photoeditor.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sutech.photoeditor.BuildConfig
import com.sutech.photoeditor.R
import com.sutech.photoeditor.adapter.ChooseColorAdapter
import kotlinx.android.synthetic.main.dialog_alert.*
import kotlinx.android.synthetic.main.dialog_choose_color.*
import kotlinx.android.synthetic.main.dialog_experience.*
import kotlinx.android.synthetic.main.dialog_input_text.*
import kotlinx.android.synthetic.main.dialog_loading.*
import kotlinx.android.synthetic.main.dialog_more_note.*


object DialogUtil {
    fun showDialogAlert(
        context: Context,
        content: String,
        onClickOk: () -> Unit,
        onClickCancel: () -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_alert)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.tvContentDialog.text = content
        dialog.btnConfirmDialog.setPreventDoubleClickScaleView {
            onClickOk()
            dialog.cancel()
        }
        dialog.btnCancelDialog.setPreventDoubleClickScaleView {
            onClickCancel()
            dialog.cancel()
        }

        dialog.show()

    }

    fun showDialogRate(
        context: Context,
        clickBad: () -> Unit,
        clickGood: () -> Unit,
        clickExcellent: () -> Unit,
        clickNoThank: () -> Unit,
        clickGotoPlay: () -> Unit,
        clickFeedbackToUs: () -> Unit,
        clickMailToUs: () -> Unit,
        clickCancel: () -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_experience)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.linearBad.setPreventDoubleClick {
            clickBad()
            dialog. btnCloseRate.setVisibility(View.VISIBLE)
             dialog.linearFeedback.setVisibility(View.VISIBLE)
             dialog.textViewNoThanks.setVisibility(View.GONE)
             dialog.imageViewIconFeedback.setImageResource(R.drawable.ic_email_white)
             dialog.textViewFeedback.setText("Feed back to us")
             dialog.textViewShow.setText("Error Reporting \n Let us know about application.")
             dialog.linearButtonFeedback.setOnClickListener(View.OnClickListener {
                 clickFeedbackToUs()
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback ${context.getString(R.string.app_name)} ")
                intent.putExtra(Intent.EXTRA_TEXT, "What is your problem?")
                intent.data =
                    Uri.parse("mailto:sutechmobile@gmail.com") // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // this will make such that when user returns to your app, your app is displayed, instead of the email app.
               context. startActivity(intent)
                 dialog.dismiss()
            })
             dialog. iconBad.setImageResource(R.drawable.ic_selected_bad)
             dialog. iconGood.setImageResource(R.drawable.ic_good)
             dialog. iconExcellent.setImageResource(R.drawable.ic_excilent)
        }

        dialog.linearGood.setPreventDoubleClick {
            clickGood()

            dialog. btnCloseRate.setVisibility(View.VISIBLE)
        dialog.linearFeedback.setVisibility(View.VISIBLE)
          dialog.textViewNoThanks.setVisibility(View.GONE)
          dialog.imageViewIconFeedback.setImageResource(R.drawable.ic_email_white)
          dialog.textViewFeedback.setText("Mail to us")
          dialog.textViewShow.setText("Give Suggestions \n Contribute to improve the application.")
          dialog.linearButtonFeedback.setOnClickListener(View.OnClickListener {
              clickMailToUs()
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback ${context.getString(R.string.app_name)} ")
                intent.putExtra(Intent.EXTRA_TEXT, "What is your problem?")
                intent.data =
                    Uri.parse("mailto:sutechmobile@gmail.com") // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // this will make such that when user returns to your app, your app is displayed, instead of the email app.
               context. startActivity(intent)
              dialog.dismiss()
            })
             dialog. iconBad.setImageResource(R.drawable.ic_bad)
             dialog. iconGood.setImageResource(R.drawable.ic_selected_good)
             dialog. iconExcellent.setImageResource(R.drawable.ic_excilent)
        }

        dialog. linearExcellent.setPreventDoubleClick {
            clickExcellent()
            dialog. btnCloseRate.setVisibility(View.VISIBLE)
            dialog. linearFeedback.setVisibility(View.VISIBLE)
            dialog. textViewNoThanks.setVisibility(View.GONE)
            dialog. imageViewIconFeedback.setImageResource(R.drawable.ic_star_white)
            dialog. textViewFeedback.setText("Go to google play")
            dialog. textViewShow.setText("Thank you!\n Please rate this app in Google Play")
            dialog. linearButtonFeedback.setOnClickListener(View.OnClickListener {
                val url =  "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID.trimIndent()}"
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(context, Uri.parse(url))
                clickGotoPlay()
                dialog.dismiss()
            })
            dialog.iconBad.setImageResource(R.drawable.ic_bad)
            dialog.iconGood.setImageResource(R.drawable.ic_good)
            dialog.iconExcellent.setImageResource(R.drawable.ic_selected_excillent)
        }
        dialog. textViewNoThanks.setPreventDoubleClick {
            clickNoThank()
            dialog.dismiss()
        }

        dialog. btnCloseRate.setPreventDoubleClick {
            clickCancel()
            dialog.dismiss()
        }



        dialog.show()

    }

    fun showDialogInput(
        context: Context,
        title: String,
        content: String,
        onClickOk: (content: String) -> Unit,
        onClickCancel: () -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_input_text)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.tvDialogInput.text = title
        dialog.tvContentDialogInput.hint = content
        dialog.btnConfirmDialogInput.setPreventDoubleClickScaleView {
            onClickOk(dialog.tvContentDialogInput.text.toString())
            dialog.cancel()
        }
        dialog.btnCancelDialogInput.setPreventDoubleClickScaleView {
            onClickCancel()
            dialog.cancel()
        }

        dialog.show()

    }


    fun showDialogMoreNote(
        context: Context,
        onClickUpdate: () -> Unit,
        onClickDelete: () -> Unit,
        onClickCancel: () -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_more_note)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.btnUpdateDiary.setPreventDoubleClickScaleView {
            onClickUpdate()
            dialog.cancel()
        }
        dialog.btnDeleteDiary.setPreventDoubleClickScaleView {
            onClickDelete()
            dialog.cancel()
        }
        dialog.btnCancelMore.setPreventDoubleClickScaleView {
            onClickCancel()
            dialog.cancel()
        }
        dialog.show()
    }

    fun showDialogChooseColor(
        context: Context,
        listColor: MutableList<Int>,
        onClickColor: (position: Int) -> Unit
    ) {
        val dialog = Dialog(context)
        val chooseColorAdapter = ChooseColorAdapter(listColor) {
            onClickColor(it)
            dialog.cancel()
        }
        dialog.setContentView(R.layout.dialog_choose_color)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.rcvChooseImage.layoutManager =
            GridLayoutManager(context, 5, RecyclerView.VERTICAL, false)
        dialog.rcvChooseImage.adapter = chooseColorAdapter
        dialog.btnCancelColor.setPreventDoubleClickScaleView {
            dialog.cancel()
        }
        dialog.show()
    }


    private var dialogLoading: Dialog? = null

    @SuppressLint("SetTextI18n")
    fun showDialogLoading(
        context: Context,
        content: String? = null,
        sideContent: String? = null
    ) {
        dialogLoading = Dialog(context)
        dialogLoading?.setContentView(R.layout.dialog_loading)
        dialogLoading?.setCancelable(false)
        dialogLoading?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        if (!content.isNullOrBlank()) {
            dialogLoading?.tvLoadingContent?.text = content
        }
        if (!sideContent.isNullOrBlank()) {
            dialogLoading?.tvSideContent?.visibility = View.VISIBLE
            dialogLoading?.tvSideContent?.text = "($sideContent)"
        } else {
            dialogLoading?.tvSideContent?.visibility = View.GONE
        }
        dialogLoading?.show()
    }

    fun cancelDialogLoading() {
        dialogLoading?.cancel()
    }
}