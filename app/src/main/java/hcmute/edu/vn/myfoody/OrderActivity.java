package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    ListView listViewOrder;
    ImageButton imgBackOrder;

    ArrayList<OrderItem> orderItemArrayList;
    OrderItemListAdapter adapter;

    Integer StoreId1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        StoreId1 = getIntent().getIntExtra("StoreId", 0);

        imgBackOrder = (ImageButton) findViewById(R.id.imgBackOrder);
        listViewOrder = (ListView)  findViewById(R.id.listViewOrder);
        orderItemArrayList = new ArrayList<OrderItem>();
        adapter = new OrderItemListAdapter(OrderActivity.this, R.layout.row_order, orderItemArrayList);
        listViewOrder.setAdapter(adapter);

        imgBackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Cursor dataOrder = MainActivity.database.GetData("SELECT * FROM Orders WHERE StoreId = " + StoreId1);
        orderItemArrayList.clear();
        while (dataOrder.moveToNext()){
            Integer OrderId = dataOrder.getInt(0);
            String CreateTime = dataOrder.getString(1);
            Float Total = dataOrder.getFloat(2);
            String Email = dataOrder.getString(3);
            Integer StoreId = dataOrder.getInt(4);

            orderItemArrayList.add(new OrderItem(OrderId, StoreId, CreateTime, Email, Total));
        }
        adapter.notifyDataSetChanged();

        listViewOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(OrderActivity.this, OrderDetailActivity.class);
                intent.putExtra("OrderId", orderItemArrayList.get(position).getOrderId());
                startActivity(intent);
            }
        });
    }
}