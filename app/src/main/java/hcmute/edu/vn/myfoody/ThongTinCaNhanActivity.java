package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.DataInput;

public class ThongTinCaNhanActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextGender;
    EditText editTextPhone;
    EditText editTextAddress;

    ImageButton imgBackThongTinCaNhan;
    ImageButton imgEdit;
    ImageButton imgConfirm;
    ImageButton imgCancel;

    CardView containerImgConfirm;
    CardView containerImgCancel;

    String Email;
    String Name;
    String Gender;
    String Phone;
    String Address;

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan);

        Email = getIntent().getStringExtra("Email");
        database = new Database(ThongTinCaNhanActivity.this, "foody.sqlite", null, 1);
        Cursor dataUser = database.GetData("SELECT * FROM Users WHERE Email = '" + Email + "'");
        while (dataUser.moveToNext()){
            Name = dataUser.getString(2);
            Gender = dataUser.getString(3);
            Phone = dataUser.getString(5);
            Address = dataUser.getString(4);
        }

        //Ánh xạ
        editTextName = (EditText) findViewById(R.id.editTextFullName) ;
        editTextGender = (EditText) findViewById(R.id.editTextGender);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        imgBackThongTinCaNhan = (ImageButton) findViewById(R.id.imgBackThongTinCaNhan);
        imgEdit = (ImageButton) findViewById(R.id.imgEdit);
        imgConfirm = (ImageButton) findViewById(R.id.imgConfirm);
        imgCancel = (ImageButton) findViewById(R.id.imgCancel);
        containerImgConfirm = (CardView) findViewById(R.id.containerImageConfirm);
        containerImgCancel = (CardView) findViewById(R.id.containerImageCancel);

        editTextName.setEnabled(false);
        editTextGender.setEnabled(false);
        editTextPhone.setEnabled(false);
        editTextAddress.setEnabled(false);

        editTextName.setText(Name);
        editTextGender.setText(Gender);
        editTextPhone.setText(Phone);
        editTextAddress.setText(Address);

        imgConfirm.setEnabled(false);
        imgCancel.setEnabled(false);

        containerImgConfirm.setVisibility(View.INVISIBLE);
        containerImgCancel.setVisibility(View.INVISIBLE);

        imgBackThongTinCaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextName.setEnabled(true);
                editTextGender.setEnabled(true);
                editTextPhone.setEnabled(true);
                editTextAddress.setEnabled(true);

                containerImgConfirm.setVisibility(View.VISIBLE);
                containerImgCancel.setVisibility(View.VISIBLE);

                imgConfirm.setEnabled(true);
                imgCancel.setEnabled(true);
            }
        });

        imgConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextName.setEnabled(false);
                editTextGender.setEnabled(false);
                editTextPhone.setEnabled(false);
                editTextAddress.setEnabled(false);

                imgConfirm.setEnabled(false);
                imgCancel.setEnabled(false);

                containerImgConfirm.setVisibility(View.INVISIBLE);
                containerImgCancel.setVisibility(View.INVISIBLE);

                Name = editTextName.getText().toString().trim();
                Gender = editTextGender.getText().toString().trim();
                Phone = editTextPhone.getText().toString().trim();
                Address = editTextAddress.getText().toString().trim();

                database.QueryData("UPDATE Users SET Name = '" + Name + "', Sex = '" + Gender + "', Address = '" + Address + "', Phone = '" + Phone + "' WHERE Email = '" + Email + "'");
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextName.setEnabled(false);
                editTextGender.setEnabled(false);
                editTextPhone.setEnabled(false);
                editTextAddress.setEnabled(false);

                imgConfirm.setEnabled(false);
                imgCancel.setEnabled(false);

                containerImgConfirm.setVisibility(View.INVISIBLE);
                containerImgCancel.setVisibility(View.INVISIBLE);

                editTextName.setText(Name);
                editTextGender.setText(Gender);
                editTextPhone.setText(Phone);
                editTextAddress.setText(Address);
            }
        });
    }
}