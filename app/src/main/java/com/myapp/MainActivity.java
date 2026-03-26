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
        
        // Buat WebView di memori
        WebView webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        
        // Aktifkan JS agar Axum/Frontend modern bisa jalan
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        // Supaya tidak dilempar ke Chrome/Browser luar
        webView.setWebViewClient(new WebViewClient());

        setContentView(webView);
        
        // Tembak ke server Rust di folder sebelah
        webView.loadUrl("http://127.0.0.1:8080");
    }
}
