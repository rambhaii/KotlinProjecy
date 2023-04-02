package com.example.gcsalesapp.ui.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.example.gcsalesapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.buttom_sheet_dialog.view.*


class EstimateBottomSheetDialog(val callBackListener: CallBackListener) : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(
            R.layout.buttom_sheet_dialog,
            container, false
        )

        v.estimate_ok_btn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("estimate",v.estimate_edt.text.toString().trim())
            callBackListener.onCallBack(bundle)
            dismiss()
        }

        return v
    }

}

interface EstimateBottomSheetInterface {
    public fun onEstimateSet(estimateVolume: String)
}