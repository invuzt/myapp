package com.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.*;

public class MainActivity extends Activity {
    private WebView webView;
    private static final String PREFS = "OdfizCache";
    private static final String KEY = "produk_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);

        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void saveToAndroid(String json) {
                getSharedPreferences(PREFS, MODE_PRIVATE).edit().putString(KEY, json).apply();
            }
        }, "AndroidBridge");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                // Jika error saat muat halaman utama, lari ke Offline UI
                if (request.isForMainFrame()) {
                    runOnUiThread(() -> loadOfflineUI());
                }
            }
        });

        setContentView(webView);
        webView.loadUrl("http://127.0.0.1:8080");
    }

    private void loadOfflineUI() {
        String cachedData = getSharedPreferences(PREFS, MODE_PRIVATE).getString(KEY, "[]");
        
        // HTML Offline yang sudah include CSS agar tidak putih polos
        String html = "<html><head><meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "<style>body{background:#0f172a;color:white;font-family:sans-serif;padding:20px;}" +
            ".card{background:rgba(30,41,59,0.8);padding:20px;border-radius:24px;border:1px solid rgba(255,255,255,0.1);}" +
            ".item{display:flex;justify-content:space-between;padding:12px 0;border-bottom:1px solid #334155;}</style></head>" +
            "<body>" +
            "<h3 style='color:#ef4444'>⚠️ Server Offline</h3>" +
            "<div class='card' id='l'></div>" +
            "<button onclick='location.reload()' style='width:100%;margin-top:20px;padding:12px;border-radius:12px;background:#6366f1;color:white;border:none;font-weight:bold;'>RETRY</button>" +
            "<script>" +
            "  const d = " + cachedData + ";" +
            "  const el = document.getElementById('l');" +
            "  if(d.length>0) { el.innerHTML = d.map(i=>`<div class='item'><span>${i.nama}</span><b>Rp ${i.harga.toLocaleString()}</b></div>`).join(''); }" +
            "  else { el.innerHTML = 'Data Kosong. Mohon Online-kan Server dulu.'; }" +
            "</script></body></html>";

        // Gunakan loadDataWithBaseURL agar Origin-nya jelas dan tidak putih
        webView.loadDataWithBaseURL("http://127.0.0.1", html, "text/html", "UTF-8", null);
    }
}
