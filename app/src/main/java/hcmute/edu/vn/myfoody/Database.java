package hcmute.edu.vn.myfoody;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Truy vấn không trả kết quả: CREATE, INSERT, UPDATE, DELETE,...
    public void QueryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //Truy vấn có trả kết quả: SELECT
    public Cursor GetData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public void INSERT_USER(String Email, String Password, String Name, String Phone, String SecurityAnswer, Integer QuestionId){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Users VALUES(?, ?, ?, null, null, ?, null, 2, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, Email);
        statement.bindString(2, Password);
        statement.bindString(3, Name);
        statement.bindString(4, Phone);
        statement.bindString(5, SecurityAnswer);
        statement.bindLong(6, QuestionId);

        statement.executeInsert();
    }

    public void insertFood(String foodName, byte[] image, Float price, Integer storeId){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Foods VALUES(null, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, foodName);
        statement.bindBlob(2, image);
        statement.bindDouble(3, price);
        statement.bindLong(4, storeId);

        statement.executeInsert();
    }

    public void updateFood(String foodName, byte[] image, Float price, Integer foodId) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE Foods SET FoodName = ?, Photo = ?, Price = ? WHERE FoodId = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, foodName);
        statement.bindBlob(2, image);
        statement.bindDouble(3, price);
        statement.bindLong(4, foodId);

        statement.execute();
        database.close();
    }

    public void deleteFood(Integer foodId) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM Foods WHERE FoodId = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1, foodId);

        statement.execute();
        database.close();
    }

    public void updatePhotoUser(byte[] image, String Email) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE Users SET Avatar = ? WHERE Email = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindBlob(1, image);
        statement.bindString(2, Email);

        statement.execute();
        database.close();
    }

    public void updateCoverPhotoStore(byte[] image, Integer storeId) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE Stores SET CoverPhoto = ? WHERE StoreId = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindBlob(1, image);
        statement.bindLong(2, storeId);

        statement.execute();
        database.close();
    }

    public void updateOpenTimeStore(String openTime, Integer storeId) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE Stores SET OpenTime = ? WHERE StoreId = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, openTime);
        statement.bindLong(2, storeId);

        statement.execute();
        database.close();
    }

    public void updateCloseTimeStore(String closeTime, Integer storeId) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE Stores SET CloseTime = ? WHERE StoreId = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, closeTime);
        statement.bindLong(2, storeId);

        statement.execute();
        database.close();
    }

    public void updateNameStore(String storeName, Integer storeId) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE Stores SET StoreName = ? WHERE StoreId = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, storeName);
        statement.bindLong(2, storeId);

        statement.execute();
        database.close();
    }

    public void updateAddressStore(String storeAddress, Integer storeId) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE Stores SET Address = ? WHERE StoreId = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, storeAddress);
        statement.bindLong(2, storeId);

        statement.execute();
        database.close();
    }

    public void updateCategoryStore(Integer storeCategoryId, Integer storeId) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE Stores SET CategoryId = ? WHERE StoreId = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindLong(1, storeCategoryId);
        statement.bindLong(2, storeId);

        statement.execute();
        database.close();
    }

    public void insertReview(String review, Integer storeId, String email){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Comments VALUES(null, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, review);
        statement.bindLong(2, storeId);
        statement.bindString(3, email);

        statement.executeInsert();
    }

    public void insertRatingStar(Float rateStar, Integer storeId, String email){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO RatingStars VALUES(null, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindDouble(1, rateStar);
        statement.bindLong(2, storeId);
        statement.bindString(3, email);

        statement.executeInsert();
    }

    public void updateRatingStar(Float rateStar, Integer storeId, String email){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE RatingStars SET RateStar = ? WHERE StoreId = ? AND Email = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindDouble(1, rateStar);
        statement.bindLong(2, storeId);
        statement.bindString(3, email);

        statement.execute();
        database.close();
    }

    public byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
