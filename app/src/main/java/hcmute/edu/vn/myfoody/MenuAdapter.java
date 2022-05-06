package hcmute.edu.vn.myfoody;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {
    private Context context;
    private Integer layout;
    private ArrayList<Food> foodsList;
    private String email;

    public MenuAdapter(Context context, Integer layout, ArrayList<Food> foodsList, String email) {
        this.context = context;
        this.layout = layout;
        this.foodsList = foodsList;
        this.email = email;
    }

    @Override
    public int getCount() {
        return foodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtName, txtPrice;
        ImageButton imgAddtoCart;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        MenuAdapter.ViewHolder holder = new MenuAdapter.ViewHolder();
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtName = (TextView) row.findViewById(R.id.textViewMenuFoodName);
            holder.txtPrice = (TextView) row.findViewById(R.id.textViewMenuFoodPrice);
            holder.imageView = (ImageView) row.findViewById(R.id.imageViewMenuListView);
            holder.imgAddtoCart = (ImageButton) row.findViewById(R.id.imgAddtoCart);
            row.setTag(holder);
        } else {
            holder = (MenuAdapter.ViewHolder) row.getTag();
        }

        Food food = foodsList.get(position);
        holder.txtName.setText(food.getFoodName());
        holder.txtPrice.setText(food.getPrice().toString());

        byte[] foodImage = food.getImage();
        if(foodImage!=null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
            holder.imageView.setImageBitmap(bitmap);
        }

        holder.imgAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Integer foodIdAdded = null;
                    Cursor dataCartItem = MainActivity.database.GetData("SELECT FoodId FROM CartItems WHERE Email = '" + email + "'" );
                    if(dataCartItem.getCount() != 0){
                        while (dataCartItem.moveToNext()) {
                            foodIdAdded = dataCartItem.getInt(0);
                        }
                        Integer storeIdFoodAdded = null;
                        Cursor dataStoreFoodAdded = MainActivity.database.GetData("SELECT StoreId FROM Foods WHERE FoodId = " + foodIdAdded);
                        while (dataStoreFoodAdded.moveToNext()){
                            storeIdFoodAdded = dataStoreFoodAdded.getInt(0);
                        }
                        Integer storeIdFoodSelected = null;
                        Cursor dataStoreFoodSelected = MainActivity.database.GetData("SELECT StoreId FROM Foods WHERE FoodId = " + food.getFoodId());
                        while (dataStoreFoodSelected.moveToNext()){
                            storeIdFoodSelected = dataStoreFoodSelected.getInt(0);
                        }
                        if(storeIdFoodSelected != storeIdFoodAdded) {
                            MainActivity.database.deleteAllCartItem(email);
                        }
                    }
                    MainActivity.database.insertCartItem(
                            food.getFoodId(),
                            email
                    );
                    Toast.makeText(view.getContext(), food.getFoodName() + " đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), food.getFoodName() + " đã được thêm", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return row;
    }
}
