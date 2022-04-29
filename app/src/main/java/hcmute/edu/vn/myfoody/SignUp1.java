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

public class SignUp1 extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword, edtConfirmPassword, edtPhone;

    TextView txtAHACLogin;
    Button btnNext;
    ImageView imgBackSignUp1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        edtName = (EditText) findViewById(R.id.editName);
        edtEmail = (EditText) findViewById(R.id.editEmail);
        edtPassword = (EditText) findViewById(R.id.editPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
        edtPhone = (EditText) findViewById(R.id.editPhone);

        btnNext = (Button) findViewById(R.id.btnNext);
        txtAHACLogin = (TextView) findViewById(R.id.textAHACLogin);
        imgBackSignUp1 = (ImageView) findViewById(R.id.imgBackSignUp1);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = edtName.getText().toString().trim();
                String Email = edtEmail.getText().toString().trim();
                String Password = edtPassword.getText().toString();
                String Phone = edtPhone.getText().toString().trim();
                String ConfirmPassword = edtConfirmPassword.getText().toString();
                if(Name.equals("") == false && Email.equals("") == false && Password.equals("") == false
                        && ConfirmPassword.equals("") == false && Phone.equals("") == false) {
                    Cursor dataUsers = MainActivity.database.GetData("SELECT * FROM Users WHERE Email = '" + Email + "'");
                    if(dataUsers.getCount() == 0) {
                        if(Password.length()>=8) {
                            if (Password.equals(ConfirmPassword)){
                                Intent intent = new Intent(SignUp1.this, SignUp2.class);
                                intent.putExtra("Name", Name);
                                intent.putExtra("Email", Email);
                                intent.putExtra("Password", Password);
                                intent.putExtra("Phone", Phone);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(SignUp1.this, "Mật khẩu xác nhận không trùng khớp", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUp1.this, "Mật khẩu tối thiểu 8 kí tự", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(SignUp1.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUp1.this, "Cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtAHACLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp1.this,MainActivity.class);
                startActivity(intent);
            }
        });

        imgBackSignUp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*
        //drop down Security Question
        Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{"1", "2", "three"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

         */
    }
}