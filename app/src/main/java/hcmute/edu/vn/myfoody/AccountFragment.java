package hcmute.edu.vn.myfoody;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AccountFragment extends Fragment {

    TextView txtThongTinCaNhan;
    TextView txtDoiMatKhau;
    TextView txtChuQuan;
    TextView txtDangXuat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        txtThongTinCaNhan = (TextView) view.findViewById(R.id.ThongTinCaNhan);
        txtDoiMatKhau = (TextView) view.findViewById(R.id.DoiMatKhau);
        txtChuQuan = (TextView) view.findViewById(R.id.ChuQuan);
        txtDangXuat = (TextView) view.findViewById(R.id.DangXuat);


        txtThongTinCaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ThongTinCaNhanActivity.class);
                startActivity(intent);
            }
        });

        txtDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
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
                getActivity().finish();
            }
        });
        return view;

    }
}