package com.example.testhelper;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnStart;
    TextView txtInputName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        txtInputName = findViewById(R.id.txtInputName);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String user = txtInputName.getText().toString();
//              Check to make sure the user entered a name.
                if (user.length() > 0)
                {
//                    Start the next activity and send the user's name
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);

                    intent.putExtra("name_key", user);

                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please enter a name",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}