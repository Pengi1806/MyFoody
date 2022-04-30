package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    ImageButton imgBackChangePassword;
    EditText editTextOldPasswordChangePassword;
    EditText editTextNewPasswordChangePassword;
    EditText editTextConfirmPasswordChangePassword;
    Button btnXacNhan;

    String Email;
    String OldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Email = getIntent().getStringExtra("Email");

        Cursor dataUser = MainActivity.database.GetData("SELECT * FROM Users WHERE Email = '" + Email + "'");
        while (dataUser.moveToNext()){
            OldPassword = dataUser.getString(1);
        }

        imgBackChangePassword = (ImageButton) findViewById(R.id.imgBackChangePassword);
        editTextOldPasswordChangePassword = (EditText) findViewById(R.id.editOldPasswordChangePassword);
        editTextNewPasswordChangePassword = (EditText) findViewById(R.id.editNewPasswordChangePassword);
        editTextConfirmPasswordChangePassword = (EditText) findViewById(R.id.editConfirmPasswordChangePassword);
        btnXacNhan = (Button) findViewById(R.id.buttonXacNhan) ;

        imgBackChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OldPasswordView = editTextOldPasswordChangePassword.getText().toString();
                String newPassword = editTextNewPasswordChangePassword.getText().toString();
                String confirmPassword = editTextConfirmPasswordChangePassword.getText().toString();
                if (OldPasswordView.equals("") == false && newPassword.equals("") == false && confirmPassword.equals("") == false) {
                    if (OldPasswordView.equals(OldPassword)) {
                        if(newPassword.length() >= 8) {
                            if (newPassword.equals(confirmPassword)) {
                                MainActivity.database.QueryData("UPDATE Users SET Password = '" + newPassword + "' WHERE Email = '" + Email + "'");

                                //Hiện thông báo
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                                builder.setMessage("Mật khẩu của bạn đã được thay đổi");
                                builder.setTitle("THÔNG BÁO");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        finish();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Cần nhập mật khẩu giống nhau!!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Mật khẩu tối thiểu 8 kí tự", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}