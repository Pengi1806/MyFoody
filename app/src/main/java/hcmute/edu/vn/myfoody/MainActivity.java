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

    String sex;
    String address;
    String phone;
    byte[] avatar;

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

//        Cursor dataUser = database.GetData("SELECT * FROM Users WHERE Email = '1@gmail.com'");
//        while (dataUser.moveToNext()){
//            sex = dataUser.getString(3);
//            address = dataUser.getString(4);
//            phone = dataUser.getString(5);
//        }
//        database.QueryData("INSERT INTO Users VALUES('11@gmail.com', '11', '11', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('12@gmail.com', '12', '12', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('13@gmail.com', '13', '13', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('14@gmail.com', '14', '14', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('15@gmail.com', '15', '15', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('16@gmail.com', '16', '16', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('17@gmail.com', '17', '17', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('18@gmail.com', '18', '18', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('19@gmail.com', '19', '19', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('20@gmail.com', '20', '20', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('21@gmail.com', '21', '21', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('22@gmail.com', '22', '22', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('23@gmail.com', '23', '23', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('24@gmail.com', '24', '24', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('25@gmail.com', '25', '25', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('26@gmail.com', '26', '26', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('27@gmail.com', '27', '27', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('28@gmail.com', '28', '28', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('29@gmail.com', '29', '29', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('30@gmail.com', '30', '30', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('31@gmail.com', '31', '31', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('32@gmail.com', '32', '32', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('33@gmail.com', '33', '33', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('34@gmail.com', '34', '34', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('35@gmail.com', '35', '35', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('36@gmail.com', '36', '36', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('37@gmail.com', '37', '37', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('38@gmail.com', '38', '38', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('39@gmail.com', '39', '39', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('40@gmail.com', '40', '40', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('41@gmail.com', '41', '41', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('42@gmail.com', '42', '42', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('43@gmail.com', '43', '43', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('44@gmail.com', '44', '44', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('45@gmail.com', '45', '45', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('46@gmail.com', '46', '46', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('47@gmail.com', '47', '47', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('48@gmail.com', '48', '48', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('49@gmail.com', '49', '49', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('50@gmail.com', '50', '50', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('51@gmail.com', '51', '51', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('52@gmail.com', '52', '52', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('53@gmail.com', '53', '53', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('54@gmail.com', '54', '54', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('55@gmail.com', '55', '55', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('56@gmail.com', '56', '56', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('57@gmail.com', '57', '57', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('58@gmail.com', '58', '58', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('59@gmail.com', '59', '59', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('60@gmail.com', '60', '60', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('61@gmail.com', '61', '61', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('62@gmail.com', '62', '62', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('63@gmail.com', '63', '63', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('64@gmail.com', '64', '64', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('65@gmail.com', '65', '65', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('66@gmail.com', '66', '66', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('67@gmail.com', '67', '67', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('68@gmail.com', '68', '68', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('69@gmail.com', '69', '69', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('70@gmail.com', '70', '70', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('71@gmail.com', '71', '71', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('72@gmail.com', '72', '72', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('73@gmail.com', '73', '73', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('74@gmail.com', '74', '74', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('75@gmail.com', '75', '75', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('76@gmail.com', '76', '76', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('77@gmail.com', '77', '77', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('78@gmail.com', '78', '78', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('79@gmail.com', '79', '79', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('80@gmail.com', '80', '80', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('81@gmail.com', '81', '81', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('82@gmail.com', '82', '82', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('83@gmail.com', '83', '83', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('84@gmail.com', '84', '84', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('85@gmail.com', '85', '85', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('86@gmail.com', '86', '86', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('87@gmail.com', '87', '87', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('88@gmail.com', '88', '88', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('89@gmail.com', '89', '89', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('90@gmail.com', '90', '90', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('91@gmail.com', '91', '91', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('92@gmail.com', '92', '92', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('93@gmail.com', '93', '93', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('94@gmail.com', '94', '94', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('95@gmail.com', '95', '95', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('96@gmail.com', '96', '96', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('97@gmail.com', '97', '97', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('98@gmail.com', '98', '98', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('99@gmail.com', '99', '99', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
//        database.QueryData("INSERT INTO Users VALUES('100@gmail.com', '100', '100', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An Hội', 2)");
        //delete data
