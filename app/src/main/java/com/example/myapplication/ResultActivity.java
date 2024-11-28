package com.example.myapplication;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView tvFinalScore;
    private Button btnPlayAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvFinalScore = findViewById(R.id.tvFinalScore);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        int total = intent.getIntExtra("total", 10);

        tvFinalScore.setText("Your Score: " + score + "/" + total);

        btnPlayAgain.setOnClickListener(v -> {
            Intent playAgainIntent = new Intent(ResultActivity.this, MainActivity.class);
            playAgainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(playAgainIntent);
            finish();
        });
    }
}
