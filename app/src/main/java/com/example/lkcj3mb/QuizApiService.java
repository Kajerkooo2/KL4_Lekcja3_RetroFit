package com.example.lkcj3mb;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuizApiService {
    @GET("db.json")
    Call<OdpowiedziPytania> getQuiz();
}
