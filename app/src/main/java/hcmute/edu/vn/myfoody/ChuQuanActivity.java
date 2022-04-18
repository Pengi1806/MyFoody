package hcmute.edu.vn.myfoody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ChuQuanActivity extends AppCompatActivity {

    Toolbar toolbarChuQuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toolbarChuQuan = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarChuQuan);
//        toolbarChuQuan.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if(item.getItemId() == R.id.nav_ThongTinQuanAn){
//                    Toast.makeText(ChuQuanActivity.this, "Thông tin quán ăn", Toast.LENGTH_SHORT).show();
//                }
//                else if(item.getItemId() == R.id.nav_Exit) {
//                    finish();
//                }
//                return false;
//            }
//        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chu_quan);
    }
}