package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class StoreHomeActivity extends AppCompatActivity {

    Toolbar toolbarStoreHome;

    ImageButton imgBackStoreHome;
    TextView textViewStoreHome;
    ImageView imageViewStoreHome;
    TextView textViewStoreNameHome;
    RatingBar ratingBarStoreHome;
    TextView textViewNumVote;
    TextView textViewOpeningClosedStoreHome;
    TextView textViewOpenTimeCloseTimeStoreHome;
    TextView textViewStoreAddressHome;
    TextView textViewStoreCategoryHome;
    TextView textViewStorePriceHome;
    LinearLayout containerStoreMenuHome;
    LinearLayout containerViewAllInformationStoreHome;

    ListView listViewComment;
    ArrayList<Comment> commentArrayList;
    CommentListAdapter adapter;

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
    Integer numVote;

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

        Cursor dataNumVote = MainActivity.database.GetData("SELECT COUNT(*) FROM RatingStars WHERE StoreId = " + StoreId);
        while (dataNumVote.moveToNext()){
            numVote = dataNumVote.getInt(0);
        }

        toolbarStoreHome = (Toolbar) findViewById(R.id.toolbarStoreHome);
        imgBackStoreHome = (ImageButton) findViewById(R.id.imageButtonBackStoreHome);
        textViewStoreHome = (TextView) findViewById(R.id.textViewStoreHome);
        imageViewStoreHome = (ImageView) findViewById(R.id.imageViewStoreHome) ;
        textViewStoreNameHome = (TextView) findViewById(R.id.textViewStoreNameHome);
        ratingBarStoreHome = (RatingBar) findViewById(R.id.ratingBarStoreHome);
        textViewNumVote = (TextView) findViewById(R.id.textViewNumVote);
        textViewOpeningClosedStoreHome = (TextView) findViewById(R.id.textViewOpeningClosedStoreHome);
        textViewOpenTimeCloseTimeStoreHome = (TextView) findViewById(R.id.textViewOpenTimeCloseTimeStoreHome);
        textViewStoreAddressHome = (TextView) findViewById(R.id.textViewStoreAddressHome);
        textViewStoreCategoryHome = (TextView) findViewById(R.id.textViewStoreCategoryHome);
        textViewStorePriceHome = (TextView) findViewById(R.id.textViewStorePriceHome);
        containerStoreMenuHome = (LinearLayout) findViewById(R.id.containerStoreMenuHome);
        containerViewAllInformationStoreHome = (LinearLayout) findViewById(R.id.containerViewAllInformationStoreHome);
        listViewComment = (ListView) findViewById(R.id.listViewComment);

        textViewStoreHome.setText(StoreName);

        // Xử lý CoverPhoto
        if (StoreCoverPhoto != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(StoreCoverPhoto, 0, StoreCoverPhoto.length);
            imageViewStoreHome.setImageBitmap(bitmap);
        }

        textViewStoreNameHome.setText(StoreName);
        ratingBarStoreHome.setRating(StoreRateStar);
        textViewNumVote.setText(numVote.toString());
        textViewOpenTimeCloseTimeStoreHome.setText(StoreOpenTime + " - " + StoreCloseTime);
        textViewStoreAddressHome.setText(StoreAddress);
        textViewStoreCategoryHome.setText(StoreCategoryName);
        textViewStorePriceHome.setText(StoreFoodMinPrice.toString() + " - " + StoreFoodMaxPrice.toString());

        //Xử lý Time
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+07:00"));
        Date currentTime = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        String localTime = dateFormat.format(currentTime);
        if(localTime.compareTo(StoreOpenTime) >= 0 && StoreCloseTime.compareTo(localTime) >= 0){
            textViewOpeningClosedStoreHome.setText("OPENING");
            textViewOpeningClosedStoreHome.setTextColor(Color.rgb(00,186,00));
        } else {
            textViewOpeningClosedStoreHome.setText("CLOSED");
            textViewOpeningClosedStoreHome.setTextColor(Color.GRAY);
        }

        commentArrayList = new ArrayList<Comment>();
        adapter = new CommentListAdapter(StoreHomeActivity.this, R.layout.row_comment, commentArrayList);
        listViewComment.setAdapter(adapter);

        Cursor dataComment = MainActivity.database.GetData("SELECT * FROM Comments WHERE StoreId = " + StoreId);
        commentArrayList.clear();
        while (dataComment.moveToNext()){
            Integer CommentId = dataComment.getInt(0);
            String CommentContent = dataComment.getString(1);
            Integer StoreId = dataComment.getInt(2);
            String Email = dataComment.getString(3);

            commentArrayList.add(new Comment(CommentId, CommentContent, StoreId, Email));
        }
        adapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(listViewComment);

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
                updateCommentList();
            }
        });
    }

    Float RateStar;
    private void showDialogRateStarStore(Activity activity, Integer storeId, String email) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.ratestar_dialog_store_home);
        dialog.setTitle("Rating Star");


        RatingBar ratingBarStoreHomeDialog = (RatingBar) dialog.findViewById(R.id.ratingBarStoreHomeDialog);
        Button buttonRateStoreDialog = (Button) dialog.findViewById(R.id.buttonRateStoreDialog);

        Cursor dataRatingStar = MainActivity.database.GetData("SELECT * FROM RatingStars WHERE StoreId = " + storeId + " AND Email = '" + email + "'");
        if(dataRatingStar.getCount() != 0) {
            while (dataRatingStar.moveToNext()){
                RateStar = dataRatingStar.getFloat(1);
            }
            ratingBarStoreHomeDialog.setRating(RateStar);
        }

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
                    Cursor dataRatingStar = MainActivity.database.GetData("SELECT * FROM RatingStars WHERE StoreId = " + storeId + " AND Email = '" + email + "'");
                    if(dataRatingStar.getCount() == 0){
                        MainActivity.database.insertRatingStar(
                                ratingBarStoreHomeDialog.getRating(),
                                storeId,
                                email
                        );
                    } else {
                        MainActivity.database.updateRatingStar(
                                ratingBarStoreHomeDialog.getRating(),
                                storeId,
                                email
                        );
                    }
                    Cursor avgRateStarStore = MainActivity.database.GetData("SELECT avg(RateStar) FROM RatingStars WHERE StoreId = " + storeId);
                    while (avgRateStarStore.moveToNext()) {
                        MainActivity.database.QueryData("UPDATE Stores SET StoreRateStar = " + avgRateStarStore.getFloat(0) + " WHERE StoreId = " + storeId);
                    }
                    dialog.dismiss();
                    Toast.makeText(StoreHomeActivity.this, "You rated successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                updateRateStarStore();
            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void updateCommentList(){
        Cursor dataComment = MainActivity.database.GetData("SELECT * FROM Comments WHERE StoreId = " + StoreId);
        commentArrayList.clear();
        while (dataComment.moveToNext()){
            Integer CommentId = dataComment.getInt(0);
            String CommentContent = dataComment.getString(1);
            Integer StoreId = dataComment.getInt(2);
            String Email = dataComment.getString(3);

            commentArrayList.add(new Comment(CommentId, CommentContent, StoreId, Email));
        }
        adapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(listViewComment);
    }

    private void updateRateStarStore(){
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
        ratingBarStoreHome.setRating(StoreRateStar);

        Cursor dataNumVote = MainActivity.database.GetData("SELECT COUNT(*) FROM RatingStars WHERE StoreId = " + StoreId);
        while (dataNumVote.moveToNext()){
            numVote = dataNumVote.getInt(0);
        }
        textViewNumVote.setText(numVote.toString());
    }
}