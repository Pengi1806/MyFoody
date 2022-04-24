package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ForgotPassword1 extends AppCompatActivity {

    ImageButton imgBackForgotPassword1;
    Button btnChangePassword;
    Spinner spinnerSecurityQuestions;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password1);

        imgBackForgotPassword1 = (ImageButton) findViewById(R.id.imgBackForgotPassword);
        btnChangePassword = (Button) findViewById(R.id.buttonChangePassword);
        spinnerSecurityQuestions = (Spinner) findViewById(R.id.spinnerSecurityQuestion);

        ArrayList<String> arraySecurityQuestions = new ArrayList<String>();
        arraySecurityQuestions.add("[Choose your security question]");
        database = new Database(ForgotPassword1.this, "foody.sqlite", null, 1);
        //get data
        Cursor dataSecurityQuestions = database.GetData("SELECT * FROM SecurityQuestions");
        while (dataSecurityQuestions.moveToNext()){
            String QuestionContent = dataSecurityQuestions.getString(1);
            arraySecurityQuestions.add(QuestionContent);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arraySecurityQuestions);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerSecurityQuestions.setAdapter(arrayAdapter);

        imgBackForgotPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassword1.this, ForgotPassword2.class);
                startActivity(intent);
            }
        });
    }
}