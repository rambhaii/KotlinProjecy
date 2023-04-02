package com.example.gcsalesapp.api

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gcsalesapp.repository.Utill

class GCWebViewClient() : WebViewClient() {

    private val urlAction = MutableLiveData<String>()
    val validateResp: LiveData<String>
        get() = urlAction

//    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//        super.onPageStarted(view, url, favicon)
//    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
//        http://udev.scci.co.in:88/MarketingAppServicesV2/webpages/CompleteVisit.aspx?Message=Success
//        http://udev.scci.co.in:88/MarketingAppServicesV2/webpages/DealerVisit.aspx?M=10109&D=378420&Auth_Key=E3B6978F-102B-4048-9D38-4CE694F3136A

        Utill.print("url === $url")

        var tempUrl = url;
        tempUrl = tempUrl!!.substring(tempUrl!!.indexOf("?"))
        val params = tempUrl!!.split("&")

        if(url!!.contains("CompleteVisit.aspx"))
        {
            if(params.size>0) {
                if(params[0].contains("Message=")) {
                    tempUrl = params[0].substring(tempUrl.indexOf("Message="))
                    urlAction.value = tempUrl.replace("Message=","")
                }
            }
        }
    }

}