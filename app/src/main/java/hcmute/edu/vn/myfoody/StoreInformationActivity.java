package hcmute.edu.vn.myfoody;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class StoreInformationActivity extends AppCompatActivity {

    ImageButton imageButtonBackStoreInformation;

    ImageView imageViewStore;

    RatingBar ratingBarStore;

    TextView textViewStoreName;
    TextView textViewStoreAddress;
    TextView textViewStoreOpenTime;
    TextView textViewStoreCloseTime;
    TextView textViewStoreCategory;

    LinearLayout containerStoreName;
    LinearLayout containerStoreAddress;
    LinearLayout containerStoreOpenTime;
    LinearLayout containerStoreCloseTime;
    LinearLayout containerStoreCategory;

    Integer StoreId;
    String StoreName;
    String StoreAddress;
    String StoreOpenTime;
    String StoreCloseTime;
    Integer StoreCategoryId;
    byte[] StoreCoverPhoto;
    Float StoreRateStar;
    String StoreCategoryName;

    int hour;
    int minute;

    final int REQUEST_CODE_GALLERY = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_information);

        StoreId = getIntent().getIntExtra("StoreId", 0);

        Cursor dataStore = MainActivity.database.GetData("SELECT * FROM Stores WHERE StoreId = " + StoreId);
        while (dataStore.moveToNext()){
            StoreName = dataStore.getString(1);
            StoreAddress = dataStore.getString(2);
            StoreOpenTime = dataStore.getString(3);
            StoreCloseTime = dataStore.getString(4);
            StoreCategoryId = dataStore.getInt(5);
            StoreCoverPhoto = dataStore.getBlob(6);
            StoreRateStar = dataStore.getFloat(7);
        }

        Cursor dataCategory = MainActivity.database.GetData("SELECT * FROM Categories WHERE CategoryId = " + StoreCategoryId);
        while (dataCategory.moveToNext()) {
            StoreCategoryName = dataCategory.getString(1);
        }

        imageButtonBackStoreInformation = (ImageButton) findViewById(R.id.imgBackStoreInformation);
        imageViewStore = (ImageView) findViewById(R.id.imageViewStore);
        ratingBarStore = (RatingBar) findViewById(R.id.ratingBarStore);
        textViewStoreName = (TextView) findViewById(R.id.textViewStoreName);
        textViewStoreAddress = (TextView) findViewById(R.id.textViewStoreAddress);
        textViewStoreOpenTime = (TextView) findViewById(R.id.textViewStoreOpenTime);
        textViewStoreCloseTime = (TextView) findViewById(R.id.textViewStoreCloseTime);
        textViewStoreCategory = (TextView) findViewById(R.id.textViewStoreCategory);
        containerStoreName = (LinearLayout) findViewById(R.id.containerStoreName);
        containerStoreAddress = (LinearLayout) findViewById(R.id.containerStoreAddress);
        containerStoreOpenTime = (LinearLayout) findViewById(R.id.containerStoreOpenTime);
        containerStoreCloseTime = (LinearLayout) findViewById(R.id.containerStoreCloseTime);
        containerStoreCategory = (LinearLayout) findViewById(R.id.containerStoreCategory);

        ratingBarStore.setRating(StoreRateStar);
        textViewStoreName.setText(StoreName);
        textViewStoreAddress.setText(StoreAddress);
        textViewStoreOpenTime.setText(StoreOpenTime);
        textViewStoreCloseTime.setText(StoreCloseTime);
        textViewStoreCategory.setText(StoreCategoryName);

        // Xử lý CoverPhoto
        if (StoreCoverPhoto != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(StoreCoverPhoto, 0, StoreCoverPhoto.length);
            imageViewStore.setImageBitmap(bitmap);
        }

        imageButtonBackStoreInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageViewStore.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ActivityCompat.requestPermissions(
                        StoreInformationActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
                return true;
            }
        });

        containerStoreName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialogUpdate(StoreInformationActivity.this, "Store Name", StoreId);
                return true;
            }
        });

        containerStoreAddress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialogUpdate(StoreInformationActivity.this, "Store Address", StoreId);
                return true;
            }
        });

        containerStoreOpenTime.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                SelectTime("Open Time");
                return true;
            }
        });

        containerStoreCloseTime.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                SelectTime("Close Time");
                return true;
            }
        });

        containerStoreCategory.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialogUpdateCategory(
                        StoreInformationActivity.this,
                        StoreId,
                        StoreCategoryName
                );
                return true;
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
                imageViewStore.setImageBitmap(bitmap);

                MainActivity.database.updateCoverPhotoStore(
                        MainActivity.database.imageViewToByte(imageViewStore),
                        StoreId
                );
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
                    textViewStoreOpenTime.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,minute));
                    MainActivity.database.updateOpenTimeStore(
                            textViewStoreOpenTime.getText().toString().trim(),
                            StoreId
                    );
                } else {
                    textViewStoreCloseTime.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,minute));
                    MainActivity.database.updateCloseTimeStore(
                            textViewStoreCloseTime.getText().toString().trim(),
                            StoreId
                    );
                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(StoreInformationActivity.this, TimePickerDialog.THEME_HOLO_DARK, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select " + type);
        timePickerDialog.show();
    }

    String editTextDialog;
    private void showDialogUpdate(Activity activity, String type, Integer storeId ){
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_store);
        dialog.setTitle("Update " + type);

        TextView textViewUpdateStore = (TextView) dialog.findViewById(R.id.textViewUpdateStore);
        EditText editTextStoreUpdate = (EditText) dialog.findViewById(R.id.editTextUpdateStore);
        Button buttonStoreUpdate = (Button) dialog.findViewById(R.id.buttonUpdateStore);

        Cursor dataStore = MainActivity.database.GetData("SELECT * FROM Stores WHERE StoreId = " + storeId);
        while (dataStore.moveToNext()){
            if(type.equals("Store Name")) {
                editTextDialog = dataStore.getString(1);
            } else {
                editTextDialog = dataStore.getString(2);
            }
        }
        textViewUpdateStore.setText("Update " + type);
        editTextStoreUpdate.setHint(type);
        editTextStoreUpdate.setText(editTextDialog);

        //set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.35);

        dialog.getWindow().setLayout(width, height);
        dialog.show();

        buttonStoreUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(type.equals("Store Name")) {
                        MainActivity.database.updateNameStore(
                                editTextStoreUpdate.getText().toString().trim(),
                                storeId
                        );
                        textViewStoreName.setText(editTextStoreUpdate.getText().toString().trim());
                    } else {
                        MainActivity.database.updateAddressStore(
                                editTextStoreUpdate.getText().toString().trim(),
                                storeId
                        );
                        textViewStoreAddress.setText(editTextStoreUpdate.getText().toString().trim());
                    }
                    dialog.dismiss();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
            }
        });
    }

    Integer CategoryId;
    private void showDialogUpdateCategory(Activity activity, Integer storeId, String storeCategoryName){
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_store_category);
        dialog.setTitle("Update Store's Category");

        Spinner spinnerCategories = (Spinner) dialog.findViewById(R.id.spinnerCategories);
        Button buttonUpdateStoreCategory = (Button) dialog.findViewById(R.id.buttonUpdateStoreCategory);

        ArrayList<String> arrayCategories = new ArrayList<String>();

        Cursor dataCategories = MainActivity.database.GetData("SELECT * FROM Categories");
        while (dataCategories.moveToNext()){
            String CategoryName = dataCategories.getString(1);
            arrayCategories.add(CategoryName);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayCategories);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(arrayAdapter);

        spinnerCategories.setSelection(arrayCategories.indexOf(storeCategoryName));

        //set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.37);

        dialog.getWindow().setLayout(width, height);
        dialog.show();

        buttonUpdateStoreCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Cursor dataCategory = MainActivity.database.GetData("SELECT * FROM Categories WHERE CategoryName = '" + spinnerCategories.getSelectedItem().toString().trim() + "'");
                    while (dataCategory.moveToNext()){
                        CategoryId = dataCategory.getInt(0);
                    }
                    MainActivity.database.updateCategoryStore(
                            CategoryId,
                            storeId
                    );
                    textViewStoreCategory.setText(spinnerCategories.getSelectedItem().toString().trim());
                    dialog.dismiss();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
            }
        });
    }
}