package hcmute.edu.vn.myfoody;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class DangKyStoreActivity extends AppCompatActivity {

    ImageButton imgBackDangKyStore;
    ImageView imageViewDangKyStore;
    EditText editTextStoreNameDangKyStore;
    EditText editTextStoreAddressDangKyStore;
    TextView textViewSelectOpenTimeDangKyStore;
    TextView textViewOpenTimeDangKyStore;
    TextView textViewSelectCloseTimeDangKyStore;
    TextView textViewCloseTimeDangKyStore;
    Spinner spinnerCategoriesDangKyStore;
    Button buttonDangKyStore;

    String Email;
    Integer CategoryId;
    Boolean Flag;

    int hour;
    int minute;

    final int REQUEST_CODE_GALLERY = 555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky_store);

        Email = getIntent().getStringExtra("Email");
        Flag = false;

        imgBackDangKyStore = (ImageButton) findViewById(R.id.imgBackDangKyStore);
        imageViewDangKyStore = (ImageView) findViewById(R.id.imageViewDangKyStore);
        editTextStoreNameDangKyStore = (EditText) findViewById(R.id.editTextStoreNameDangKyStore);
        editTextStoreAddressDangKyStore = (EditText) findViewById(R.id.editTextStoreAddressDangKyStore);
        textViewSelectOpenTimeDangKyStore = (TextView) findViewById(R.id.textViewSelectOpenTimeDangKyStore);
        textViewOpenTimeDangKyStore = (TextView) findViewById(R.id.textViewOpenTimeDangKyStore);
        textViewSelectCloseTimeDangKyStore = (TextView) findViewById(R.id.textViewSelectCloseTimeDangKyStore);
        textViewCloseTimeDangKyStore = (TextView) findViewById(R.id.textViewCloseTimeDangKyStore);
        spinnerCategoriesDangKyStore = (Spinner) findViewById(R.id.spinnerCategoriesDangKyStore);
        buttonDangKyStore = (Button) findViewById(R.id.buttonDangKyStore);

        ArrayList<String> arrayCategories = new ArrayList<String>();
        arrayCategories.add("[Choose store's category]");

        //get data
        Cursor dataCategories = MainActivity.database.GetData("SELECT * FROM Categories");
        while (dataCategories.moveToNext()){
            String CategoryName = dataCategories.getString(1);
            arrayCategories.add(CategoryName);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayCategories);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerCategoriesDangKyStore.setAdapter(arrayAdapter);

        imgBackDangKyStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageViewDangKyStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        DangKyStoreActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        textViewSelectOpenTimeDangKyStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectTime("Open Time");
            }
        });

        textViewSelectCloseTimeDangKyStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectTime("Close Time");
            }
        });

        buttonDangKyStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String storeName = editTextStoreNameDangKyStore.getText().toString().trim();
                String storeAddress = editTextStoreAddressDangKyStore.getText().toString().trim();
                String openTime = textViewOpenTimeDangKyStore.getText().toString().trim();
                String closeTime = textViewCloseTimeDangKyStore.getText().toString().trim();
                String CategoryName = spinnerCategoriesDangKyStore.getSelectedItem().toString().trim();
                if(storeName.equals("") == false && storeAddress.equals("") == false && openTime.equals("") == false && closeTime.equals("") == false && Flag == true && CategoryName.equals("[Choose store's category]") == false) {
                    if(closeTime.compareTo(openTime) >= 0) {
                        try {
                            Cursor dataCategory = MainActivity.database.GetData("SELECT * FROM Categories WHERE CategoryName = '" + spinnerCategoriesDangKyStore.getSelectedItem().toString().trim() + "'");
                            while (dataCategory.moveToNext()){
                                CategoryId = dataCategory.getInt(0);
                            }
                            MainActivity.database.insertStore(
                                    storeName,
                                    storeAddress,
                                    openTime,
                                    closeTime,
                                    CategoryId,
                                    MainActivity.database.imageViewToByte(imageViewDangKyStore),
                                    Email
                            );
                            MainActivity.database.QueryData("UPDATE Users SET Role = 1 WHERE Email = '" + Email + "'");
                            Flag = false;
                            Intent intent = new Intent(DangKyStoreActivity.this, ChuQuanActivity.class);
                            intent.putExtra("Email", Email);
                            startActivity(intent);
                            finish();
                        } catch (Exception error) {
                            Log.e("Update error", error.getMessage());
                        }
                    } else {
                        Toast.makeText(DangKyStoreActivity.this, "Thời điểm mở cửa phải sớm hơn đóng cửa!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DangKyStoreActivity.this, "Cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
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
                imageViewDangKyStore.setImageBitmap(bitmap);
                Flag = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void SelectTime(String type){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                if(type.equals("Open Time")) {
                    textViewOpenTimeDangKyStore.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,minute));
                } else {
                    textViewCloseTimeDangKyStore.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,minute));
                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(DangKyStoreActivity.this, TimePickerDialog.THEME_HOLO_DARK, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select " + type);
        timePickerDialog.show();
    }
}