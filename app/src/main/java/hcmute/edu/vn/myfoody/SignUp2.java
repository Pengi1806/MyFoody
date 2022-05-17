package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SignUp2 extends AppCompatActivity {

    TextView txtAHACLogin2;
    Button btnCreateAccount;
    ImageView imgBackSignUp2;
    Spinner spinnerSecurityQuestions;
    int QuestionID;
    TextView textViewSecurityQuestion;
    EditText editSecurityAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        Intent intent = getIntent();
        String Name = intent.getStringExtra("Name");
        String Email = intent.getStringExtra("Email");
        String Password = intent.getStringExtra("Password");
        String Phone = intent.getStringExtra("Phone");

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        txtAHACLogin2 = (TextView) findViewById(R.id.textAHACLogin2);
        imgBackSignUp2 = (ImageView) findViewById(R.id.imgBackSignUp2);
        spinnerSecurityQuestions = (Spinner) findViewById(R.id.spinnerSignUpSecurityQuestions);
        textViewSecurityQuestion = (TextView) findViewById(R.id.TextSignUpSecurityQuestion);
        editSecurityAnswer = (EditText) findViewById(R.id.editSecurityAnswer);

        ArrayList<String> arraySecurityQuestions = new ArrayList<String>();
        arraySecurityQuestions.add("[Choose your security question]");
        //get data
        Cursor dataSecurityQuestions = MainActivity.database.GetData("SELECT * FROM SecurityQuestions");
        while (dataSecurityQuestions.moveToNext()){
            String QuestionContent = dataSecurityQuestions.getString(1);
            QuestionID = dataSecurityQuestions.getInt(0);
            arraySecurityQuestions.add(QuestionContent);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arraySecurityQuestions);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerSecurityQuestions.setAdapter(arrayAdapter);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String SecurityQuestion = spinnerSecurityQuestions.getSelectedItem().toString().trim();
                String SecurityAnswer = editSecurityAnswer.getText().toString().trim();
                Integer QuestionIdSqlite = -2 ;
                if (SecurityQuestion.equals("[Choose your security question]") == false) {
                    if(SecurityAnswer.equals("") == false) {
                        Cursor dataSecurityQuestions = MainActivity.database.GetData("SELECT * FROM SecurityQuestions WHERE QuestionContent = '" + SecurityQuestion + "'");
                        while (dataSecurityQuestions.moveToNext()){
                            QuestionIdSqlite = dataSecurityQuestions.getInt(0);
                        }
                        MainActivity.database.INSERT_USER(
                                Email,
                                Password,
                                Name,
                                Phone,
                                SecurityAnswer,
                                QuestionIdSqlite
                        );
                        Toast.makeText(SignUp2.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUp2.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(SignUp2.this, "Cần nhập câu trả lời", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignUp2.this, "Cần chọn câu hỏi bảo mật", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtAHACLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp2.this,MainActivity.class);
                startActivity(intent);
            }
        });

        imgBackSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}