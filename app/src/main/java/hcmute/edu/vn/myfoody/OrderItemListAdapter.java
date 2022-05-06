package hcmute.edu.vn.myfoody;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class OrderItemListAdapter extends BaseAdapter {

    Context context;
    Integer layout;
    ArrayList<OrderItem> arrayListOrderItem;

    public OrderItemListAdapter(Context context, Integer layout, ArrayList<OrderItem> arrayListOrderItem) {
        this.context = context;
        this.layout = layout;
        this.arrayListOrderItem = arrayListOrderItem;
    }

    @Override
    public int getCount() {
        return arrayListOrderItem.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListOrderItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView textViewOrderEmail;
        TextView textViewOrderCreateTime;
        TextView textViewOrderTotal;
        TextView textViewOrderQuantity;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        OrderItemListAdapter.ViewHolder holder = new OrderItemListAdapter.ViewHolder();
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.textViewOrderEmail = (TextView) row.findViewById(R.id.textViewOrderEmail);
            holder.textViewOrderCreateTime = (TextView) row.findViewById(R.id.textViewOrderCreateTime);
            holder.textViewOrderTotal = (TextView) row.findViewById(R.id.textViewOrderTotal);
            holder.textViewOrderQuantity = (TextView) row.findViewById(R.id.textViewOrderQuantity);
            row.setTag(holder);
        } else {
            holder = (OrderItemListAdapter.ViewHolder) row.getTag();
        }
        OrderItem orderItem = arrayListOrderItem.get(position);
        String CreatedTime = orderItem.getCreateTime();
        String[] separated = CreatedTime.split(" ");
        CreatedTime = separated[3] + " " + separated[2] + ", " + separated[1] + ", " + separated[5];
        holder.textViewOrderCreateTime.setText(CreatedTime);
        holder.textViewOrderEmail.setText(orderItem.getEmail());
        holder.textViewOrderTotal.setText(orderItem.getTotal().toString());

        Integer Quantity = null;

        Cursor dataOrderDetails = MainActivity.database.GetData("SELECT SUM(Quantity) FROM OrderDetails WHERE OrderId = " + orderItem.getOrderId());
        while (dataOrderDetails.moveToNext()){
            Quantity = dataOrderDetails.getInt(0);
        }
        holder.textViewOrderQuantity.setText(Quantity.toString());

        return row;
    }
}
