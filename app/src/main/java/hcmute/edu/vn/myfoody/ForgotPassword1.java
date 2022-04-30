package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ForgotPassword1 extends AppCompatActivity {

    ImageButton imgBackForgotPassword1;
    EditText edtTextEmailForgotPassword;
    EditText edtTextSecurityAnswerForgotPassword;
    Button btnChangePassword;
    Spinner spinnerSecurityQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password1);

        imgBackForgotPassword1 = (ImageButton) findViewById(R.id.imgBackForgotPassword);
        edtTextEmailForgotPassword = (EditText) findViewById(R.id.editEmailForgotPassword);
        edtTextSecurityAnswerForgotPassword = (EditText) findViewById(R.id.editAnswerForgotPassword);
        btnChangePassword = (Button) findViewById(R.id.buttonChangePassword);
        spinnerSecurityQuestions = (Spinner) findViewById(R.id.spinnerSecurityQuestion);

        ArrayList<String> arraySecurityQuestions = new ArrayList<String>();
        arraySecurityQuestions.add("[Choose your security question]");

        //get data
        Cursor dataSecurityQuestions = MainActivity.database.GetData("SELECT * FROM SecurityQuestions");
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
                String Email = edtTextEmailForgotPassword.getText().toString().trim();
                String SecurityQuestion = spinnerSecurityQuestions.getSelectedItem().toString().trim();
                String SecurityAnswer = edtTextSecurityAnswerForgotPassword.getText().toString().trim();
                Integer UserQuestionIdSqlite = -1;
                Integer QuestionIdSqlite = -2 ;
                String UserAnswerSqlite = "";
                if(Email.equals("") == false && SecurityQuestion.equals("[Choose your security question]") == false && SecurityAnswer.equals("") == false) {
                    Cursor dataUsers = MainActivity.database.GetData("SELECT * FROM Users WHERE Email = '" + Email + "'");
                    if(dataUsers.getCount() == 0){
                        Toast.makeText(ForgotPassword1.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        Cursor dataSecurityQuestions = MainActivity.database.GetData("SELECT * FROM SecurityQuestions WHERE QuestionContent = '" + SecurityQuestion + "'");
                        while (dataSecurityQuestions.moveToNext()){
                            QuestionIdSqlite = dataSecurityQuestions.getInt(0);
                        }
                        while (dataUsers.moveToNext()){
                            UserQuestionIdSqlite = dataUsers.getInt(9);
                            UserAnswerSqlite = dataUsers.getString(8);
                        }
                        if (QuestionIdSqlite == UserQuestionIdSqlite) {
                            if(SecurityAnswer.equals(UserAnswerSqlite)) {
                                Intent intent = new Intent(ForgotPassword1.this, ForgotPassword2.class);
                                intent.putExtra("Email", Email);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ForgotPassword1.this, "Sai câu trả lời", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ForgotPassword1.this, "Sai câu hỏi bảo mật", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(ForgotPassword1.this, "Cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}