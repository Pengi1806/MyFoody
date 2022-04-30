package hcmute.edu.vn.myfoody;

import android.app.Dialog;
import android.app.appsearch.AppSearchResult;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AccountFragment extends Fragment {

    TextView txtViewName;
    TextView txtThongTinCaNhan;
    TextView txtDoiMatKhau;
    TextView txtChuQuan;
    TextView txtDangXuat;

    ImageView imgAvatar;

    String Email;
    String Name;
    int Role;
    byte[] Avatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        Email = getArguments().getString("Email");

        Cursor dataUser = MainActivity.database.GetData("SELECT * FROM Users WHERE Email = '" + Email + "'");
        while (dataUser.moveToNext()){
            Name = dataUser.getString(2);
            Avatar = dataUser.getBlob(6);
            Role = dataUser.getInt(7);
        }

        txtViewName = (TextView) view.findViewById(R.id.textViewFullNameAccount);
        txtThongTinCaNhan = (TextView) view.findViewById(R.id.ThongTinCaNhan);
        txtDoiMatKhau = (TextView) view.findViewById(R.id.DoiMatKhau);
        txtChuQuan = (TextView) view.findViewById(R.id.ChuQuan);
        txtDangXuat = (TextView) view.findViewById(R.id.DangXuat);
        imgAvatar = (ImageView) view.findViewById(R.id.imgAccount);

        txtViewName.setText(Name);

        // Xử lý Avatar
        if (Avatar != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(Avatar, 0, Avatar.length);
            imgAvatar.setImageBitmap(bitmap);
        }

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
                if (Role == 1) {
                    Intent intent = new Intent(getActivity(), ChuQuanActivity.class);
                    intent.putExtra("Email", Email);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Bạn không phải chủ quán!", Toast.LENGTH_SHORT).show();
                }
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
        return view;
    }

    @Override
    public void onResume() {
        Cursor dataUser = MainActivity.database.GetData("SELECT * FROM Users WHERE Email = '" + Email + "'");
        while (dataUser.moveToNext()){
            Name = dataUser.getString(2);
            Avatar = dataUser.getBlob(6);
        }
        txtViewName.setText(Name);
        // Xử lý Avatar
        if (Avatar != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(Avatar, 0, Avatar.length);
            imgAvatar.setImageBitmap(bitmap);
        }
        super.onResume();
    }
}