package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class StoreHomeActivity extends AppCompatActivity {

    Toolbar toolbarStoreHome;

    ImageButton imgBackStoreHome;
    TextView textViewStoreHome;
    ImageView imageViewStoreHome;
    TextView textViewStoreNameHome;
    RatingBar ratingBarStoreHome;
    TextView textViewOpeningClosedStoreHome;
    TextView textViewOpenTimeCloseTimeStoreHome;
    TextView textViewStoreAddressHome;
    TextView textViewStoreCategoryHome;
    TextView textViewStorePriceHome;
    LinearLayout containerStoreMenuHome;
    LinearLayout containerViewAllInformationStoreHome;

    Integer StoreId;
    String StoreName;
    String StoreAddress;
    String StoreOpenTime;
    String StoreCloseTime;
    Integer StoreCategoryId;
    String StoreCategoryName;
    byte[] StoreCoverPhoto;
    Float StoreRateStar;
    Float StoreFoodMinPrice;
    Float StoreFoodMaxPrice;
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toolbarStoreHome = (Toolbar) findViewById(R.id.toolbarStoreHome);
        setSupportActionBar(toolbarStoreHome);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_home);

        StoreId = getIntent().getIntExtra("StoreId", 0);
        Email = getIntent().getStringExtra("Email");

        Cursor dataStore = MainActivity.database.GetData("SELECT * FROM Stores WHERE StoreId = " + StoreId);
        while (dataStore.moveToNext()){
            StoreName = dataStore.getString(1);
            StoreAddress = dataStore.getString(2);
            StoreOpenTime = dataStore.getString(3);
            StoreCloseTime = dataStore.getString(4);
            StoreCategoryId = dataStore.getInt(5);
            StoreCoverPhoto = dataStore.getBlob(6);
            StoreRateStar = dataStore.getFloat(7);
        }

        Cursor dataCategory = MainActivity.database.GetData("SELECT * FROM Categories WHERE CategoryId = " + StoreCategoryId);
        while (dataCategory.moveToNext()){
            StoreCategoryName = dataCategory.getString(1);
        }

        Cursor dataFoodMinMax = MainActivity.database.GetData("SELECT MIN(Price), MAX(Price) FROM Foods WHERE StoreId = " + StoreId);
        while (dataFoodMinMax.moveToNext()){
            StoreFoodMinPrice = dataFoodMinMax.getFloat(0);
            StoreFoodMaxPrice = dataFoodMinMax.getFloat(1);
        }

        toolbarStoreHome = (Toolbar) findViewById(R.id.toolbarStoreHome);
        imgBackStoreHome = (ImageButton) findViewById(R.id.imageButtonBackStoreHome);
        textViewStoreHome = (TextView) findViewById(R.id.textViewStoreHome);
        imageViewStoreHome = (ImageView) findViewById(R.id.imageViewStoreHome) ;
        textViewStoreNameHome = (TextView) findViewById(R.id.textViewStoreNameHome);
        ratingBarStoreHome = (RatingBar) findViewById(R.id.ratingBarStoreHome);
        textViewOpeningClosedStoreHome = (TextView) findViewById(R.id.textViewOpeningClosedStoreHome);
        textViewOpenTimeCloseTimeStoreHome = (TextView) findViewById(R.id.textViewOpenTimeCloseTimeStoreHome);
        textViewStoreAddressHome = (TextView) findViewById(R.id.textViewStoreAddressHome);
        textViewStoreCategoryHome = (TextView) findViewById(R.id.textViewStoreCategoryHome);
        textViewStorePriceHome = (TextView) findViewById(R.id.textViewStorePriceHome);
        containerStoreMenuHome = (LinearLayout) findViewById(R.id.containerStoreMenuHome);
        containerViewAllInformationStoreHome = (LinearLayout) findViewById(R.id.containerViewAllInformationStoreHome);

        textViewStoreHome.setText(StoreName);

        // Xử lý CoverPhoto
        if (StoreCoverPhoto != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(StoreCoverPhoto, 0, StoreCoverPhoto.length);
            imageViewStoreHome.setImageBitmap(bitmap);
        }

        textViewStoreNameHome.setText(StoreName);
        ratingBarStoreHome.setRating(StoreRateStar);
        textViewOpenTimeCloseTimeStoreHome.setText(StoreOpenTime + " - " + StoreCloseTime);
        textViewStoreAddressHome.setText(StoreAddress);
        textViewStoreCategoryHome.setText(StoreCategoryName);
        textViewStorePriceHome.setText(StoreFoodMinPrice.toString() + " - " + StoreFoodMaxPrice.toString());

        toolbarStoreHome.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.nav_Review){
                    showDialogReviewStore(StoreHomeActivity.this, StoreId, Email);
                }
                else if(item.getItemId() == R.id.nav_RatingStar) {
                    showDialogRateStarStore(StoreHomeActivity.this, StoreId, Email);
                }
                return false;
            }
        });

        imgBackStoreHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        containerStoreMenuHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreHomeActivity.this, MenuStoreHomeActivity.class);
                intent.putExtra("StoreId", StoreId);
                intent.putExtra("Email", Email);
                startActivity(intent);
            }
        });

        containerViewAllInformationStoreHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreHomeActivity.this, AllInformationStoreHome.class);
                intent.putExtra("StoreId", StoreId);
                startActivity(intent);
            }
        });
    }

    private void showDialogReviewStore(Activity activity, Integer storeId, String email) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.review_dialog_store_home);
        dialog.setTitle("Review");

        EditText editTextReviewStore = (EditText) dialog.findViewById(R.id.editTextReviewStore);
        Button buttonPostReviewStore = (Button) dialog.findViewById(R.id.buttonPostReviewStore);

        //set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.6);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        buttonPostReviewStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.database.insertReview(
                            editTextReviewStore.getText().toString().trim(),
                            storeId,
                            email
                    );
                    dialog.dismiss();
                    Toast.makeText(StoreHomeActivity.this, "Your review is posted successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
//                updateFoodList();
            }
        });
    }

    private void showDialogRateStarStore(Activity activity, Integer storeId, String email) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.ratestar_dialog_store_home);
        dialog.setTitle("Rating Star");

        RatingBar ratingBarStoreHomeDialog = (RatingBar) dialog.findViewById(R.id.ratingBarStoreHomeDialog);
        Button buttonRateStoreDialog = (Button) dialog.findViewById(R.id.buttonRateStoreDialog);

        //set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.4);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        buttonRateStoreDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.database.insertRatingStar(
                            ratingBarStoreHomeDialog.getRating(),
                            storeId,
                            email
                    );
                    dialog.dismiss();
                    Toast.makeText(StoreHomeActivity.this, "You rated successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
//                updateFoodList();
            }
        });
    }
}