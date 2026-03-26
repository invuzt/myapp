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

        btnSapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = inputNama.getText().toString().trim();
                if (nama.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Halo, " + nama + "!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
