package com.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class MainActivity extends Activity {
    private WebView webView;
    private static final String PREFS = "OdfizCache";
    private static final String KEY = "produk_data";
    // Endpoint Server Rust kamu
    private String apiUrl = "http://127.0.0.1:8080/produk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);

        // Bridge: Menghubungkan JavaScript di WebView ke fungsi Java Android
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void saveCache(String json) {
                getSharedPreferences(PREFS, MODE_PRIVATE).edit().putString(KEY, json).apply();
            }
            @JavascriptInterface
            public void showToast(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        }, "AndroidBridge");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Skrip untuk ambil data dari Rust dan simpan ke Cache HP
                injectDataFetcher(view);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                // Jika server mati/offline, tampilkan UI dari Cache
                loadOfflineUI();
            }
        });

        setContentView(webView);
        loadOnlineUI();
    }

    private void loadOnlineUI() {
        // HTML Utama yang akan fetch data ke Rust Server
        String html = "<html><head><meta name='viewport' content='width=device-width, initial-scale=1'>" +
            "<style>" +
            "  body{background:#0f172a;color:white;font-family:sans-serif;padding:20px;}" +
            "  .card{background:#1e293b;padding:20px;border-radius:20px;margin-bottom:12px;border:1px solid #334155;}" +
            "  .item{display:flex;justify-content:space-between;padding:10px 0;border-bottom:1px solid #334155;}" +
            "  .price{color:#3b82f6;font-weight:bold;}" +
            "  h2{color:#60a5fa;margin-bottom:20px;}" +
            "</style></head><body>" +
            "<h2>Odfiz POS v2</h2>" +
            "<div id='app'>Memuat data dari Tokyo...</div>" +
            "<script>" +
            "  fetch('" + apiUrl + "')" +
            "  .then(r => r.json())" +
            "  .then(data => {" +
            "    AndroidBridge.saveCache(JSON.stringify(data));" +
            "    const el = document.getElementById('app');" +
            "    if(data.length > 0) {" +
            "      el.innerHTML = data.map(i => `<div class='card'><div class='item'><span>${i.nama}</span><span class='price'>Rp ${i.harga.toLocaleString()}</span></div></div>`).join('');" +
            "    } else { el.innerHTML = 'Belum ada produk.'; }" +
            "  }).catch(e => { console.log(e); });" +
            "</script></body></html>";

        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    private void injectDataFetcher(WebView view) {
        view.evaluateJavascript("console.log('Fetcher Active')", null);
    }

    private void loadOfflineUI() {
        String cachedData = getSharedPreferences(PREFS, MODE_PRIVATE).getString(KEY, "[]");

        String html = "<html><head><meta name='viewport' content='width=device-width, initial-scale=1'>" +
            "<style>" +
            "  body{background:#0f172a;color:white;font-family:sans-serif;padding:20px;}" +
            "  .card{background:rgba(239,68,68,0.1);padding:20px;border-radius:20px;border:1px solid #ef4444;}" +
            "  .item{display:flex;justify-content:space-between;padding:12px 0;border-bottom:1px solid #334155;}" +
            "  button{background:#3b82f6;color:white;border:none;padding:15px;border-radius:12px;width:100%;margin-top:20px;font-weight:bold;}" +
            "</style></head><body>" +
            "<h3 style='color:#ef4444'>⚠️ Mode Offline</h3>" +
            "<p style='font-size:12px;opacity:0.6'>Data terakhir dari Tokyo (AWS):</p>" +
            "<div id='list'></div>" +
            "<button onclick='location.reload()'>COBA HUBUNGKAN LAGI</button>" +
            "<script>" +
            "  const data = " + cachedData + ";" +
            "  const el = document.getElementById('list');" +
            "  if(data.length > 0) {" +
            "    el.innerHTML = data.map(i => `<div class='item'><span>${i.nama}</span><b>Rp ${i.harga.toLocaleString()}</b></div>`).join('');" +
            "  } else {" +
            "    el.innerHTML = 'Cache kosong.'; " +
            "  }" +
            "</script></body></html>";

        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }
}
