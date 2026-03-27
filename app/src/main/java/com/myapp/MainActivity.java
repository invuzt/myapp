package com.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.*;

public class MainActivity extends Activity {
    private WebView webView;
    private static final String PREFS_NAME = "OdfizCache";
    private static final String CACHE_KEY = "produk_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);

        // Bridge antara JavaScript dan Java
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void saveToAndroid(String json) {
                SharedPreferences res = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                res.edit().putString(CACHE_KEY, json).apply();
            }
        }, "AndroidBridge");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) { loadOfflineUI(); }
            }
        });

        setContentView(webView);
        webView.loadUrl("http://127.0.0.1:8080");
    }

    private void loadOfflineUI() {
        SharedPreferences res = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String cachedData = res.getString(CACHE_KEY, "[]");

        String html = "<html><body style='background:#0f172a; color:white; font-family:sans-serif; text-align:center; padding:20px;'>" +
            "<div style='background:rgba(30,41,59,0.8); padding:25px; border-radius:30px; border:1px solid rgba(255,255,255,0.1);'>" +
            "<h3 style='color:#ef4444;'>⚠️ Server Offline</h3>" +
            "<div id='list' style='text-align:left; margin:20px 0;'></div>" +
            "<button onclick='location.reload()' style='background:#6366f1; color:white; border:none; padding:12px; width:100%; border-radius:15px; font-weight:bold;'>🔄 Hubungkan Kembali</button>" +
            "</div>" +
            "<script>" +
            "  const data = " + cachedData + ";" +
            "  const div = document.getElementById('list');" +
            "  if(data.length > 0) {" +
            "    div.innerHTML = data.map(i => `<div style='display:flex; justify-content:space-between; padding:8px 0; border-bottom:1px solid #334155;'><span>${i.nama}</span><b>Rp ${i.harga.toLocaleString()}</b></div>`).join('');" +
            "  } else { div.innerHTML = 'Data kosong. Online-kan server dulu.'; }" +
            "</script></body></html>";
        webView.loadData(html, "text/html", "UTF-8");
    }
}
