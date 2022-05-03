package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class AllInformationStoreHome extends AppCompatActivity {

    ImageButton imgBackOwnerInformation;
    TextView textViewNameOwnerInformation, textViewEmailOwnerInformation, textViewPhoneOwnerInformation;

    Integer StoreId;
    String Email, Name, Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_information_store_home);

        StoreId = getIntent().getIntExtra("StoreId", 0);

        Cursor dataStore = MainActivity.database.GetData("SELECT * FROM Stores WHERE StoreId = " + StoreId);
        while (dataStore.moveToNext()){
            Email = dataStore.getString(8);
        }

        Cursor dataOwner = MainActivity.database.GetData("SELECT * FROM Users WHERE Email = '" + Email + "'");
        while (dataOwner.moveToNext()){
            Email = dataOwner.getString(0);
            Name = dataOwner.getString(2);
            Phone = dataOwner.getString(5);
        }

        imgBackOwnerInformation = (ImageButton) findViewById(R.id.imgBackOwnerInformation);
        textViewNameOwnerInformation = (TextView) findViewById(R.id.textViewNameOwnerInformation);
        textViewEmailOwnerInformation = (TextView) findViewById(R.id.textViewEmailOwnerInformation);
        textViewPhoneOwnerInformation = (TextView) findViewById(R.id.textViewPhoneOwnerInformation);

        textViewNameOwnerInformation.setText(Name);
        textViewEmailOwnerInformation.setText(Email);
        textViewPhoneOwnerInformation.setText(Phone);

        imgBackOwnerInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}