package com.example.gcsalesapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.gcsalesapp.R
import com.example.gcsalesapp.data.response.Dealer
import com.example.gcsalesapp.ui.view.dialogs.CustomMessageDialog
import com.example.gcsalesapp.ui.view.dialogs.CustomProgressDialog
import com.example.gcsalesapp.ui.viewmodal.ApiState
import com.example.gcsalesapp.ui.viewmodal.MonitorDealerViewModel
import com.example.gcsalesapp.ui.viewmodal.RegisterDealerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register.*

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {


    private val viewModel: RegisterDealerViewModel by viewModels()
    private val progressDialog = CustomProgressDialog()
    private val messageDialog = CustomMessageDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dealer_submit_btn.setOnClickListener {
            val data = Bundle()
            data.putString("DealershipName", dealership_name_txt.text.toString())
            data.putString("MiddleName", dealer_middle_name_txt.text.toString())
            data.putString("Name", dealer_first_name_txt.text.toString())
            data.putString("PhoneNo", dealer_phone_txt.text.toString())
            data.putString("PinCode", dealer_pin_txt.text.toString())
            data.putString("Surname", dealer_last_name_txt.text.toString())
            viewModel.registerDealer(data)
        }


        viewModel.validateResp.observe(this, { state ->
            when (state) {

                is ApiState.Loading -> {
                    progressDialog.show(this, "Please Wait...")
                }
                is ApiState.Failure -> {
//                    swipe_refresh.isRefreshing=false
                    if (progressDialog.isShowing) {
                        progressDialog.dialog.dismiss()
                    }
                    messageDialog.show(this, CustomMessageDialog.MessageType.ERROR, state.errorText)
                }
                is ApiState.Success -> {
//                    swipe_refresh.isRefreshing=false
                    if (progressDialog.isShowing) {
                        progressDialog.dialog.dismiss()
                    }
                    messageDialog.show(this, CustomMessageDialog.MessageType.SUCCESS, state.message)
                        .setOnDismissListener {
                            dealership_name_txt.setText("")
                            dealer_first_name_txt.setText("")
                            dealer_last_name_txt.setText("")
                            dealer_middle_name_txt.setText("")
                            dealer_phone_txt.setText("")
                            dealer_pin_txt.setText("")
                            messageDialog.isShowing = false;
                        }
                }
            }
        })


    }
}