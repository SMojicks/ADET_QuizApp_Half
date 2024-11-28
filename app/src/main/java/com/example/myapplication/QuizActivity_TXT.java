package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class QuizActivity_TXT extends AppCompatActivity {
    private List<Question_TXT> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private TextView tvQuestion;
    private Button btnChoice1, btnChoice2, btnChoice3, btnChoice4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_txt);

        tvQuestion = findViewById(R.id.tv_question);
        btnChoice1 = findViewById(R.id.btn_choice_1);
        btnChoice2 = findViewById(R.id.btn_choice_2);
        btnChoice3 = findViewById(R.id.btn_choice_3);
        btnChoice4 = findViewById(R.id.btn_choice_4);

        questionList = getIntent().getParcelableArrayListExtra("questionList");
        showQuestion();

        View.OnClickListener choiceClickListener = view -> {
            Button selectedButton = (Button) view;
            checkAnswer(selectedButton.getText().toString());
            currentQuestionIndex++;
            if (currentQuestionIndex < questionList.size()) {
                showQuestion();
            } else {
                showScore();
            }
        };

        btnChoice1.setOnClickListener(choiceClickListener);
        btnChoice2.setOnClickListener(choiceClickListener);
        btnChoice3.setOnClickListener(choiceClickListener);
        btnChoice4.setOnClickListener(choiceClickListener);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void showQuestion() {
        Question_TXT currentQuestion = questionList.get(currentQuestionIndex);
        tvQuestion.setText(currentQuestion.getQuestion());

        List<String> choices = currentQuestion.getChoices();
        btnChoice1.setText(choices.get(0));
        btnChoice2.setText(choices.get(1));
        btnChoice3.setText(choices.get(2));
        btnChoice4.setText(choices.get(3));
    }

    private void checkAnswer(String selectedAnswer) {
        String correctAnswer = questionList.get(currentQuestionIndex).getChoices()
                .get(questionList.get(currentQuestionIndex).getCorrectAnswerIndex());
        if (selectedAnswer.equals(correctAnswer)) {
            score++;
        }
    }

    private void showScore() {
        Intent intent = new Intent(this, ResultActivity_TXT.class);
        intent.putExtra("score", score);
        intent.putExtra("totalQuestions", questionList.size());
        startActivity(intent);
        finish();
    }
}