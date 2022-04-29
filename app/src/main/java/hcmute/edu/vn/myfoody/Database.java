package hcmute.edu.vn.myfoody;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

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



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
