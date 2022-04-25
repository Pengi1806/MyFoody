package hcmute.edu.vn.myfoody;

import android.app.Dialog;
import android.app.appsearch.AppSearchResult;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountFragment extends Fragment {

    TextView txtViewName;
    TextView txtThongTinCaNhan;
    TextView txtDoiMatKhau;
    TextView txtChuQuan;
    TextView txtDangXuat;

    Database database;

    String Email;
    String Name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        Email = getArguments().getString("Email");

        database = new Database(getActivity(), "foody.sqlite", null, 1);
        Cursor dataUser = database.GetData("SELECT * FROM Users WHERE Email = '" + Email + "'");
        while (dataUser.moveToNext()){
            Name = dataUser.getString(2);
        }

        txtViewName = (TextView) view.findViewById(R.id.textViewFullNameAccount);
        txtThongTinCaNhan = (TextView) view.findViewById(R.id.ThongTinCaNhan);
        txtDoiMatKhau = (TextView) view.findViewById(R.id.DoiMatKhau);
        txtChuQuan = (TextView) view.findViewById(R.id.ChuQuan);
        txtDangXuat = (TextView) view.findViewById(R.id.DangXuat);

        txtViewName.setText(Name);

        txtThongTinCaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ThongTinCaNhanActivity.class);
                intent.putExtra("Email", Email);
                startActivity(intent);
            }
        });

        txtDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                intent.putExtra("Email", Email);
                startActivity(intent);
            }
        });

        txtChuQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChuQuanActivity.class);
                startActivity(intent);
            }
        });

        txtDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn muốn đăng xuất?");
                builder.setTitle("XÁC NHẬN");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        Log.d("TAG", "onCreateView: ");
        return view;
    }

    @Override
    public void onResume() {
        Cursor dataUser = database.GetData("SELECT * FROM Users WHERE Email = '" + Email + "'");
        while (dataUser.moveToNext()){
            Name = dataUser.getString(2);
        }
        txtViewName.setText(Name);
        super.onResume();
    }
}