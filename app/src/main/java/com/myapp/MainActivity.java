package com.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;

public class MainActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true); // Kunci utama Offline Cache
        s.setDatabaseEnabled(true);
        
        // Memaksa WebView tetap menggunakan cache jika offline
        s.setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                // Jika koneksi ke localhost gagal, muat halaman emergency
                if (request.getUrl().toString().contains("127.0.0.1")) {
                    loadOfflineUI();
                }
            }
        });

        setContentView(webView);
        webView.loadUrl("http://127.0.0.1:8080");
    }

    private void loadOfflineUI() {
        // Tampilan minimalis tapi fungsional saat server mati
        String html = "<html><body style='background:#0f172a; color:white; font-family:sans-serif; display:flex; justify-content:center; align-items:center; height:100vh; margin:0; text-align:center;'>" +
            "<div style='padding:20px; border:1px solid #ef4444; border-radius:20px;'>" +
            "<h2 style='color:#ef4444;'>Odfiz Offline</h2>" +
            "<p id='status'>Mencoba mengambil data lokal...</p>" +
            "<div id='cache-data' style='margin-top:20px; text-align:left; background:rgba(255,255,255,0.05); border-radius:10px; padding:10px;'></div>" +
            "<p style='font-size:12px; margin-top:20px; color:#64748b;'>Nyalakan Server di Termux lalu buka kembali APK.</p>" +
            "</div>" +
            "<script>" +
            "  const cache = localStorage.getItem('odfiz_cache');" +
            "  if(cache) {" +
            "    const data = JSON.parse(cache);" +
            "    document.getElementById('status').innerText = 'Menampilkan Data Tersimpan:';" +
            "    document.getElementById('cache-data').innerHTML = data.map(i => `<div>${i.nama}: Rp ${i.harga.toLocaleString()}</div>`).join('');" +
            "  } else {" +
            "    document.getElementById('status').innerText = 'Tidak ada data tersimpan. Mohon onlinekan server sekali saja.';" +
            "  }" +
            "</script></body></html>";
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }
}
