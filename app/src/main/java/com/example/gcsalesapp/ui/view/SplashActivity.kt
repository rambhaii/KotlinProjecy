package com.example.gcsalesapp.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.gcsalesapp.R
import com.example.gcsalesapp.ui.viewmodal.LoginViewModal
import com.example.gcsalesapp.ui.viewmodal.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash.*


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.checkLoginStatus();

        app_logo.alpha = 0f
        app_logo.animate().setDuration(1500).alpha(1f).withEndAction {

            if (viewModel.getLoginStatus()) {
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
            } else {
                val i = Intent(this, HomeActivity::class.java)
               // val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
            }

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}