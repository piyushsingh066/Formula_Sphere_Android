package com.android.formulasphere

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Remove transitions to prevent flicker
        @Suppress("DEPRECATION")
        overridePendingTransition(0, 0)

        webView = WebView(this)
        setupWebView()
        setContentView(webView)

        webView.loadUrl("https://piyushsingh066.github.io/Formula_Sphere/")

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.setBackgroundColor("#080c14".toColorInt())
        
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true
        
        // Performance settings
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false
                
                // Keep local navigation in WebView, open external links in browser
                return if (url.startsWith("file:///android_asset/") || url.contains("blob:")) {
                    false
                } else {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        startActivity(intent)
                        true
                    } catch (_: Exception) {
                        false
                    }
                }
            }
        }
    }
}