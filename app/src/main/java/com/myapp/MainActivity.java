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
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            // Menangkap error koneksi (Server Mati)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    loadOfflineUI();
                }
            }
        });

        setContentView(webView);
        // Coba muat server
        webView.loadUrl("http://127.0.0.1:8080");
    }

    private void loadOfflineUI() {
        // HTML ini disimpan di dalam APK, jadi PASTI muncul walau offline
        String html = "<html><head><meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "<style>" +
            "body { background:#0f172a; color:white; font-family:sans-serif; display:flex; justify-content:center; align-items:center; height:100vh; margin:0; text-align:center; }" +
            ".box { padding:30px; border:1px solid rgba(255,255,255,0.1); border-radius:32px; background:rgba(30,41,59,0.7); backdrop-filter:blur(10px); width:80%; }" +
            ".btn { background:#6366f1; color:white; padding:12px; border-radius:16px; border:none; width:100%; font-weight:bold; margin-top:20px; }" +
            ".wa-btn { background:#22c55e; color:white; text-decoration:none; display:block; padding:12px; border-radius:16px; margin-top:10px; font-weight:bold; font-size:14px; }" +
            ".item { display:flex; justify-content:space-between; padding:10px 0; border-bottom:1px solid rgba(255,255,255,0.05); font-size:14px; }" +
            "</style></head><body>" +
            "<div class='box'>" +
            "<h3 style='color:#ef4444; margin:0;'>⚠️ Server Offline</h3>" +
            "<p style='color:#94a3b8; font-size:13px;'>Menampilkan data terakhir tersimpan:</p>" +
            "<div id='list' style='text-align:left; margin-top:15px;'></div>" +
            "<button class='btn' onclick='location.reload()'>🔄 Coba Hubungkan Lagi</button>" +
            "<a href='https://wa.me/628123456789' class='wa-btn'>💬 Hubungi Admin</a>" +
            "<p style='font-size:11px; color:#64748b; margin-top:15px;'>Pastikan Termux sudah Cargo Run</p>" +
            "</div>" +
            "<script>" +
            "  const data = JSON.parse(localStorage.getItem('odfiz_cache') || '[]');" +
            "  const container = document.getElementById('list');" +
            "  if(data.length > 0) {" +
            "    container.innerHTML = data.map(i => `<div class='item'><span>${i.nama}</span><span style='color:#4ade80;'>Rp ${i.harga.toLocaleString()}</span></div>`).join('');" +
            "  } else {" +
            "    container.innerHTML = '<p style='text-align:center; color:#64748b;'>Belum ada cache data.</p>';" +
            "  }" +
            "</script></body></html>";
        
        webView.loadDataWithBaseURL("http://127.0.0.1:8080", html, "text/html", "UTF-8", null);
    }
}
