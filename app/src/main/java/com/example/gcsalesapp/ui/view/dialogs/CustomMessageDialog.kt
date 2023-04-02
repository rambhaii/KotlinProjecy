package com.example.gcsalesapp.ui.view.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.gcsalesapp.R
import com.example.gcsalesapp.repository.Utill
import kotlinx.android.synthetic.main.dialog_message.view.*

class CustomMessageDialog {

    enum class MessageType {
        SUCCESS,
        ERROR,
        WARNING,
        NOTHING
    }

    lateinit var dialog: CustomDialog
    var isShowing = false

    fun show(context: Context): Dialog {
        return show(context, MessageType.NOTHING,"")
    }

    @SuppressLint("ResourceAsColor")
    fun show(context: Context, messageType: MessageType, message: String): Dialog {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.dialog_message, null)

        when (messageType) {

            MessageType.SUCCESS -> {
                Utill.setColorFilter(view.msgDialogHeader.background, ResourcesCompat.getColor(context.resources, R.color.green, null))
//                view.msgDialogHeader.setBackgroundColor(R.color.green)
                view.msgDialogImg.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
                view.msgDialogText.text = message
                view.msgDialogTitle.text = "Success"
            }
            MessageType.ERROR -> {
                Utill.setColorFilter(view.msgDialogHeader.background, ResourcesCompat.getColor(context.resources, R.color.redTxt, null))
//                view.msgDialogHeader.setBackgroundColor(R.color.redTxt)
                view.msgDialogImg.setImageResource(R.drawable.ic_baseline_error_outline_24)
                view.msgDialogText.text = message
                view.msgDialogTitle.text ="Error"
            }
            MessageType.WARNING -> {
                Utill.setColorFilter(view.msgDialogHeader.background, ResourcesCompat.getColor(context.resources, R.color.teal_500, null))
//                view.msgDialogHeader.setBackgroundColor(R.color.teal_500)
                view.msgDialogImg.setImageResource(R.drawable.ic_baseline_error_outline_24)
                view.msgDialogText.text = message
                view.msgDialogTitle.text = "Warning"
            }
            MessageType.NOTHING -> {
                Utill.setColorFilter(view.msgDialogHeader.background, ResourcesCompat.getColor(context.resources, R.color.greyTxt, null))
                view.msgDialogImg.visibility = View.GONE
                view.msgDialogText.text = ""
                view.msgDialogTitle.text = ""
            }

        }

        view.msgDialogButton.setOnClickListener {
            dialog.dismiss()
        }


        dialog = CustomDialog(context)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
        dialog.setContentView(view)
//        dialog.setCancelable(false)
        dialog.setOnCancelListener {
            isShowing = false;
        }
        dialog.show()
        isShowing = true;
        return dialog
    }

//    private fun setColorFilter(drawable: Drawable, color: Int) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
//        } else {
//            @Suppress("DEPRECATION")
//            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
//        }
//    }

    class CustomDialog(context: Context) : Dialog(context, R.style.ProgressDialogTheme) {
        init {
            // Set Semi-Transparent Color for Dialog Background
            window?.decorView?.rootView?.setBackgroundResource(R.color.semi_transparent)
            window?.decorView?.setOnApplyWindowInsetsListener { _, insets ->
                insets.consumeSystemWindowInsets()
            }
        }
    }




}