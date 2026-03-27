package com.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;
import android.content.Context;
import android.content.SharedPreferences;

public class MainActivity extends Activity {
    private WebView webView;
    private String apiUrl = "http://127.0.0.1:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);

        String html = "<html><head><meta name='viewport' content='width=device-width, initial-scale=1'>" +
            "<style>" +
            "  body{background:#0f172a;color:white;font-family:sans-serif;margin:0;padding:15px;}" +
            "  .tabs{display:flex;gap:10px;margin-bottom:20px;}" +
            "  .tab{padding:10px;background:#1e293b;border-radius:10px;flex:1;text-align:center;font-weight:bold;}" +
            "  .active{background:#3b82f6;}" +
            "  .msg{background:#1e293b;padding:10px;border-radius:15px;margin-bottom:8px;border-left:4px solid #3b82f6;}" +
            "  .sender{font-size:10px;color:#60a5fa;display:block;margin-bottom:4px;}" +
            "  .card{background:#1e293b;padding:15px;border-radius:15px;margin-bottom:10px;display:flex;justify-content:space-between;}" +
            "</style></head><body>" +
            "<div class='tabs'><div class='tab active' onclick='show(\"stok\")'>STOK</div><div class='tab' onclick='show(\"chat\")'>CHAT</div></div>" +
            "<div id='content'>Memuat...</div>" +
            "<script>" +
            "  let mode = 'stok';" +
            "  function show(m){ mode = m; document.querySelectorAll('.tab').forEach(t=>t.classList.toggle('active', t.innerText.toLowerCase()==m)); load(); }" +
            "  function load() {" +
            "    fetch('" + apiUrl + "/' + (mode=='stok'?'produk':'chat'))" +
            "    .then(r=>r.json()).then(data=>{" +
            "      const el = document.getElementById('content');" +
            "      if(mode=='stok') el.innerHTML = data.map(i=>`<div class='card'><span>${i.nama}</span><b>Rp ${i.harga.toLocaleString()}</b></div>`).join('');" +
            "      else el.innerHTML = data.map(m=>`<div class='msg'><span class='sender'>${m.pengirim}</span>${m.isi}</div>`).join('');" +
            "    });" +
            "  }" +
            "  setInterval(load, 5000); load();" +
            "</script></body></html>";

        webView.setWebViewClient(new WebViewClient());
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        setContentView(webView);
    }
}
