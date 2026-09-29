package com.example.lkcj3mb;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuizApiService {
    @GET("pytania")
    public Call<List<Pytania>> getPytania();
}
