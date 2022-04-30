package hcmute.edu.vn.myfoody;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ThongTinCaNhanActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextGender;
    EditText editTextPhone;
    EditText editTextAddress;

    ImageButton imgBackThongTinCaNhan;
    ImageButton imgEdit;
    ImageButton imgConfirm;
    ImageButton imgCancel;
    ImageButton imgPhoto;

    ImageView imgAvatar;

    CardView containerImgConfirm;
    CardView containerImgCancel;

    String Email;
    String Name;
    String Gender;
    String Phone;
    String Address;
    byte[] Avatar;

    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan);

        Email = getIntent().getStringExtra("Email");

        Cursor dataUser = MainActivity.database.GetData("SELECT * FROM Users WHERE Email = '" + Email + "'");
        while (dataUser.moveToNext()){
            Name = dataUser.getString(2);
            Gender = dataUser.getString(3);
            Phone = dataUser.getString(5);
            Address = dataUser.getString(4);
            Avatar = dataUser.getBlob(6);
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
        imgPhoto = (ImageButton) findViewById(R.id.imgPhoto);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
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

        // Xử lý Avatar
        if (Avatar != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(Avatar, 0, Avatar.length);
            imgAvatar.setImageBitmap(bitmap);
        }

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

                MainActivity.database.QueryData("UPDATE Users SET Name = '" + Name + "', Sex = '" + Gender + "', Address = '" + Address + "', Phone = '" + Phone + "' WHERE Email = '" + Email + "'");
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

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        ThongTinCaNhanActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgAvatar.setImageBitmap(bitmap);

                MainActivity.database.updatePhotoUser(
                        MainActivity.database.imageViewToByte(imgAvatar),
                        Email
                );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}