package com.example.testhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {
//Global variables
    Button btnRetry;
    TextView txtFinalMessage;
    String message = "Congrats, you have completed the quiz. \n Your final score is: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        btnRetry = findViewById(R.id.btnRetry);
        txtFinalMessage = findViewById(R.id.txtFinalMessage);

        btnRetry.setOnClickListener(onButtonClicked);

        Intent intent = getIntent();
//      Get the user's score and display it in the final message.
        String userScore = intent.getStringExtra("score_key");
        message += userScore + "/10";
        txtFinalMessage.setText(message);


    }
    public View.OnClickListener onButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Go back to the main activity.
            Intent intent = new Intent(FinalActivity.this, MainActivity.class);

            startActivity(intent);
        }
    };
}