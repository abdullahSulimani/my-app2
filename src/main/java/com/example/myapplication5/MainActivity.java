package com.example.myapplication5;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private ArrayList<String> patientAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startConversationButton = findViewById(R.id.startConversationButton);

        startConversationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConversation();
            }
        });

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        // Initialize patientAnswers ArrayList
        patientAnswers = new ArrayList<>();
    }

    private void startConversation() {
        // Ask Questions
        askQuestion("لديك كحة");
    }

    private void askQuestion(String question) {
        // Speak the question
        textToSpeech.speak(question, TextToSpeech.QUEUE_FLUSH, null, null);

        // Show AlertDialog to receive user input
        new AlertDialog.Builder(this)
                .setTitle(question)
                .setPositiveButton("نعم", (dialog, which) -> processAnswer("نعم"))
                .setNegativeButton("لا", (dialog, which) -> processAnswer("لا"))
                .setCancelable(false)
                .show();
    }

    private void processAnswer(String answer) {
        // Store the patient's answer
        patientAnswers.add(answer);

        // Continue asking more questions or perform other actions based on the answers

        // For demonstration purposes, let's ask another question
        if (patientAnswers.size() < 5) {
            String nextQuestion = getNextQuestion(patientAnswers.size());
            askQuestion(nextQuestion);
        } else {
            // Finish the conversation or perform other actions
            displayResults();
        }
    }

    private String getNextQuestion(int questionNumber) {
        // Define the next set of questions based on the questionNumber
        String[] questions = {
                "هل لديك سخونة",
                "هل لديك تعب",
                "هل لديك صعوبة في التنفس",
                "هل لديك كوليسترول"
        };

        if (questionNumber < questions.length) {
            return questions[questionNumber];
        } else {
            return "شكراً للإجابة على جميع الأسئلة.";
        }
    }

    private void displayResults() {
        // Display the stored answers or perform other actions based on the answers
        StringBuilder results = new StringBuilder("إجابات المريض:\n");
        for (int i = 0; i < patientAnswers.size(); i++) {
            results.append("سؤال ").append(i + 1).append(": ").append(patientAnswers.get(i)).append("\n");
        }

        // For demonstration purposes, show the answers in a dialog
        new AlertDialog.Builder(this)
                .setTitle("نتائج المحادثة")
                .setMessage(results.toString())
                .setPositiveButton("موافق", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        // Shutdown TextToSpeech when the activity is destroyed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
