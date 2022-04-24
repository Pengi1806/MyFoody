package hcmute.edu.vn.myfoody;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ThemQuanAnActivity extends AppCompatActivity {

    Button btnThem, btnHuy;
    EditText edtStoreName, edtAddress, edtOpenTime, edtCloseTime, edtCategories;
    ImageButton imbtFolder;
    ImageView imgCoverPhoto;
    int REQUEST_CODE_FOLDER = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_quan_an);

        AnhXa();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chuyen data imgview -> byte[]
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgCoverPhoto.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                byte[] hinhAnh = byteArray.toByteArray();


                HomeFragment.database.INSERT_QUANAN(
                        edtStoreName.getText().toString().trim(),
                        edtAddress.getText().toString().trim(),
                        edtOpenTime.getText().toString().trim(),
                        edtCloseTime.getText().toString().trim(),
                        edtCategories.getText().toString().trim(),
                        hinhAnh
                );
                Toast.makeText(ThemQuanAnActivity.this, "Đã thêm", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ThemQuanAnActivity.this, HomeActivity.class));
            }
        });

        imbtFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgCoverPhoto.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void AnhXa(){
        btnThem = (Button) findViewById(R.id.buttonThem);
        btnHuy = (Button) findViewById(R.id.buttonHuy);
        edtStoreName = (EditText) findViewById(R.id.editTextStoreName);
        edtAddress = (EditText) findViewById(R.id.editTextAddress);
        edtOpenTime = (EditText) findViewById(R.id.editTextOpenTime);
        edtCloseTime = (EditText) findViewById(R.id.editTextCloseTime);
        edtCategories = (EditText) findViewById(R.id.editTextCategories);
        imgCoverPhoto = (ImageView) findViewById(R.id.imageViewCoverPhoto);
        imbtFolder = (ImageButton) findViewById(R.id.imageButtonFolder);
    }
}