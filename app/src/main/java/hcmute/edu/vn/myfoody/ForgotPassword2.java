package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ForgotPassword2 extends AppCompatActivity {

    ImageView imgBackForgotPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgot_password2);

        imgBackForgotPassword2 = (ImageView) findViewById(R.id.imgBackForgotPassword2);

        imgBackForgotPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}