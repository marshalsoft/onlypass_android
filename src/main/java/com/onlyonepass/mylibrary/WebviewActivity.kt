package com.onlyonepass.mylibrary

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@SuppressLint("SetJavaScriptEnabled")
class WebviewActivity : AppCompatActivity() {
    lateinit var webpay:WebView;
    val BaseUrl:String = "http://api.marshalsoft.pro/v1.0/"
    lateinit var progWrper:RelativeLayout
    private var port: WebMessagePort? = null
    private var apikey:String? = null
    private var amount:String? = null
    private var memo:String? = null
    private var currency:String? = null
    private var mobile:String? = null
    private var email:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val intent: Intent = intent
        apikey = intent.getStringExtra("apikey")
        amount = intent.getStringExtra("amount")
        memo = intent.getStringExtra("memo")
        currency = intent.getStringExtra("currency")
        mobile = intent.getStringExtra("mobile")!!
        email = intent.getStringExtra("email")!!
        webpay = findViewById<WebView>(R.id.webpay)
        webpay.addJavascriptInterface(AndroidJSInterface,"PayMentCalls")
        progWrper = findViewById<RelativeLayout>(R.id.progWrper)
        webpay.settings.loadsImagesAutomatically = true
        webpay.settings.javaScriptEnabled = true
        webpay.settings.setSupportZoom(true)

        webpay.loadUrl("file:///android_asset/index.html")
//        webpay?.loadData(loadHtml(), "text/html", "UTF-8")
        webpay.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                view?.loadData(loadHtml(), "text/html", "UTF-8")
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progWrper.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                checkClient()
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                checkError()
                super.onReceivedError(view, request, error)
            }

        }
    }
    fun checkError()
    {

    }
    fun sendMessage(btn:String)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            //calls when version code greater than or equal to KITKAT
            webpay.evaluateJavascript(btn,null)
        }
        else
        {
            webpay.loadUrl("javascript:"+btn)
        }
    }

    private fun checkClient() {
        val retroifitBuilder = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiInterface::class.java)
        var getData = retroifitBuilder.GetUser()
        getData.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                val resp = response.body()!!
                resp.memo = memo
                resp.amount = amount
                resp.currency = currency
                resp.mobile = mobile
                resp.email = email
                // build buttons
                var senddata = ""
                val json = Gson()
                var sty = json.toJson(resp)
                senddata = "showBtn('"+ sty+"')"
//              Toast.makeText(applicationContext,senddata,Toast.LENGTH_LONG).show()
                sendMessage(senddata)
                progWrper.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable){
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
                progWrper.visibility = View.INVISIBLE
            }
        })
    }

    object AndroidJSInterface {
        @JavascriptInterface
        fun pay() {
            Log.d("click", "Help button clicked")
        }
    }

 fun loadHtml():String
 {
      var html:String = "<script src='https://js.paystack.co/v1/inline.js'></script>\n" +
              "<button onclick=\"PayMentCalls.pay()\" >Welcome</button>"
     return html
 }
}