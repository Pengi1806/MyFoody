package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {

    ListView listViewOrderDetail;
    ImageButton imgBackOrderDetail;

    ArrayList<OrderDetail> orderDetailArrayList;
    OrderDetailListAdapter adapter;

    Integer OrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        OrderId = getIntent().getIntExtra("OrderId", 0);

        imgBackOrderDetail = (ImageButton) findViewById(R.id.imgBackOrderDetail);
        listViewOrderDetail = (ListView) findViewById(R.id.listViewOrderDetail);
        orderDetailArrayList = new ArrayList<OrderDetail>();
        adapter = new OrderDetailListAdapter(OrderDetailActivity.this, R.layout.row_order_detail, orderDetailArrayList);
        listViewOrderDetail.setAdapter(adapter);

        Cursor dataOrderDetail = MainActivity.database.GetData("SELECT * FROM OrderDetails WHERE OrderId = " + OrderId);
        orderDetailArrayList.clear();
        while (dataOrderDetail.moveToNext()){
            Integer OrderId = dataOrderDetail.getInt(0);
            Integer FoodId = dataOrderDetail.getInt(1);
            Integer Quantity = dataOrderDetail.getInt(2);
            Float UnitPrice = dataOrderDetail.getFloat(3);

            orderDetailArrayList.add(new OrderDetail(OrderId, FoodId, Quantity, UnitPrice));
        }
        adapter.notifyDataSetChanged();

        imgBackOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}