package hcmute.edu.vn.myfoody;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentListAdapter extends BaseAdapter {
    Context context;
    Integer layout;
    ArrayList<Comment> arrayListComment;

    public CommentListAdapter(Context context, Integer layout, ArrayList<Comment> arrayListComment) {
        this.context = context;
        this.layout = layout;
        this.arrayListComment = arrayListComment;
    }

    @Override
    public int getCount() {
        return arrayListComment.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListComment.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        ImageView imgUserReview;
        TextView textViewUserNameReview, textViewReview;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        CommentListAdapter.ViewHolder holder = new CommentListAdapter.ViewHolder();
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.textViewUserNameReview = (TextView) row.findViewById(R.id.textViewUserNameReview);
            holder.textViewReview = (TextView) row.findViewById(R.id.textViewReview);
            holder.imgUserReview = (ImageView) row.findViewById(R.id.imgUserReview);
            row.setTag(holder);
        } else {
            holder = (CommentListAdapter.ViewHolder) row.getTag();
        }

        Comment comment = arrayListComment.get(position);
        String Username = null;
        byte[] UserImage = null;
        Cursor dataUser = MainActivity.database.GetData("SELECT * FROM Users WHERE Email = '" + comment.getEmail() + "'");
        while (dataUser.moveToNext()){
            Username = dataUser.getString(2);
            UserImage = dataUser.getBlob(6);
        }
        holder.textViewUserNameReview.setText(Username);
        holder.textViewReview.setText(comment.getCommentContent());

        if(UserImage != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(UserImage, 0, UserImage.length);
            holder.imgUserReview.setImageBitmap(bitmap);
        }
        return row;
    }
}
