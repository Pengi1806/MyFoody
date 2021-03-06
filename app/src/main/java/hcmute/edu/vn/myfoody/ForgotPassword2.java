package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ForgotPassword2 extends AppCompatActivity {

    ImageView imgBackForgotPassword2;
    EditText editTextNewPasswordForgotPassword;
    EditText editTextConfirmPasswordForgotPassword;
    Button btnConfirmForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);

        imgBackForgotPassword2 = (ImageView) findViewById(R.id.imgBackForgotPassword2);
        editTextNewPasswordForgotPassword = (EditText) findViewById(R.id.editNewPasswordForgotPassword);
        editTextConfirmPasswordForgotPassword = (EditText) findViewById(R.id.editConfirmPasswordForgotPassword);
        btnConfirmForgotPassword = (Button) findViewById(R.id.buttonConfirm);

        imgBackForgotPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnConfirmForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = getIntent().getStringExtra("Email");
                String newPassword = editTextNewPasswordForgotPassword.getText().toString();
                String confirmPassword = editTextConfirmPasswordForgotPassword.getText().toString();
                if (newPassword.equals("") == false && confirmPassword.equals("") == false) {
                    if(newPassword.length() >= 8) {
                        if (newPassword.equals(confirmPassword)) {
                            MainActivity.database.QueryData("UPDATE Users SET Password = '" + newPassword + "' WHERE Email = '" + Email + "'");

                            //Hi???n th??ng b??o
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword2.this);
                            builder.setMessage("M???t kh???u c???a b???n ???? ???????c thay ?????i");
                            builder.setTitle("TH??NG B??O");

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(ForgotPassword2.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    Intent intent = new Intent(ForgotPassword2.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Toast.makeText(ForgotPassword2.this, "C???n nh???p m???t kh???u gi???ng nhau!!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ForgotPassword2.this, "M???t kh???u t???i thi???u 8 k?? t???", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPassword2.this, "C???n nh???p ????? th??ng tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}