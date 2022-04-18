package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ThongTinCaNhanActivity extends AppCompatActivity {

    ImageButton imgBackThongTinCaNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan);

        imgBackThongTinCaNhan = (ImageButton) findViewById(R.id.imgBackThongTinCaNhan);

        imgBackThongTinCaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}