package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import androidx.annotation.Nullable;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnStartQuiz;
    private Switch switchTimer;

    private static final int FILE_REQUEST_CODE = 1;
    private TextView tvFileContent;
    private Button btnStartQuiz_TXT;
    private List<Question_TXT> questionList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnStartQuiz = findViewById(R.id.btnStartQuiz_INTE);
        switchTimer = findViewById(R.id.switchTimer);

        Button btnUpload = findViewById(R.id.btn_upload_TXT);
        tvFileContent = findViewById(R.id.tv_file_content_TXT);
        btnStartQuiz_TXT = findViewById(R.id.btn_start_quiz_TXT);

        btnUpload.setOnClickListener(v -> openFileChooser());
        btnStartQuiz_TXT.setOnClickListener(v -> startQuiz_TXT());

        btnStartQuiz.setOnClickListener(v -> {
            boolean isTimerEnabled = switchTimer.isChecked();
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("isTimerEnabled", isTimerEnabled);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    } //methods outside this braces are temporary
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        startActivityForResult(intent, FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            readFileContent(fileUri);
        }
    }

    private void readFileContent(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            tvFileContent.setText(content.toString());
            generateQuestions(content.toString());
            btnStartQuiz_TXT.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateQuestions(String content) {
        questionList = new ArrayList<>();
        String[] sentences = content.split("[.?!]");

        for (int i = 0; i < Math.min(10, sentences.length); i++) {
            String sentence = sentences[i].trim();
            if (sentence.isEmpty()) continue;

            // Generate a fill-in-the-blank question
            String[] words = sentence.split(" ");
            if (words.length > 3) {
                int randomIndex = (int) (Math.random() * words.length);
                String missingWord = words[randomIndex];
                words[randomIndex] = "______";

                String questionText = String.join(" ", words) + "?";
                List<String> choices = generateChoices(missingWord, content);
                questionList.add(new Question_TXT(questionText, choices, choices.indexOf(missingWord)));
            }
        }

        // If less than 10 questions are generated, repeat some to ensure 10 questions.
        while (questionList.size() < 10 && questionList.size() > 0) {
            questionList.add(questionList.get(questionList.size() - 1));
        }
    }

    private List<String> generateChoices(String correctAnswer, String content) {
        List<String> choices = new ArrayList<>();
        choices.add(correctAnswer);

        // Generate three random incorrect choices from the text content
        for (int i = 0; i < 3; i++) {
            String randomChoice = generateRandomWordFromText(content, choices);
            while (choices.contains(randomChoice)) {
                randomChoice = generateRandomWordFromText(content, choices);
            }
            choices.add(randomChoice);
        }

        // Shuffle the choices so the correct answer isn't always first
        Collections.shuffle(choices);
        return choices;
    }

    private String generateRandomWordFromText(String text, List<String> excludeWords) {
        // Tokenize the input text into words
        String[] words = text.split("\\s+");
        List<String> wordList = new ArrayList<>();

        // Filter out words that are too short or already in the excludeWords list
        for (String word : words) {
            word = word.replaceAll("[^a-zA-Z]", ""); // Remove punctuation
            if (word.length() > 2 && !excludeWords.contains(word)) {
                wordList.add(word);
            }
        }

        // If we have some words, pick one at random
        if (!wordList.isEmpty()) {
            Random random = new Random();
            return wordList.get(random.nextInt(wordList.size()));
        }

        // Fallback to generating a random word if no suitable words are found
        return generateRandomWord(5); // Default length of 5 if fallback needed
    }

    private String generateRandomWord(int length) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * alphabet.length());
            sb.append(alphabet.charAt(randomIndex));
        }
        return sb.toString();
    }

    private void startQuiz_TXT() {
        Intent intent = new Intent(this, QuizActivity_TXT.class);
        intent.putParcelableArrayListExtra("questionList", (ArrayList<Question_TXT>) questionList);
        startActivity(intent);
    }
}