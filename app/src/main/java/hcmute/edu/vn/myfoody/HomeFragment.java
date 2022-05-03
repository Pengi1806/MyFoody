package hcmute.edu.vn.myfoody;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    Spinner spinnerCategoriesHome;
    GridView gridViewStoresHome;
    View view;
    ArrayList<Store> storeArrayList;
    StoreListAdapter adapter;
    ImageButton ImageButtonSearchHomeFragment;
    EditText EditTextSearchHomeFragment;


    Database database;

    Integer CategoryIdFilter;

    String Email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        Email = getArguments().getString("Email");

        spinnerCategoriesHome = (Spinner) view.findViewById(R.id.dropdownCategoriesHome);
        gridViewStoresHome = (GridView) view.findViewById(R.id.gridViewStoreHome);
        storeArrayList = new ArrayList<Store>();
        adapter = new StoreListAdapter(getActivity(), R.layout.store_items, storeArrayList);
        gridViewStoresHome.setAdapter(adapter);
        ImageButtonSearchHomeFragment = (ImageButton) view.findViewById(R.id.ImageButtonSearchHomeFragment);
        EditTextSearchHomeFragment = (EditText) view.findViewById(R.id.EditTextSearchHomeFragment);

        database = new Database(view.getContext(), "foody.sqlite", null, 1);

        ImageButtonSearchHomeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String SearchCategory = spinnerCategoriesHome.getSelectedItem().toString().trim();
                String SearchInput = EditTextSearchHomeFragment.getText().toString().trim();
                Cursor dataStoreFilterBySearchCategory;
                if(SearchCategory == "All")
                {
                    dataStoreFilterBySearchCategory = database.GetData("SELECT * FROM Stores WHERE StoreName LIKE '%"+SearchInput+"%' " +
                            "OR Address LIKE '%"+SearchInput+"%'");
                } else {
                    Cursor dataSearchCategory = database.GetData("SELECT * FROM Categories WHERE CategoryName = '" + SearchCategory + "'");
                    while (dataSearchCategory.moveToNext()){
                        CategoryIdFilter = dataSearchCategory.getInt(0);
                    }
                    dataStoreFilterBySearchCategory = database.GetData("SELECT * FROM Stores WHERE (StoreName LIKE '%"+SearchInput+"%' " +
                            "OR Address LIKE '%"+SearchInput+"%') AND CategoryId = " + CategoryIdFilter);
                }

                storeArrayList.clear();
                while (dataStoreFilterBySearchCategory.moveToNext()){
                    Integer StoreId = dataStoreFilterBySearchCategory.getInt(0);
                    String StoreName = dataStoreFilterBySearchCategory.getString(1);
                    String Address = dataStoreFilterBySearchCategory.getString(2);
                    String OpenTime = dataStoreFilterBySearchCategory.getString(3);
                    String CloseTime = dataStoreFilterBySearchCategory.getString(4);
                    Integer CategoryId = dataStoreFilterBySearchCategory.getInt(5);
                    byte[] CoverPhoto = dataStoreFilterBySearchCategory.getBlob(6);
                    Float StoreRateStar = dataStoreFilterBySearchCategory.getFloat(7);
                    String Email = dataStoreFilterBySearchCategory.getString(8);

                    storeArrayList.add(new Store(StoreId, StoreName, Address, OpenTime, CloseTime, CategoryId, CoverPhoto, StoreRateStar, Email));
                }
                adapter.notifyDataSetChanged();
            }
        });


        Cursor dataStore = database.GetData("SELECT * FROM Stores");
        storeArrayList.clear();
        while (dataStore.moveToNext()){
            Integer StoreId = dataStore.getInt(0);
            String StoreName = dataStore.getString(1);
            String Address = dataStore.getString(2);
            String OpenTime = dataStore.getString(3);
            String CloseTime = dataStore.getString(4);
            Integer CategoryId = dataStore.getInt(5);
            byte[] CoverPhoto = dataStore.getBlob(6);
            Float StoreRateStar = dataStore.getFloat(7);
            String Email = dataStore.getString(8);

            storeArrayList.add(new Store(StoreId, StoreName, Address, OpenTime, CloseTime, CategoryId, CoverPhoto, StoreRateStar, Email));
        }
        adapter.notifyDataSetChanged();

        ArrayList<String> arrayCategories = new ArrayList<String>();
        arrayCategories.add("All");


        Cursor dataCategories = database.GetData("SELECT * FROM Categories");
        while (dataCategories.moveToNext()){
            String CategoryName = dataCategories.getString(1);
            arrayCategories.add(CategoryName);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(view.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayCategories);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerCategoriesHome.setAdapter(arrayAdapter);

        spinnerCategoriesHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EditTextSearchHomeFragment.setText("");
                String Category = spinnerCategoriesHome.getItemAtPosition(i).toString().trim();
                Cursor dataStoreFilterByCategory;
                if(Category == "All")
                {
                    dataStoreFilterByCategory = database.GetData("SELECT * FROM Stores");

                } else {
                    Cursor dataSelectedCategory = database.GetData("SELECT * FROM Categories WHERE CategoryName = '" + Category + "'");
                    while (dataSelectedCategory.moveToNext()){
                        CategoryIdFilter = dataSelectedCategory.getInt(0);
                    }
                    dataStoreFilterByCategory = database.GetData("SELECT * FROM Stores WHERE CategoryId = " + CategoryIdFilter);
                }

                storeArrayList.clear();

                while (dataStoreFilterByCategory.moveToNext()){
                    Integer StoreId = dataStoreFilterByCategory.getInt(0);
                    String StoreName = dataStoreFilterByCategory.getString(1);
                    String Address = dataStoreFilterByCategory.getString(2);
                    String OpenTime = dataStoreFilterByCategory.getString(3);
                    String CloseTime = dataStoreFilterByCategory.getString(4);
                    Integer CategoryId = dataStoreFilterByCategory.getInt(5);
                    byte[] CoverPhoto = dataStoreFilterByCategory.getBlob(6);
                    Float StoreRateStar = dataStoreFilterByCategory.getFloat(7);
                    String Email = dataStoreFilterByCategory.getString(8);

                    storeArrayList.add(new Store(StoreId, StoreName, Address, OpenTime, CloseTime, CategoryId, CoverPhoto, StoreRateStar, Email));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gridViewStoresHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), StoreHomeActivity.class);
                intent.putExtra("StoreId", storeArrayList.get(position).getStoreId());
                intent.putExtra("Email", Email);
                startActivity(intent);
            }
        });
        return view;
    }
}