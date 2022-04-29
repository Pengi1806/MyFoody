package hcmute.edu.vn.myfoody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toolbarChuQuan = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarChuQuan);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chu_quan);

        Email = getIntent().getStringExtra("Email");

        database = new Database(ChuQuanActivity.this, "foody.sqlite", null, 1);
        Cursor dataStore = database.GetData("SELECT * FROM Stores WHERE Email = '" + Email + "'");
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
        Cursor dataFood = database.GetData("SELECT * FROM Foods WHERE StoreId = " + StoreId);
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
                    Toast.makeText(ChuQuanActivity.this, "Thông tin quán ăn", Toast.LENGTH_SHORT).show();
                }
                else if(item.getItemId() == R.id.nav_Exit) {
                    finish();
                }
                return false;
            }
        });

        gridViewFood.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChuQuanActivity.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {

                        } else {

                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    private void showDialogUpdate(Activity activity) {

        Dialog dialog = new Dialog(activity);
//        dialog.setContentView();
    }
}