package hcmute.edu.vn.myfoody;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ChuQuanActivity extends AppCompatActivity {

    ImageButton imgButtonAddFood;
    Toolbar toolbarChuQuan;
    TextView textViewStoreName;

    GridView gridViewFood;
    ArrayList<Food> foodArrayList;
    FoodListAdapter adapter;

    String Email;
    int StoreId;
    String StoreName;

    final int REQUEST_CODE_GALLERY = 888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toolbarChuQuan = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarChuQuan);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chu_quan);

        Email = getIntent().getStringExtra("Email");

        Cursor dataStore = MainActivity.database.GetData("SELECT * FROM Stores WHERE Email = '" + Email + "'");
        while (dataStore.moveToNext()){
            StoreId = dataStore.getInt(0);
            StoreName = dataStore.getString(1);
        }

        imgButtonAddFood = (ImageButton) findViewById(R.id.imgAddFood);
        textViewStoreName = (TextView) findViewById(R.id.textViewStoreName);
        toolbarChuQuan = (Toolbar) findViewById(R.id.toolbar);

        textViewStoreName.setText(StoreName);

        gridViewFood = (GridView) findViewById(R.id.gridViewFood);
        foodArrayList = new ArrayList<Food>();
        adapter = new FoodListAdapter(ChuQuanActivity.this, R.layout.food_items, foodArrayList);
        gridViewFood.setAdapter(adapter);


        Cursor dataFood = MainActivity.database.GetData("SELECT * FROM Foods WHERE StoreId = " + StoreId);
        foodArrayList.clear();
        while (dataFood.moveToNext()){
            Integer FoodId = dataFood.getInt(0);
            String FoodName = dataFood.getString(1);
            byte[] Photo = dataFood.getBlob(2);
            Float Price = dataFood.getFloat(3);

            foodArrayList.add(new Food(FoodId, FoodName, Price, Photo));
        }
        adapter.notifyDataSetChanged();

        imgButtonAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChuQuanActivity.this, AddFoodActivity.class);
                intent.putExtra("StoreId", StoreId);
                startActivity(intent);
            }
        });

        toolbarChuQuan.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.nav_ThongTinQuanAn){
                    Intent intent = new Intent(ChuQuanActivity.this, StoreInformationActivity.class);
                    intent.putExtra("StoreId", StoreId);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.nav_Exit) {
                    finish();
                }
                return false;
            }
        });

        gridViewFood.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChuQuanActivity.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            //Update Food
                            Cursor dataFood = MainActivity.database.GetData("SELECT FoodId FROM Foods WHERE StoreId = " + StoreId);
                            ArrayList<Integer> arrId = new ArrayList<Integer>();
                            while (dataFood.moveToNext()) {
                                arrId.add(dataFood.getInt(0));
                            }
                            showDialogUpdate(ChuQuanActivity.this, arrId.get(position));
                        } else {
                            //Delete Food
                            Cursor dataFood = MainActivity.database.GetData("SELECT FoodId FROM Foods WHERE StoreId = " + StoreId);
                            ArrayList<Integer> arrId = new ArrayList<Integer>();
                            while (dataFood.moveToNext()) {
                                arrId.add(dataFood.getInt(0));
                            }
                            showDialogDelete(arrId.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    ImageView imageViewFood;
    String FoodNameDialog;
    Float FoodPriceDialog;
    byte[] PhotoDialog;

    private void showDialogDelete(Integer foodId) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(ChuQuanActivity.this);

        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Bạn muốn xóa món ăn này khỏi thực đơn?");

        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    MainActivity.database.deleteFood(foodId);
                    dialogInterface.dismiss();
                    Toast.makeText(ChuQuanActivity.this, "Delete successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Delete error", error.getMessage());
                }
                updateFoodList();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialogDelete.show();
    }

    private void showDialogUpdate(Activity activity, Integer foodId) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_food_activity);
        dialog.setTitle("Update");

        imageViewFood = (ImageView) dialog.findViewById(R.id.imageViewFoodUpdate);
        EditText editTextName = (EditText) dialog.findViewById(R.id.editTextFoodNameUpdate);
        EditText editTextPrice = (EditText) dialog.findViewById(R.id.editTextPriceUpdate);
        Button buttonUpdate = (Button) dialog.findViewById(R.id.buttonUpdateFood);

        Cursor dataFood = MainActivity.database.GetData("SELECT * FROM Foods WHERE FoodId = " + foodId);
        while (dataFood.moveToNext()){
            FoodNameDialog = dataFood.getString(1);
            PhotoDialog = dataFood.getBlob(2);
            FoodPriceDialog = dataFood.getFloat(3);
        }

        editTextName.setText(FoodNameDialog);
        editTextPrice.setText(FoodPriceDialog.toString());
        // Xử lý Photo
        if (PhotoDialog != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(PhotoDialog, 0, PhotoDialog.length);
            imageViewFood.setImageBitmap(bitmap);
        }

        //set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageViewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        ChuQuanActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.database.updateFood(
                            editTextName.getText().toString().trim(),
                            MainActivity.database.imageViewToByte(imageViewFood),
                            Float.valueOf(editTextPrice.getText().toString().trim()),
                            foodId
                    );
                    dialog.dismiss();
                    Toast.makeText(ChuQuanActivity.this, "Update successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                updateFoodList();
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
                imageViewFood.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateFoodList(){
        Cursor dataFood = MainActivity.database.GetData("SELECT * FROM Foods WHERE StoreId = " + StoreId);
        foodArrayList.clear();
        while (dataFood.moveToNext()){
            Integer FoodId = dataFood.getInt(0);
            String FoodName = dataFood.getString(1);
            byte[] Photo = dataFood.getBlob(2);
            Float Price = dataFood.getFloat(3);

            foodArrayList.add(new Food(FoodId, FoodName, Price, Photo));
        }
        adapter.notifyDataSetChanged();
    }

    private void updateAfterChangeInformation(){
        Cursor dataStore = MainActivity.database.GetData("SELECT * FROM Stores WHERE Email = '" + Email + "'");
        while (dataStore.moveToNext()){
            StoreName = dataStore.getString(1);
        }
        textViewStoreName.setText(StoreName);
    }

    @Override
    protected void onResume() {
        updateFoodList();
        updateAfterChangeInformation();
        super.onResume();
    }
}