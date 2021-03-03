package com.example.testhelper;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class QuizActivity extends AppCompatActivity {

    //Global variables
    int finalScore = 0;
    int answerIndex = 0;
    String userChoice = "";
    //Array lists and hashmap
    ArrayList<String> definitionList = new ArrayList<String>();
    ArrayList<String> answerList = new ArrayList<String>();
    ArrayList<Integer> choiceList = new ArrayList<Integer>();
    HashMap<String, String> question = new HashMap<String, String>();
    //Declaring text views and buttons.
    TextView txtUser, txtDefinition, txtUserScore;
    Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, btnSubmit;
    ArrayList<Button> buttons = new ArrayList<Button>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        int count = 0;
        String def = "";

        txtUser = findViewById(R.id.txtUser);
        txtDefinition = findViewById(R.id.txtDefinition);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        btnAnswer4 = findViewById(R.id.btnAnswer4);
//Set all buttons to the same listener
        btnSubmit.setOnClickListener(onButtonClicked);
        btnAnswer1.setOnClickListener(onButtonClicked);
        btnAnswer2.setOnClickListener(onButtonClicked);
        btnAnswer3.setOnClickListener(onButtonClicked);
        btnAnswer4.setOnClickListener(onButtonClicked);
//Add answer buttons to the button array
        buttons.add(btnAnswer1);
        buttons.add(btnAnswer2);
        buttons.add(btnAnswer3);
        buttons.add(btnAnswer4);
//Get the user's name from the previous screen and display it
        Intent intent = getIntent();
        String user = intent.getStringExtra("name_key");
        txtUser.setText(user);
//Populate the choiceList
        for(int i=0; i<10; i++)
        {
            choiceList.add(i);
        }
//Input the quiz text file
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("QuizDefinitions.txt")));
            String answer = "";
            while (true)
            {
                String line = reader.readLine();
                if (line != null) {
//                    Loop through text file and split it up between answers and questions
                    for (String word : line.split(" ")) {
                        if (word.equals(":")) {
                            count = 1;
                        }
                        else if (count == 0)
                        {
                            answer += word;
                        }
                        else if (count == 1)
                        {
                            def += word + " ";
                        }
                    }//End for loop
//                  Assign answers and questions to the hashmap and their lists.
                    question.put(answer, def);
                    answerList.add(answer);
                    definitionList.add(def);
                    count = 0;
                    def = "";
                    answer = "";
                } else
                {
//                    Close the file
                    reader.close();
                    break;
                }
            }//End while loop

        } catch (IOException e) {
            Log.e("QuizActivity", "File Input Error", e);
        }
//      Shuffle the lists
        Collections.shuffle(answerList);
        Collections.shuffle(definitionList);
        Collections.shuffle(choiceList);

        displayNextQuestion();

    }//End on create

    public View.OnClickListener onButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAnswer1:
                    userChoice = (String) btnAnswer1.getText();
                    checkIfCorrect(userChoice);
                    break;
                case R.id.btnAnswer2:
                    userChoice = (String) btnAnswer2.getText();
                    checkIfCorrect(userChoice);
                    break;
                case R.id.btnAnswer3:
                    userChoice = (String) btnAnswer3.getText();
                    checkIfCorrect(userChoice);
                    break;
                case R.id.btnAnswer4:
                    userChoice = (String) btnAnswer4.getText();
                    checkIfCorrect(userChoice);
                    break;
                case R.id.btnSubmit:
                    endQuiz();
                    break;
            }
        }
    };//End onButtonClicked class

    //Method to display the next question
    public void displayNextQuestion()
    {
        int selected;
        int x = 0;
        //Choose a random button to assign the correct answer to.
        int correctButton = getRandomNumber();
        //Get the next correct answer to select which question will be displayed.
        String nextAnswer = answerList.get(answerIndex);

        //Loop through hashmap to get the question to display.
        for (String i : question.keySet())
        {
            if(i.equals(nextAnswer))
            {
                txtDefinition.setText(question.get(i));
            }
        }
        //Remove the question from the hashmap
        question.remove(nextAnswer);

        //Assign answers to buttons
        for (int i=1; i<=4; i++)
        {
            //Get another answer
             selected = choiceList.get(x);
             //Assign the correct answer to this button
            Button correct = buttons.get(i-1);
            if(i == correctButton)
            {
                correct.setText(nextAnswer);
            }
            //Assign false answers to the other buttons
            else {
                //If the selected answer equals the current answer, grab the next answer
                if (answerList.get(selected).equals(nextAnswer)) {
                    x++;
                    selected = choiceList.get(x);
                }
                correct.setText(answerList.get(selected));
            }
            x++;
        }//End of for loop
    }//End of Display Method

    //Check if user hit correct button
    public void checkIfCorrect(String choice)
    {
        //Check if user is correct and if yes, award point.
        if (answerList.get(answerIndex).equals(choice))
        {
            finalScore += 1;
            Toast toast= Toast.makeText(getApplicationContext(),
                    "Correct!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else
        {
            Toast toast= Toast.makeText(getApplicationContext(),
                    "Incorrect!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        answerIndex++;
        //Check if question hashmap is empty.
        if (question.isEmpty())
        {
            answerIndex = 0;
            endQuiz();
        }
        else
        {
            //Shuffle the choices and display the next question
            Collections.shuffle(choiceList);
            displayNextQuestion();
        }
    }//End checkIfCorrect method

    //Get a random button number.
    public  int getRandomNumber()
    {
        return (int) (Math.random() * (4 - 1 + 1) + 1);
    }//End getRandomNumber method

    //Method to end the quiz.
    public void endQuiz()
    {
        answerIndex = 0;
        //Send the final user's score to the final page.
        String score = String.valueOf(finalScore);

        Intent intent = new Intent(QuizActivity.this, FinalActivity.class);

        intent.putExtra("score_key", score);

        startActivity(intent);
    }//End endQuiz method
}//End Screen2 Class