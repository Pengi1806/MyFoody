package hcmute.edu.vn.myfoody;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CartFragment extends Fragment {

    View view;
    TextView textViewOrderCart;
    TextView textViewResetCart;
    ImageView imgAvatarUserCart;
    TextView textViewFullNameCart;
    ListView listViewCart;

    ArrayList<CartItem> cartItemArrayList;
    CartItemListAdapter adapter;

    String Email;
    String Name;
    byte[] Avatar;
    Integer FoodId;
    Integer Quantity;
    Integer StoreId;

    Date currentTime;
    Float total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        Email = getArguments().getString("Email");

        textViewOrderCart = (TextView) view.findViewById(R.id.textViewOrderCart);
        textViewResetCart = (TextView) view.findViewById(R.id.textViewResetCart);
        imgAvatarUserCart = (ImageView) view.findViewById(R.id.imgAvatarUserCart);
        textViewFullNameCart = (TextView) view.findViewById(R.id.textViewFullNameCart);
        listViewCart = (ListView) view.findViewById(R.id.listViewCart);

        cartItemArrayList = new ArrayList<CartItem>();
        adapter = new CartItemListAdapter(getActivity(), R.layout.row_cartitems, cartItemArrayList);
        listViewCart.setAdapter(adapter);

        Cursor dataCartItem = MainActivity.database.GetData("SELECT * FROM CartItems WHERE Email = '" + Email + "'");
        cartItemArrayList.clear();
        while (dataCartItem.moveToNext()){
            FoodId = dataCartItem.getInt(0);
            Quantity = dataCartItem.getInt(2);
            cartItemArrayList.add(new CartItem(FoodId, Email, Quantity));
        }
        adapter.notifyDataSetChanged();

        Cursor dataFood = MainActivity.database.GetData("SELECT * FROM Foods WHERE FoodId = " + FoodId);
        while (dataFood.moveToNext()){
            StoreId = dataFood.getInt(4);
        }

        Cursor dataUser = MainActivity.database.GetData("SELECT * FROM Users WHERE Email = '" + Email + "'");
        while (dataUser.moveToNext()){
            Name = dataUser.getString(2);
            Avatar = dataUser.getBlob(6);
        }
        textViewFullNameCart.setText(Name);
        // Xử lý Avatar
        if (Avatar != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(Avatar, 0, Avatar.length);
            imgAvatarUserCart.setImageBitmap(bitmap);
        }

        textViewOrderCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Cursor dataOrder = MainActivity.database.GetData("SELECT * FROM Orders");
                    Integer orderId = dataOrder.getCount() + 1;
                    currentTime = Calendar.getInstance().getTime();
                    MainActivity.database.insertOrder(
                            orderId,
                            currentTime,
                            Email,
                            StoreId
                    );
                    total = Float.valueOf(0);
                    Cursor dataCartItemOrder = MainActivity.database.GetData("SELECT * FROM CartItems WHERE Email = '" + Email + "'");
                    while (dataCartItemOrder.moveToNext()){
                        Integer FoodIdOrder = dataCartItemOrder.getInt(0);
                        Integer Quantity = dataCartItemOrder.getInt(2);
                        Float FoodPrice = null;
                        Cursor dataFood = MainActivity.database.GetData("SELECT Price FROM Foods WHERE FoodId = " + FoodIdOrder);
                        while (dataFood.moveToNext()){
                            FoodPrice = dataFood.getFloat(0);
                        }
                        MainActivity.database.insertOrderDetail(
                                orderId,
                                FoodIdOrder,
                                Quantity,
                                FoodPrice
                        );
                        total = total + (FoodPrice * Quantity);
                    }
                    MainActivity.database.updateTotalOrder(
                            total,
                            orderId
                    );
                    MainActivity.database.deleteAllCartItem(Email);
                    reloadCartItemListView();
                    Cursor dataOrderAdded = MainActivity.database.GetData("SELECT * FROM Orders");
                    while (dataOrderAdded.moveToNext()){
                        Log.d("TAG", "onClick: " + dataOrderAdded.getInt(0));
                        Log.d("TAG", "onClick: " + dataOrderAdded.getString(3));
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Có gì đó không đúng!!!", Toast.LENGTH_SHORT);
                }
            }
        });

        textViewResetCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.database.deleteAllCartItem(Email);
                cartItemArrayList.clear();
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }
    private void reloadCartItemListView(){
        Cursor dataCartItem = MainActivity.database.GetData("SELECT * FROM CartItems WHERE Email = '" + Email + "'");
        cartItemArrayList.clear();
        while (dataCartItem.moveToNext()){
            FoodId = dataCartItem.getInt(0);
            Quantity = dataCartItem.getInt(2);
            cartItemArrayList.add(new CartItem(FoodId, Email, Quantity));
        }
        adapter.notifyDataSetChanged();
    }
}