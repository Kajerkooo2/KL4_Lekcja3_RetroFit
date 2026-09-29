package com.example.lkcj3mb;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView tvTrescPytania;
    RadioGroup rgOdpowiedzi;
    RadioButton rbOdpA, rbOdpB, rbOdpC;

    Button btnSprawdz;

    List<Pytania> listaPytan;

    int aktualnyIndeks = 0;
    int licznikPunktow = 0;

    boolean czyPokazujeWynikPytania = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTrescPytania = findViewById(R.id.tvTrescPytania);
        rgOdpowiedzi = findViewById(R.id.rgOdpowiedzi);
        rbOdpA = findViewById(R.id.rbOdpA);
        rbOdpB = findViewById(R.id.rbOdpB);
        rbOdpC = findViewById(R.id.rbOdpC);
        btnSprawdz = findViewById(R.id.btnSprawdz);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/Kajerkooo2/KL4_Lekcja3_JSON_pytania_retrofit/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuizApiService quizApiService = retrofit.create(QuizApiService.class);
        Call<List<Pytania>> call = quizApiService.getPytania();
        call.enqueue(
                new Callback<List<Pytania>>() {
                    @Override
                    public void onResponse(Call<List<Pytania>> call, Response<List<Pytania>> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Błąd: " + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        listaPytan = response.body();

                        if (listaPytan != null && !listaPytan.isEmpty()) {
                            wyswietlPytanie(0);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Pytania>> call, Throwable t) {
                    }
                }
        );

        btnSprawdz.setOnClickListener(v -> sprawdzOdpowiedz());
    }

    private void wyswietlPytanie(int x) {
        if (x < 0 || x >= listaPytan.size()) {
            tvTrescPytania.setText("Koniec wynik to: " + licznikPunktow + " / " + listaPytan.size());
            rgOdpowiedzi.setVisibility(RadioGroup.GONE);
            btnSprawdz.setEnabled(false);
            return;
        }

        aktualnyIndeks = x;
        czyPokazujeWynikPytania = false;
        btnSprawdz.setText("Sprawdź");

        Pytania pytanie = listaPytan.get(x);

        tvTrescPytania.setText(pytanie.getTresc());

        rbOdpA.setTextColor(Color.BLACK);
        rbOdpB.setTextColor(Color.BLACK);
        rbOdpC.setTextColor(Color.BLACK);

        rbOdpA.setText(pytanie.getOdpA());
        rbOdpB.setText(pytanie.getOdpB());
        rbOdpC.setText(pytanie.getOdpC());

        rgOdpowiedzi.clearCheck();

        for (int i = 0; i < rgOdpowiedzi.getChildCount(); i++) {
            rgOdpowiedzi.getChildAt(i).setEnabled(true);
        }
    }

    private void sprawdzOdpowiedz() {
        if (listaPytan == null || aktualnyIndeks >= listaPytan.size()) {
            return;
        }

        if (czyPokazujeWynikPytania) {
            wyswietlPytanie(aktualnyIndeks + 1);
            return;
        }

        int wyborId = rgOdpowiedzi.getCheckedRadioButtonId();
        if (wyborId == -1) {
            Toast.makeText(this, "Wybierz odpowiedź", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton zaznaczonyPrzycisk = findViewById(wyborId);
        int wybranaOdpowiedz = rgOdpowiedzi.indexOfChild(zaznaczonyPrzycisk) + 1;

        Pytania pytanie = listaPytan.get(aktualnyIndeks);
        int poprawnaOdpowiedz = pytanie.getPoprawna();

        RadioButton poprawnyPrzycisk = (RadioButton) rgOdpowiedzi.getChildAt(poprawnaOdpowiedz - 1);

        if (wybranaOdpowiedz == poprawnaOdpowiedz) {
            licznikPunktow++;
            zaznaczonyPrzycisk.setTextColor(Color.GREEN);
        } else {
            zaznaczonyPrzycisk.setTextColor(Color.RED);
            if (poprawnyPrzycisk != null) {
                poprawnyPrzycisk.setTextColor(Color.GREEN);
            }
        }

        for (int i = 0; i < rgOdpowiedzi.getChildCount(); i++) {
            rgOdpowiedzi.getChildAt(i).setEnabled(false);
        }

        czyPokazujeWynikPytania = true;
        btnSprawdz.setText("Następne pytanie");
    }
}
