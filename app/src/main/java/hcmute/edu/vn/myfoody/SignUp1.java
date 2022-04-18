package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class SignUp1 extends AppCompatActivity {

    TextView txtAHACLogin;
    Button btnNext;
    ImageView imgBackSignUp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        btnNext = (Button) findViewById(R.id.btnNext);
        txtAHACLogin = (TextView) findViewById(R.id.textAHACLogin);
        imgBackSignUp1 = (ImageView) findViewById(R.id.imgBackSignUp1);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp1.this,SignUp2.class);
                startActivity(intent);
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