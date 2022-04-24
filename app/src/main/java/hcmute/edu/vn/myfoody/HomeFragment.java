package hcmute.edu.vn.myfoody;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    GridView gvQuanAn;
    ArrayList<QuanAn> arrayQuanAn;
    QuanAnAdapter adapter;
    public static Database database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_home,container,false);
        gvQuanAn = (GridView)rootView.findViewById(R.id.gridviewFragmentHome);
        arrayQuanAn = new ArrayList<>();

        adapter = new QuanAnAdapter(this, R.layout.dong_quan_an, arrayQuanAn);
        gvQuanAn.setAdapter(adapter);

        database = new Database(this, "QuanLy.sqlite", null, 1);

        database.QueryData("CREATE TABLE IF NOT EXISTS Stores(StoreId INTEGER PRIMARY KEY AUTOINCREMENT, StoreName VARCHAR(150), " +
                "Address VARCHAR(250), OpenTime TEXT, CloseTime TEXT, Categories VARCHAR(150), CoverPhoto BLOB, StoreRateStar FLOAT, Email VARCHAR(200) )");

        Cursor cursor = database.GetData("SELECT * FROM Stores");
        while (cursor.moveToNext()){
            arrayQuanAn.add(new QuanAn(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5),
                    cursor.getString(8),
                                cursor.getFloat(7),
                    cursor.getBlob(6)
                        ));
        }
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}