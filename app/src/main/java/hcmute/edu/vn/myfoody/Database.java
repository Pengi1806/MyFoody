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
    //Truy vấn có trả kết quả: SELECT
    public Cursor GetData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }
    
//    public void insertStore(String name, String address, String openTime, String closeTime, Integer categoryId, byte[] coverPhoto, Float rateStar, )

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
