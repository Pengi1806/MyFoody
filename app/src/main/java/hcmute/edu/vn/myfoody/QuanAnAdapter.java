package hcmute.edu.vn.myfoody;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class QuanAnAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<QuanAn> quanAnList;

    public QuanAnAdapter(HomeFragment context, int layout, List<QuanAn> quanAnList) {
        this.context = context;
        this.layout = layout;
        this.quanAnList = quanAnList;
    }

    @Override
    public int getCount() {
        return quanAnList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        TextView txtStoreName;
        ImageView imgHinh;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            holder.txtStoreName = (TextView) view.findViewById(R.id.textViewStoreNameHomeFragment);
            holder.imgHinh = (ImageView) view.findViewById(R.id.imageCoverPhotoHomeFragment);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        QuanAn quanAn = quanAnList.get(i);

        holder.txtStoreName.setText(quanAn.getStoreName());
        //chuyen byte ve bitmap
        byte[] hinhAnh = quanAn.getCoverPhoto();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
        holder.imgHinh.setImageBitmap(bitmap);

        return view;
    }
}
