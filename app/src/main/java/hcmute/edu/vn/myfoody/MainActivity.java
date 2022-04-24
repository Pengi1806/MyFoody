package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtForgotPassword;
    Button btnLogin;
    TextView txtSignUp;
    //
    Button btnThemQuanAn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtForgotPassword = (TextView) findViewById(R.id.forgotPassword);
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        txtSignUp = (TextView) findViewById(R.id.textSignUp);
        //
        btnThemQuanAn = (Button) findViewById(R.id.buttonThemQuanAn);


        //
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ForgotPassword1.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp1.class);
                startActivity(intent);
            }
        });

        //
        btnThemQuanAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , ThemQuanAnActivity.class);
                startActivity(intent);
            }
        });
    }
}