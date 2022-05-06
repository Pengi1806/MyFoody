package hcmute.edu.vn.myfoody;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Blob;
import java.util.ArrayList;

public class OrderDetailListAdapter extends BaseAdapter {
    private Context context;
    private Integer layout;
    private ArrayList<OrderDetail> orderDetailsList;

    private String FoodName;
    private Float Price;
    private byte[] FoodImg;

    public OrderDetailListAdapter(Context context, Integer layout, ArrayList<OrderDetail> orderDetailsList) {
        this.context = context;
        this.layout = layout;
        this.orderDetailsList = orderDetailsList;
    }

    @Override
    public int getCount() {
        return orderDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtName, txtPrice, txtQuantity;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        OrderDetailListAdapter.ViewHolder holder = new OrderDetailListAdapter.ViewHolder();
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtName = (TextView) row.findViewById(R.id.textViewOrderDetailName);
            holder.txtPrice = (TextView) row.findViewById(R.id.textViewOrderDetailPrice);
            holder.imageView = (ImageView) row.findViewById(R.id.imageViewOrderDetailListView);
            holder.txtQuantity = (TextView) row.findViewById(R.id.textViewOrderDetailQuantity);
            row.setTag(holder);
        } else {
            holder = (OrderDetailListAdapter.ViewHolder) row.getTag();
        }
        OrderDetail orderDetail = orderDetailsList.get(position);
        Cursor dataFood = MainActivity.database.GetData("SELECT * FROM Foods WHERE FoodId = " + orderDetail.getFoodId());
        while (dataFood.moveToNext()){
            FoodName = dataFood.getString(1);
            FoodImg = dataFood.getBlob(2);
        }
        holder.txtName.setText(FoodName);
        holder.txtPrice.setText(orderDetail.getUnitPrice().toString());
        holder.txtQuantity.setText(orderDetail.getQuantity().toString());
        if(FoodImg!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(FoodImg, 0, FoodImg.length);
            holder.imageView.setImageBitmap(bitmap);
        }
        return row;
    }
}
