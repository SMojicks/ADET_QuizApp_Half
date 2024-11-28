package com.example.myapplication;
import retrofit2.Call;
import retrofit2.http.GET;
public interface QuizApi {
    @GET("api.php?amount=10&category=9&type=multiple")
    Call<QuizResponse> getQuestions();
}
