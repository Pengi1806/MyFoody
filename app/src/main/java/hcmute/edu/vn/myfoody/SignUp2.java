package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SignUp2 extends AppCompatActivity {

    TextView txtAHACLogin2;
    Button btnCreateAccount;
    ImageView imgBackSignUp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        txtAHACLogin2 = (TextView) findViewById(R.id.textAHACLogin2);
        imgBackSignUp2 = (ImageView) findViewById(R.id.imgBackSignUp2);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp2.this,MainActivity.class);
                startActivity(intent);
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