package com.example.lkcj3mb;

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
    RadioButton rbOdpA,rbOdpB,rbOdpC;

    Button btnSprawdz;

    List<Pytania> listaPytan;

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
                        if(!response.isSuccessful()){
                            Toast.makeText(MainActivity.this,response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        listaPytan = response.body();
                        tvTrescPytania.setText(listaPytan.get(0).getTresc());
                    }


                    @Override
                    public void onFailure(Call<List<Pytania>> call, Throwable t) {

                    }
                }
        );

    }
}