//        database.QueryData("DELETE FROM SecurityQuestions WHERE QuestionId > 4");

//        Cursor dataUsers = database.GetData("SELECT * FROM Users");
//        while (dataUsers.moveToNext()){
//            Log.d("TAG", "CreateDatabase: " + dataUsers.getString(0));
//        }
//        Cursor dataUser = database.GetData("SELECT * FROM Users WHERE Email = '1@gmail.com'");
//        while (dataUser.moveToNext()){
//            avatar = dataUser.getBlob(6);
//        }
//        database.updatePhotoUser(avatar, "11@gmail.com");
//        database.updatePhotoUser(avatar, "12@gmail.com");
//        database.updatePhotoUser(avatar, "13@gmail.com");
//        database.updatePhotoUser(avatar, "14@gmail.com");
//        database.updatePhotoUser(avatar, "15@gmail.com");
//        database.updatePhotoUser(avatar, "16@gmail.com");
//        database.updatePhotoUser(avatar, "17@gmail.com");
//        database.updatePhotoUser(avatar, "18@gmail.com");
//        database.updatePhotoUser(avatar, "19@gmail.com");
//        database.updatePhotoUser(avatar, "20@gmail.com");
//        database.updatePhotoUser(avatar, "21@gmail.com");
//        database.updatePhotoUser(avatar, "22@gmail.com");
//        database.updatePhotoUser(avatar, "23@gmail.com");
//        database.updatePhotoUser(avatar, "24@gmail.com");
//        database.updatePhotoUser(avatar, "25@gmail.com");
//        database.updatePhotoUser(avatar, "26@gmail.com");
//        database.updatePhotoUser(avatar, "27@gmail.com");
//        database.updatePhotoUser(avatar, "28@gmail.com");
//        database.updatePhotoUser(avatar, "29@gmail.com");
//        database.updatePhotoUser(avatar, "30@gmail.com");
//        database.updatePhotoUser(avatar, "31@gmail.com");
//        database.updatePhotoUser(avatar, "32@gmail.com");
//        database.updatePhotoUser(avatar, "33@gmail.com");
//        database.updatePhotoUser(avatar, "34@gmail.com");
//        database.updatePhotoUser(avatar, "35@gmail.com");
//        database.updatePhotoUser(avatar, "36@gmail.com");
//        database.updatePhotoUser(avatar, "37@gmail.com");
//        database.updatePhotoUser(avatar, "38@gmail.com");
//        database.updatePhotoUser(avatar, "39@gmail.com");
//        database.updatePhotoUser(avatar, "40@gmail.com");
//        database.updatePhotoUser(avatar, "41@gmail.com");
//        database.updatePhotoUser(avatar, "42@gmail.com");
//        database.updatePhotoUser(avatar, "43@gmail.com");
//        database.updatePhotoUser(avatar, "44@gmail.com");
//        database.updatePhotoUser(avatar, "45@gmail.com");
//        database.updatePhotoUser(avatar, "46@gmail.com");
//        database.updatePhotoUser(avatar, "47@gmail.com");
//        database.updatePhotoUser(avatar, "48@gmail.com");
//        database.updatePhotoUser(avatar, "49@gmail.com");
//        database.updatePhotoUser(avatar, "50@gmail.com");
//        database.updatePhotoUser(avatar, "51@gmail.com");
//        database.updatePhotoUser(avatar, "52@gmail.com");
//        database.updatePhotoUser(avatar, "53@gmail.com");
//        database.updatePhotoUser(avatar, "54@gmail.com");
//        database.updatePhotoUser(avatar, "55@gmail.com");
//        database.updatePhotoUser(avatar, "56@gmail.com");
//        database.updatePhotoUser(avatar, "57@gmail.com");
//        database.updatePhotoUser(avatar, "58@gmail.com");
//        database.updatePhotoUser(avatar, "59@gmail.com");
//        database.updatePhotoUser(avatar, "60@gmail.com");
//        database.updatePhotoUser(avatar, "61@gmail.com");
//        database.updatePhotoUser(avatar, "62@gmail.com");
//        database.updatePhotoUser(avatar, "63@gmail.com");
//        database.updatePhotoUser(avatar, "64@gmail.com");
//        database.updatePhotoUser(avatar, "65@gmail.com");
//        database.updatePhotoUser(avatar, "66@gmail.com");
//        database.updatePhotoUser(avatar, "67@gmail.com");
//        database.updatePhotoUser(avatar, "68@gmail.com");
//        database.updatePhotoUser(avatar, "69@gmail.com");
//        database.updatePhotoUser(avatar, "70@gmail.com");
//        database.updatePhotoUser(avatar, "71@gmail.com");
//        database.updatePhotoUser(avatar, "72@gmail.com");
//        database.updatePhotoUser(avatar, "73@gmail.com");
//        database.updatePhotoUser(avatar, "74@gmail.com");
//        database.updatePhotoUser(avatar, "75@gmail.com");
//        database.updatePhotoUser(avatar, "76@gmail.com");
//        database.updatePhotoUser(avatar, "77@gmail.com");
//        database.updatePhotoUser(avatar, "78@gmail.com");
//        database.updatePhotoUser(avatar, "79@gmail.com");
//        database.updatePhotoUser(avatar, "80@gmail.com");
//        database.updatePhotoUser(avatar, "81@gmail.com");
//        database.updatePhotoUser(avatar, "82@gmail.com");
//        database.updatePhotoUser(avatar, "83@gmail.com");
//        database.updatePhotoUser(avatar, "84@gmail.com");
//        database.updatePhotoUser(avatar, "85@gmail.com");
//        database.updatePhotoUser(avatar, "86@gmail.com");
//        database.updatePhotoUser(avatar, "87@gmail.com");
//        database.updatePhotoUser(avatar, "88@gmail.com");
//        database.updatePhotoUser(avatar, "89@gmail.com");
//        database.updatePhotoUser(avatar, "90@gmail.com");
//        database.updatePhotoUser(avatar, "91@gmail.com");
//        database.updatePhotoUser(avatar, "92@gmail.com");
//        database.updatePhotoUser(avatar, "93@gmail.com");
//        database.updatePhotoUser(avatar, "94@gmail.com");
//        database.updatePhotoUser(avatar, "95@gmail.com");
//        database.updatePhotoUser(avatar, "96@gmail.com");
//        database.updatePhotoUser(avatar, "97@gmail.com");
//        database.updatePhotoUser(avatar, "98@gmail.com");
//        database.updatePhotoUser(avatar, "99@gmail.com");
//        database.updatePhotoUser(avatar, "100@gmail.com");

//        //insert Comments data
//        database.QueryData("INSERT INTO Comments VALUES(null, 'Good', 1, '100@gmail.com')");
//        database.QueryData("INSERT INTO Comments VALUES(null, 'Bad', 1, '99@gmail.com')");
//        database.QueryData("INSERT INTO Comments VALUES(null, 'Cool', 1, '98@gmail.com')");
//        database.QueryData("INSERT INTO Comments VALUES(null, 'Great', 1, '97@gmail.com' )");
//        database.QueryData("INSERT INTO Comments VALUES(null, 'Comfortable', 1, '96@gmail.com')");
//
//        //insert RatingStars data
//        database.QueryData("INSERT INTO RatingStars VALUES(null, 5, 1, '100@gmail.com')");
//        database.QueryData("INSERT INTO RatingStars VALUES(null, 4.5, 1, '99@gmail.com')");
//        database.QueryData("INSERT INTO RatingStars VALUES(null, 4, 1, '98@gmail.com')");
//        database.QueryData("INSERT INTO RatingStars VALUES(null, 3.5, 1, '97@gmail.com' )");
//        database.QueryData("INSERT INTO RatingStars VALUES(null, 3, 1, '96@gmail.com')");
    }
}