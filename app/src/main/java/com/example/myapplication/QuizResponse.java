package com.example.myapplication;
import java.util.List;
public class QuizResponse{
    private int response_code;
    private List<Question> results;

    // Getters
    public int getResponseCode() { return response_code; }
    public List<Question> getResults() { return results; }
}

