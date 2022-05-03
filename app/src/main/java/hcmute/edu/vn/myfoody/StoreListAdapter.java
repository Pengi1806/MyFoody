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

import java.util.ArrayList;

public class StoreListAdapter extends BaseAdapter {

    private Context context;
    private Integer layout;
    private ArrayList<Store> storeList;

    public StoreListAdapter(Context context, Integer layout, ArrayList<Store> storeList) {
        this.context = context;
        this.layout = layout;
        this.storeList = storeList;
    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int position) {
        return storeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder{
        ImageView imageView;
        TextView txtName;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        StoreListAdapter.ViewHolder holder = new StoreListAdapter.ViewHolder();
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtName = (TextView) row.findViewById(R.id.textViewStoreNameGridView);
            holder.imageView = (ImageView) row.findViewById(R.id.imageViewStoreGridView);
            row.setTag(holder);
        } else {
            holder = (StoreListAdapter.ViewHolder) row.getTag();
        }

        Store store = storeList.get(position);
        holder.txtName.setText(store.getStoreName());


            byte[] storeImage = store.getCoverPhoto();

        if(storeImage!=null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(storeImage, 0, storeImage.length);
            holder.imageView.setImageBitmap(bitmap);
        }

            return row;

    }
}
