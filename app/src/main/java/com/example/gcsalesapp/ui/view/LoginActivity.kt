package com.example.gcsalesapp.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.gcsalesapp.R
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.data.request.ValidateSalesPersonRequest
import com.example.gcsalesapp.services.TrackingService
import com.example.gcsalesapp.ui.view.dialogs.CustomMessageDialog
import com.example.gcsalesapp.ui.view.dialogs.CustomProgressDialog
import com.example.gcsalesapp.ui.viewmodal.LoginViewModal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModal by viewModels()
    private val progressDialog = CustomProgressDialog()
    private val messageDialog = CustomMessageDialog()

    @Inject
    @Named("MachineID")
    lateinit var MachineID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // development user
//        phone_number_et.setText("9811581625")
//        password_et.setText("0831")

//        Live user
//        phone_number_et.setText("7058559095")
//        password_et.setText("9932")

        sign_in_button.setOnClickListener {


            viewModel.loginSellsPerson(
                ValidateSalesPersonRequest(
                    Constants.APP_MINOR_UPDATE,
                    Constants.APP_UPDATE,
                    MachineID,
                    Constants.DEFAULT_LANGUAGE,
                    MachineID,
                    Constants.ANDROID_OS,
                    password_et.text.toString(),
                    phone_number_et.text.toString()
                )
            )

        }


        viewModel.validateResp.observe(this, {

                state ->
            when (state) {

                is LoginViewModal.ValidateState.Loading -> {
                    progressDialog.show(this, "Please Wait...")
                }
                is LoginViewModal.ValidateState.Failure -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dialog.dismiss()
                    }
                    messageDialog.show(this, CustomMessageDialog.MessageType.ERROR, state.errorText)
                }
                is LoginViewModal.ValidateState.Success -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dialog.dismiss()
                    }

                    Intent(this,HomeActivity::class.java).also {
                        startActivity(it)
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                        finish()
                    }


//                    messageDialog.show(this, CustomMessageDialog.MessageType.SUCCESS, "Welcome ${state.salesStaff.MarketingRepFName} ${state.salesStaff.MarketingRepLName} ${state.salesStaff.MarketingRepID}")
                }


            }

        })

    }

    override fun onResume() {
        super.onResume()
        Intent(this, TrackingService::class.java).also {
            stopService(it)
        }
    }
}