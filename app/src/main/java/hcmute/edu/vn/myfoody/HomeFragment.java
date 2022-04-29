package hcmute.edu.vn.myfoody;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    Spinner spinnerCategoriesHome;
    GridView gridViewStoresHome;
    View view;

    Database database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        spinnerCategoriesHome = (Spinner) view.findViewById(R.id.dropdownCategoriesHome);
        gridViewStoresHome = (GridView) view.findViewById(R.id.gridViewStoreHome);

        ArrayList<String> arrayCategories = new ArrayList<String>();
        arrayCategories.add("All");

        database = new Database(view.getContext(), "foody.sqlite", null, 1);
        Cursor dataCategories = database.GetData("SELECT * FROM Categories");
        while (dataCategories.moveToNext()){
            String CategoryName = dataCategories.getString(1);
            arrayCategories.add(CategoryName);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(view.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayCategories);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerCategoriesHome.setAdapter(arrayAdapter);

        return view;
    }
}