package hcmute.edu.vn.myfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static Database database;

    TextView txtForgotPassword;
    Button btnLogin;
    TextView txtSignUp;
    EditText edtTextEmailDangNhap;
    EditText edtTextPasswordDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateDatabase();

        txtForgotPassword = (TextView) findViewById(R.id.forgotPassword);
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        txtSignUp = (TextView) findViewById(R.id.textSignUp);
        edtTextEmailDangNhap = (EditText) findViewById(R.id.editEmailDangNhap);
        edtTextPasswordDangNhap = (EditText) findViewById(R.id.editPasswordDangNhap);

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ForgotPassword1.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = edtTextEmailDangNhap.getText().toString().trim();
                String Password = edtTextPasswordDangNhap.getText().toString();
                String PasswordSqlite ="";
                if(Email.equals("") == false && Password.equals("") == false) {
                    Cursor dataUsers = database.GetData("SELECT * FROM Users WHERE Email = '" + Email + "'");
                    if(dataUsers.getCount() == 0){
                        Toast.makeText(MainActivity.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        while (dataUsers.moveToNext()){
                            PasswordSqlite = dataUsers.getString(1);
                        }
                        if (PasswordSqlite.equals(Password)) {
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            intent.putExtra("Email", Email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Email hoặc Password chưa nhập!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp1.class);
                startActivity(intent);
            }
        });
    }
    private void CreateDatabase(){
        // Tạo database
        database = new Database(MainActivity.this, "foody.sqlite", null, 1);

        // Tạo bảng
        database.QueryData("CREATE TABLE IF NOT EXISTS SecurityQuestions(" +
                "QuestionId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "QuestionContent NVARCHAR(200))");

        database.QueryData("CREATE TABLE IF NOT EXISTS Users(" +
                "Email VARCHAR(200) PRIMARY KEY, " +
                "Password VARCHAR(200), " +
                "Name NVARCHAR(200), " +
                "Sex NVARCHAR(10), " +
                "Address NVARCHAR(200), " +
                "Phone VARCHAR(15), " +
                "Avatar Blob, " +
                "Role INTEGER, " +
                "SecurityAnswer NVARCHAR(200), " +
                "QuestionId INTEGER, " +
                "FOREIGN KEY (QuestionId) REFERENCES SecurityQuestions (QuestionId))");

        database.QueryData("CREATE TABLE IF NOT EXISTS Categories(" +
                "CategoryId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CategoryName NVARCHAR(200))");

        database.QueryData("CREATE TABLE IF NOT EXISTS Stores(" +
                "StoreId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "StoreName NVARCHAR(200), " +
                "Address NVARCHAR(200), " +
                "OpenTime VARCHAR(10), " +
                "CloseTime VARCHAR(10), " +
                "CategoryId INTEGER, " +
                "CoverPhoto Blob, " +
                "StoreRateStar FLOAT, " +
                "Email VARCHAR(200), " +
                "FOREIGN KEY (Email) REFERENCES Users (Email), " +
                "FOREIGN KEY (CategoryId) REFERENCES Categories (CategoryId))");

        database.QueryData("CREATE TABLE IF NOT EXISTS Foods(" +
                "FoodId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "FoodName NVARCHAR(200), " +
                "Photo Blob, " +
                "Price FLOAT, " +
                "StoreId INTEGER, " +
                "FOREIGN KEY (StoreId) REFERENCES Stores (StoreId))");

        database.QueryData("CREATE TABLE IF NOT EXISTS Comments(" +
                "CommentId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CommentContent NVARCHAR(255), " +
                "StoreId INTEGER, " +
                "Email VARCHAR(200), " +
                "FOREIGN KEY (StoreId) REFERENCES Stores (StoreId), " +
                "FOREIGN KEY (Email) REFERENCES Users (Email))");

        database.QueryData("CREATE TABLE IF NOT EXISTS RatingStars(" +
                "RatingStarId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "RateStar FLOAT, " +
                "StoreId INTEGER, " +
                "Email VARCHAR(200), " +
                "FOREIGN KEY (StoreId) REFERENCES Stores (StoreId), " +
                "FOREIGN KEY (Email) REFERENCES Users (Email))");

        database.QueryData("CREATE TABLE IF NOT EXISTS CartItems(" +
                "FoodId INTEGER, " +
                "Email VARCHAR(200), " +
                "Quantity INTEGER, " +
                "FOREIGN KEY (FoodId) REFERENCES Foods (FoodId), " +
                "FOREIGN KEY (Email) REFERENCES Users (Email), " +
                "PRIMARY KEY (FoodId, Email))");

        database.QueryData("CREATE TABLE IF NOT EXISTS Orders(" +
                "OrderId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CreateTime DATETIME, " +
                "Total FLOAT, " +
                "Email VARCHAR(200), " +
                "StoreId INTEGER, " +
                "FOREIGN KEY (StoreId) REFERENCES Stores (StoreId), " +
                "FOREIGN KEY (Email) REFERENCES Users (Email))");

        database.QueryData("CREATE TABLE IF NOT EXISTS OrderDetails(" +
                "OrderId INTEGER, " +
                "FoodId INTEGER, " +
                "Quantity INTEGER, " +
                "UnitPrice FLOAT, " +
                "FOREIGN KEY (OrderId) REFERENCES Orders (OrderId), " +
                "FOREIGN KEY (FoodId) REFERENCES Foods (FoodId), " +
                "PRIMARY KEY (OrderId, FoodId))");

        //insert questions data
//        database.QueryData("INSERT INTO SecurityQuestions VALUES(null, 'Tên con vật yêu thích?')");
//        database.QueryData("INSERT INTO SecurityQuestions VALUES(null, 'Trường cấp 1 của bạn tên gì?')");
//        database.QueryData("INSERT INTO SecurityQuestions VALUES(null, 'Người bạn yêu quý nhất?')");
//        database.QueryData("INSERT INTO SecurityQuestions VALUES(null, 'Trò chơi bạn thích nhất?')");
//        database.QueryData("INSERT INTO SecurityQuestions VALUES(null, 'Nơi để lại kỉ niệm khó quên nhất?')");

        //insert Categories data
//        database.QueryData("INSERT INTO Categories VALUES(null, 'Luxury')");
//        database.QueryData("INSERT INTO Categories VALUES(null, 'Restaurant')");
//        database.QueryData("INSERT INTO Categories VALUES(null, 'Vegetarian')");
//        database.QueryData("INSERT INTO Categories VALUES(null, 'Bistro')");
//        database.QueryData("INSERT INTO Categories VALUES(null, 'Boozer')");
//        database.QueryData("INSERT INTO Categories VALUES(null, 'Buffet')");
//        database.QueryData("INSERT INTO Categories VALUES(null, 'Street food')");
//        database.QueryData("INSERT INTO Categories VALUES(null, 'Coffee/Dessert')");
//        database.QueryData("INSERT INTO Categories VALUES(null, 'Bar/Pub')");
//        database.QueryData("INSERT INTO Categories VALUES(null, 'Beer club')");

        //insert Stores data
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Kobe Teppanyaki - Nhà Hàng Nhật', '13A Tú Xương, P. 7, Quận 3, TP. HCM', '10:00', '22:00', 1, null, 0, '1@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Pizza Hut - Trương Định', '61 Trương Định, Quận 3, TP. HCM', '10:00', '22:00', 2, null, 0, '2@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Bánh mì Chay Vĩnh Viễn', '203 Vĩnh Viễn, Quận 10, TP. HCM', '06:00', '21:00', 3, null, 0, '3@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Five Boys - Sinh Tố Bùi Viện', '84/7 Bùi Viện, Quận 1, TP. HCM', '11:00', '23:59', 4, null, 0, '4@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Lẩu Bò Tí Chuột 2', '21 Cống Quỳnh, Quận 1, TP. HCM', '09:00', '22:00', 5, null, 0, '5@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Hangout Restaurant', '361A Phạm Ngũ Lão, Quận 1, TP. HCM', '10:00', '21:00', 6, null, 0, '6@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Súp Cua Thảo - Hồ Thị Kỷ', '68/8 Hồ Thị Kỷ, Quận 10, TP. HCM', '13:00', '22:00', 7, null, 0, '7@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Phúc Long 122 Lê Lợi', '122 Lê Lợi, Quận 1, TP. HCM', '8:00', '20:00', 8, null, 0, '8@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Lush Saigon Bar', '2 Lý Tự Trọng, Quận 1, TP. HCM', '17:00', '23:59', 9, null, 0, '9@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Fox Beer Club - Hàm Nghi', '11 Hàm Nghi, Quận 1, TP. HCM', '17:00', '23:59', 10, null, 0, '10@gmail.com')");

        //insert Users data
//        database.QueryData("INSERT INTO Users VALUES('1@gmail.com', '1', '1', null, null, null, null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('2@gmail.com', '2', '2', null, null, null, null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('3@gmail.com', '3', '3', null, null, null, null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('4@gmail.com', '4', '4', null, null, null, null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('5@gmail.com', '5', '5', null, null, null, null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('6@gmail.com', '6', '6', null, null, null, null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('7@gmail.com', '7', '7', null, null, null, null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('8@gmail.com', '8', '8', null, null, null, null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('9@gmail.com', '9', '9', null, null, null, null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('10@gmail.com', '10', '10', null, null, null, null, 1, 'An Hội', 2)");

        //delete data
//        database.QueryData("DELETE FROM SecurityQuestions WHERE QuestionId > 4");
    }
}