package hcmute.edu.vn.myfoody;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(HomeFragment context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void QueryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void INSERT_QUANAN(String StoreName, String Address, String OpenTime, String CloseTime, String Categories, byte[] CoverPhoto){
        SQLiteDatabase database = getWritableDatabase();
        //String sql = "INSERT INTO Stores VALUES(null, ?, ?, ?, ?, ?, ?)";
        String sql = "INSERT INTO Stores VALUES(null, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, StoreName);
        statement.bindString(2, Address);
        statement.bindString(3, OpenTime);
        statement.bindString(4, CloseTime);
        statement.bindString(5, Categories);
        statement.bindBlob(6, CoverPhoto);

        statement.executeInsert();
    }

    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
