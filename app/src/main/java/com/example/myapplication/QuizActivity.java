package com.example.myapplication;



import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;//
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.Serializable;
import java.util.List;//


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//import java.util.Timer;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizActivity extends AppCompatActivity {
    // Timer toast;
    private TextView tvQuestion, tvTimer, tvQuestionNumber;
    private Button btnOption1, btnOption2, btnOption3, btnOption4;
    private ProgressBar timerProgressBar;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean isTimerEnabled;
    private CountDownTimer countDownTimer;
    private final int TOTAL_QUESTIONS = 10;
    private final long TIMER_DURATION = 30000; // 30 seconds

    private String selectedAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize views
        tvQuestion = findViewById(R.id.tvQuestion);
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);
        timerProgressBar = findViewById(R.id.timerProgressBar);

        // Get timer preference from intent
        isTimerEnabled = getIntent().getBooleanExtra("isTimerEnabled", false);
        if (isTimerEnabled) {
            timerProgressBar.setVisibility(View.VISIBLE);
            tvTimer.setVisibility(View.VISIBLE);
        }

        // Fetch questions from API
        fetchQuestions();

        // Set button click listeners
        btnOption1.setOnClickListener(v -> checkAnswer(btnOption1.getText().toString()));
        btnOption2.setOnClickListener(v -> checkAnswer(btnOption2.getText().toString()));
        btnOption3.setOnClickListener(v -> checkAnswer(btnOption3.getText().toString()));
        btnOption4.setOnClickListener(v -> checkAnswer(btnOption4.getText().toString()));
    }

    private void fetchQuestions() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://opentdb.com/") // Base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuizApi quizApi = retrofit.create(QuizApi.class);
        Call<QuizResponse> call = quizApi.getQuestions();

        call.enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questionList = response.body().getResults();
                    if (questionList.size() < TOTAL_QUESTIONS) {
                        Toast.makeText(QuizActivity.this, "Not enough questions available.", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        displayQuestion(currentQuestionIndex);
                    }
                } else {
                    Toast.makeText(QuizActivity.this, "Failed to retrieve questions.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                Toast.makeText(QuizActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void displayQuestion(int index) {
        if (index >= TOTAL_QUESTIONS) {
            // Quiz finished
            endQuiz();
            return;
        }

        Question question = questionList.get(index);
        tvQuestionNumber.setText("Question " + (index + 1) + "/" + TOTAL_QUESTIONS);
        tvQuestion.setText(android.text.Html.fromHtml(question.getQuestion()).toString());

        List<String> options = new ArrayList<>(question.getIncorrectAnswers());
        options.add(question.getCorrectAnswer());
        Collections.shuffle(options);

        btnOption1.setText(android.text.Html.fromHtml(options.get(0)).toString());
        btnOption2.setText(android.text.Html.fromHtml(options.get(1)).toString());
        btnOption3.setText(android.text.Html.fromHtml(options.get(2)).toString());
        btnOption4.setText(android.text.Html.fromHtml(options.get(3)).toString());

        if (isTimerEnabled) {
            startTimer();
        }
    }

    private void startTimer() {
        timerProgressBar.setProgress(100);
        countDownTimer = new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                tvTimer.setText("Time Left: " + secondsLeft + "s");
                int progress = (int) ((millisUntilFinished / (float) TIMER_DURATION) * 100);
                timerProgressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                tvTimer.setText("Time's up!");
                // Automatically move to next question
                currentQuestionIndex++;
                displayQuestion(currentQuestionIndex);
            }
        }.start();
    }

    private void checkAnswer(String selectedAnswer) {
        // Cancel timer if enabled
        if (isTimerEnabled && countDownTimer != null) {
            countDownTimer.cancel();
        }

        Question question = questionList.get(currentQuestionIndex);
        String correctAnswer = android.text.Html.fromHtml(question.getCorrectAnswer()).toString();

        if (selectedAnswer.equals(correctAnswer)) {
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "Wrong! Correct Answer: " + correctAnswer, Toast.LENGTH_SHORT).show();
        }

        //  new Handler().postDelayed(() -> toast.cancel(), 500); // 1000 ms = 1 second
        currentQuestionIndex++;
        displayQuestion(currentQuestionIndex);
    }

    private void endQuiz() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", TOTAL_QUESTIONS);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}

