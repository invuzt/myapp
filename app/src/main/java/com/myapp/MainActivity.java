package com.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true); // Wajib untuk localStorage
        webView.setWebViewClient(new WebViewClient());
        setContentView(webView);
        webView.loadUrl("http://127.0.0.1:8080");
    }
}
