package hcmute.edu.vn.myfoody;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CartItemListAdapter extends BaseAdapter {

    Context context;
    Integer layout;
    ArrayList<CartItem> arrayListCartItem;

    public CartItemListAdapter(Context context, Integer layout, ArrayList<CartItem> arrayListCartItem) {
        this.context = context;
        this.layout = layout;
        this.arrayListCartItem = arrayListCartItem;
    }

    @Override
    public int getCount() {
        return arrayListCartItem.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListCartItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView textViewItemName;
        TextView textViewPriceItem;
        TextView textViewNumberItem;
        ImageButton imageButtonDecrease;
        ImageButton imageButtonIncrease;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        CartItemListAdapter.ViewHolder holder = new CartItemListAdapter.ViewHolder();
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.textViewItemName = (TextView) row.findViewById(R.id.textViewItemName);
            holder.textViewPriceItem = (TextView) row.findViewById(R.id.textViewPriceItem);
            holder.textViewNumberItem = (TextView) row.findViewById(R.id.textViewNumberItem);
            holder.imageButtonDecrease = (ImageButton) row.findViewById(R.id.imageButtonDecrease);
            holder.imageButtonIncrease = (ImageButton) row.findViewById(R.id.imageButtonIncrease);
            row.setTag(holder);
        } else {
            holder = (CartItemListAdapter.ViewHolder) row.getTag();
        }
        CartItem cartItem = arrayListCartItem.get(position);
        String FoodName = null;
        Float FoodPrice = null;
        Float ItemPrice;
        Cursor dataFood = MainActivity.database.GetData("SELECT * FROM Foods WHERE FoodId = " + cartItem.getFoodId());
        while (dataFood.moveToNext()){
            FoodName = dataFood.getString(1);
            FoodPrice = dataFood.getFloat(3);
        }
        ItemPrice = FoodPrice * cartItem.getQuantity();
        holder.textViewItemName.setText(FoodName);
        holder.textViewPriceItem.setText(FoodPrice + " x " + cartItem.getQuantity() + " = " + ItemPrice);
        holder.textViewNumberItem.setText(cartItem.getQuantity().toString());

        ViewHolder finalHolder = holder;
        Float finalFoodPrice = FoodPrice;
        holder.imageButtonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartItem.getQuantity() <= 1) {
                    MainActivity.database.deleteOneCartItem(
                            cartItem.getFoodId(),
                            cartItem.getEmail()
                    );
                    arrayListCartItem.remove(position);
                } else {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    MainActivity.database.updateCartItem(
                            cartItem.getQuantity(),
                            cartItem.getFoodId(),
                            cartItem.getEmail()
                    );
                    finalHolder.textViewPriceItem.setText(finalFoodPrice + " x " + cartItem.getQuantity() + " = " + (finalFoodPrice * cartItem.getQuantity()));
                    finalHolder.textViewNumberItem.setText(cartItem.getQuantity().toString());
                }
                notifyDataSetChanged();
            }
        });

        holder.imageButtonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                MainActivity.database.updateCartItem(
                        cartItem.getQuantity(),
                        cartItem.getFoodId(),
                        cartItem.getEmail()
                );
                finalHolder.textViewPriceItem.setText(finalFoodPrice + " x " + cartItem.getQuantity() + " = " + (finalFoodPrice * cartItem.getQuantity()));
                finalHolder.textViewNumberItem.setText(cartItem.getQuantity().toString());
                notifyDataSetChanged();
            }
        });
        return row;
    }
}
