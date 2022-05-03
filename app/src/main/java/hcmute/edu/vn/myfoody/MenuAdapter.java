package hcmute.edu.vn.myfoody;

import android.content.Context;
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

    public MenuAdapter(Context context, Integer layout, ArrayList<Food> foodsList) {
        this.context = context;
        this.layout = layout;
        this.foodsList = foodsList;
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
                Toast.makeText(view.getContext(), food.getFoodName() + " đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });

        return row;
    }
}
