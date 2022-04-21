package com.onlyonepass.mylibrary

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity


@SuppressLint("SetJavaScriptEnabled")
class WebviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val intent: Intent = intent
        var apikey = intent.getStringExtra("apikey")
        var amount = intent.getStringExtra("amount")
        var memo = intent.getStringExtra("memo")
        var gateway = intent.getStringExtra("gateway")
        var webpay = findViewById<WebView>(R.id.webpay)
        var Prog = findViewById<ProgressBar>(R.id.progressBar)
        var progWrper = findViewById<RelativeLayout>(R.id.progWrper)
//        Prog.max = 1000;
//        val curremProg = 500
//        ObjectAnimator.ofInt(Prog,"progress",curremProg).setDuration(2000).start()

        webpay.settings.loadsImagesAutomatically = true
        webpay.settings.javaScriptEnabled = true
        webpay.settings.setSupportZoom(true)
        var urlString = "http://api.marshalsoft.pro/v1.0/plugin/"
        var str = "{\"amount\":\"$amount\","
        str = "$str\"gateway\":\"$gateway\","
        str = "$str\"apikey\":\"$apikey\","
        str = "$str\"memo\":\"$memo\"}"
        urlString = urlString + str

        webpay.loadUrl(urlString)
        webpay.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progWrper.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progWrper.visibility = View.INVISIBLE
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {

                super.onReceivedError(view, request, error)
            }
        }
    }

}