package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class MenuStoreHomeActivity extends AppCompatActivity {

    ImageButton imgBackMenuStoreHome;
    ListView listViewMenu;

    ArrayList<Food> foodArrayList;
    MenuAdapter adapter;

    Integer StoreId;
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_store_home);

        StoreId = getIntent().getIntExtra("StoreId", 0);
        Email = getIntent().getStringExtra("Email");

        imgBackMenuStoreHome = (ImageButton) findViewById(R.id.imgBackMenuStoreHome);
        listViewMenu = (ListView) findViewById(R.id.listViewMenu);
        foodArrayList = new ArrayList<Food>();
        adapter = new MenuAdapter(MenuStoreHomeActivity.this, R.layout.row_menu, foodArrayList);
        listViewMenu.setAdapter(adapter);

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

        imgBackMenuStoreHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}