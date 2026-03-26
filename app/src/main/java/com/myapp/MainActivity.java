package com.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText inputNama = findViewById(R.id.inputNama);
        Button btnSapa = findViewById(R.id.btnSapa);

        btnSapa.setOnClickListener(v -> {
            // Animasi membal (Fluid Scale)
            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction(() -> {
                v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                
                String nama = inputNama.getText().toString().trim();
                if (nama.isEmpty()) {
                    Toast.makeText(this, "Tulis namamu dulu ya", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Halo, " + nama + "!", Toast.LENGTH_SHORT).show();
                }
            }).start();
        });
    }
}
