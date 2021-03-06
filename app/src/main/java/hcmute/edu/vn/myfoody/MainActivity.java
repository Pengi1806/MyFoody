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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
                        Toast.makeText(MainActivity.this, "Email kh??ng t???n t???i", Toast.LENGTH_SHORT).show();
                    } else {
                        while (dataUsers.moveToNext()){
                            PasswordSqlite = dataUsers.getString(1);
                        }
                        if (PasswordSqlite.equals(Password)) {
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            intent.putExtra("Email", Email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "M???t kh???u kh??ng ????ng", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Email ho???c Password ch??a nh???p!!!", Toast.LENGTH_SHORT).show();
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
        // T???o database
        database = new Database(MainActivity.this, "foody.sqlite", null, 1);

        // T???o b???ng
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
//        database.QueryData("INSERT INTO SecurityQuestions VALUES(null, 'T??n con v???t y??u th??ch?')");
//        database.QueryData("INSERT INTO SecurityQuestions VALUES(null, 'Tr?????ng c???p 1 c???a b???n t??n g???')");
//        database.QueryData("INSERT INTO SecurityQuestions VALUES(null, 'Ng?????i b???n y??u qu?? nh???t?')");
//        database.QueryData("INSERT INTO SecurityQuestions VALUES(null, 'Tr?? ch??i b???n th??ch nh???t?')");
//        database.QueryData("INSERT INTO SecurityQuestions VALUES(null, 'N??i ????? l???i k??? ni???m kh?? qu??n nh???t?')");

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
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Vua Cua', '153 B??nh Ph??, P. 11, Qu???n 6, TP. HCM', '10:00', '22:00', 1, null, 0, '1@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Pizza Hut - Tr????ng ?????nh', '61 Tr????ng ?????nh, Qu???n 3, TP. HCM', '10:00', '22:00', 2, null, 0, '2@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'B??nh m?? Chay V??nh Vi???n', '203 V??nh Vi???n, Qu???n 10, TP. HCM', '06:00', '21:00', 3, null, 0, '3@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Five Boys - Sinh T??? B??i Vi???n', '84/7 B??i Vi???n, Qu???n 1, TP. HCM', '11:00', '23:59', 4, null, 0, '4@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'L???u B?? T?? Chu???t 2', '21 C???ng Qu???nh, Qu???n 1, TP. HCM', '09:00', '22:00', 5, null, 0, '5@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Hangout Restaurant', '361A Ph???m Ng?? L??o, Qu???n 1, TP. HCM', '10:00', '21:00', 6, null, 0, '6@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'S??p Cua Th???o - H??? Th??? K???', '68/8 H??? Th??? K???, Qu???n 10, TP. HCM', '13:00', '22:00', 7, null, 0, '7@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Ph??c Long 122 L?? L???i', '122 L?? L???i, Qu???n 1, TP. HCM', '08:00', '20:00', 8, null, 0, '8@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Lush Saigon Bar', '2 L?? T??? Tr???ng, Qu???n 1, TP. HCM', '17:00', '23:59', 9, null, 0, '9@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Fox Beer Club - H??m Nghi', '11 H??m Nghi, Qu???n 1, TP. HCM', '17:00', '23:59', 10, null, 0, '10@gmail.com')");

//        database.QueryData("INSERT INTO Stores VALUES(null, 'May Restaurant & Lounge', '56 B??i Th??? Xu??n, Qu???n 1, TP. HCM', '08:00', '23:59', 1, null, 0, '11@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'El Sol - Meat & Wine', '140/2 V?? Th??? S??u, Qu???n 3, TP. HCM', '11:00', '22:00', 2, null, 0, '12@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'H???nh Dung - ???m Th???c Chay M???n', '111 ??u C??, P.14, Qu???n 11, TP. HCM', '06:00', '22:00', 3, null, 0, '13@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Chilli - L???u N?????ng T??? Ch???n', '232 V??nh Kh??nh, Qu???n 4, TP. HCM', '16:00', '23:59', 4, null, 0, '14@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'L???u D?? Tri K???', '335 L?? V??n S???, Qu???n T??n B??nh, TP. HCM', '10:30', '23:00', 5, null, 0, '15@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Nh?? H??ng - Ph??ng Tr?? ??n Nam', '52 Tr????ng ?????nh, P.7, Qu???n 3, TP. HCM', '06:30', '23:00', 6, null, 0, '16@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Qu??n 223 - B??nh Flan Th???p C???m', '223 Tr???n B??nh Tr???ng, Qu???n 5, TP. HCM', '11:30', '21:00', 7, null, 0, '17@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Paris Baguette - Cao Th???ng', '1 Cao Th???ng, P. 2, Qu???n 3, TP.HCM', '7:00', '23:00', 8, null, 0, '18@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Fang Pub', '13 H??? Xu??n H????ng, Qu???n 3, TP. HCM', '18:00', '23:59', 9, null, 0, '19@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'BiaCraft - L?? Ng?? C??t', '1 L?? Ng?? C??t, Qu???n 3, TP. HCM', '11:00', '22:30', 10, null, 0, '20@gmail.com')");
//
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Saigon Steak 123', '123 L?? L???i, Qu???n 1, TP. HCM', '10:00', '23:00', 1, null, 0, '21@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Pizza 4Ps Pizza Ki???u Nh???t', '8/15 L?? Th??nh T??n, Qu???n 1, TP. HCM', '10:00', '23:00', 2, null, 0, '22@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Vajra - Nh?? H??ng Chay', '711 L?? H???ng Phong, P. 12, Qu???n 10, TP. HCM', '07:00', '23:00', 3, null, 0, '23@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Moon Fast Food - M??n H??n', '53 Xu??n H???ng, P. 12, Qu???n T??n B??nh, TP. HCM', '11:00', '21:30', 4, null, 0, '24@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, '???? Na - Qu??n ???c', '237 Tr???n V??n ??ang, Qu???n 3, TP. HCM', '12:00', '23:59', 5, null, 0, '25@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Sumo BBQ - Buffet N?????ng & L???u', '300 L?? V??n S???, Qu???n T??n B??nh, TP. HCM', '17:00', '22:00', 6, null, 0, '26@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'B??nh Tr??ng Tr???n Ch?? Vi??n', '38 Nguy???n Th?????ng Hi???n, Qu???n 3, TP. HCM', '11:30', '19:00', 7, null, 0, '27@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Monkey In Black Cafe', '263 Tr???n Quang Kh???i, Qu???n 1, TP. HCM', '8:00', '22:00', 8, null, 0, '28@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Chill Sky Bar', '76A L?? Lai, Qu???n 1, TP. HCM', '17:30', '23:59', 9, null, 0, '29@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Poc Poc Beer Garden', '39 Ph???m Ng???c Th???ch, Qu???n 3, TP. HCM', '07:00', '23:30', 10, null, 0, '30@gmail.com')");
//
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Ng???c S????ng - Ho??ng Vi???t', '6 Ho??ng Vi???t, Qu???n T??n B??nh, TP. HCM', '10:00', '22:00', 1, null, 0, '31@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Marukame Udon - Udon & Tempura', '215 L?? T??? Tr???ng, Qu???n 1, TP. HCM', '07:00', '21:30', 2, null, 0, '32@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'C??m Chay Mani', '291/2 V?? V??n T???n, P. 5, Qu???n 3, TP. HCM', '16:00', '21:00', 3, null, 0, '33@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'G?? C?? B???p - Chuy??n C??c M??n G??', '171B Tr???n V??n ??ang, Qu???n 3, TP. HCM', '08:00', '23:00', 4, null, 0, '34@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'V?????n ???c Wongnai', '268 T?? Hi???n Th??nh, Qu???n 10, TP. HCM', '15:00', '22:30', 5, null, 0, '35@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Langfarm Buffet', '771 Tr???n H??ng ?????o, Qu???n 5, TP. HCM', '08:00', '22:00', 6, null, 0, '36@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'X??i Ch?? B??i Th??? Xu??n', '111 B??i Th??? Xu??n, Qu???n 1, TP. HCM', '06:30', '22:00', 7, null, 0, '37@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Partea - English Tearoom', '42 Nguy???n Hu???, Qu???n 1, TP. HCM', '09:00', '22:00', 8, null, 0, '38@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Acoustic - Live Music Cafe', '6E1 Ng?? Th???i Nhi???m, Qu???n 3, TP. HCM', '18:00', '23:39', 9, null, 0, '39@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Vuvuzela Beer Club', 'A43 Tr?????ng S??n, Qu???n T??n B??nh, TP. HCM', '16:00', '23:59', 10, null, 0, '40@gmail.com')");
//
//        database.QueryData("INSERT INTO Stores VALUES(null, 'The LOG Restaurant', '8 Nguy???n B???nh Khi??m, Qu???n 1, TP. HCM', '18:00', '23:30', 1, null, 0, '41@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Hanuri - Qu??n ??n H??n Qu???c', '284A Nguy???n ????nh Chi???u, P. 6, Qu???n 3, TP. HCM', '09:00', '21:30', 2, null, 0, '42@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Hum Vegetarian - Caf?? & Restaurant', '32 V?? V??n T???n, P. 6, Qu???n 3, TP. HCM', '10:00', '22:00', 3, null, 0, '43@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'B??n ?????u Homemade', '6 H???ng H??, Qu???n T??n B??nh, TP. HCM', '10:00', '22:00', 4, null, 0, '44@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'L????ng S??n Qu??n - B?? T??ng X???o', '31 L?? T??? Tr???ng, Qu???n 1, TP. HCM', '10:00', '22:00', 5, null, 0, '45@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'King BBQ - Vua N?????ng H??n Qu???c', '58 Cao Th???ng, Qu???n 3, TP. HCM', '10:00', '21:15', 6, null, 0, '46@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Thi??n Du & Sky Sushi', '1089 Tr???n H??ng ?????o, Qu???n 5, TP. HCM', '11:00', '22:00', 7, null, 0, '47@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Du Mi??n Garden Cafe', '7 Phan V??n Tr???, Qu???n G?? V???p, TP. HCM', '7:00', '23:00', 8, null, 0, '48@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'M.Bar - Majestic Hotel', '1 ?????ng Kh???i, Qu???n 1, TP. HCM', '07:00', '23:00', 9, null, 0, '49@gmail.com')");
//        database.QueryData("INSERT INTO Stores VALUES(null, 'Gammer Beer 107', '107 Pasteur, Qu???n 1, TP. HCM', '09:00', '23:59', 10, null, 0, '50@gmail.com')");

        //insert Users data
//        database.QueryData("INSERT INTO Users VALUES('1@gmail.com', '1', '1', null, null, null, null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('2@gmail.com', '2', '2', null, null, null, null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('3@gmail.com', '3', '3', null, null, null, null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('4@gmail.com', '4', '4', null, null, null, null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('5@gmail.com', '5', '5', null, null, null, null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('6@gmail.com', '6', '6', null, null, null, null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('7@gmail.com', '7', '7', null, null, null, null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('8@gmail.com', '8', '8', null, null, null, null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('9@gmail.com', '9', '9', null, null, null, null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('10@gmail.com', '10', '10', null, null, null, null, 1, 'An H???i', 2)");

//        Cursor dataUser = database.GetData("SELECT * FROM Users WHERE Email = '1@gmail.com'");
//        while (dataUser.moveToNext()){
//            sex = dataUser.getString(3);
//            address = dataUser.getString(4);
//            phone = dataUser.getString(5);
//        }
//        database.QueryData("INSERT INTO Users VALUES('11@gmail.com', '11', '11', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('12@gmail.com', '12', '12', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('13@gmail.com', '13', '13', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('14@gmail.com', '14', '14', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('15@gmail.com', '15', '15', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('16@gmail.com', '16', '16', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('17@gmail.com', '17', '17', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('18@gmail.com', '18', '18', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('19@gmail.com', '19', '19', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('20@gmail.com', '20', '20', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('21@gmail.com', '21', '21', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('22@gmail.com', '22', '22', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('23@gmail.com', '23', '23', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('24@gmail.com', '24', '24', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('25@gmail.com', '25', '25', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('26@gmail.com', '26', '26', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('27@gmail.com', '27', '27', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('28@gmail.com', '28', '28', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('29@gmail.com', '29', '29', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('30@gmail.com', '30', '30', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('31@gmail.com', '31', '31', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('32@gmail.com', '32', '32', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('33@gmail.com', '33', '33', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('34@gmail.com', '34', '34', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('35@gmail.com', '35', '35', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('36@gmail.com', '36', '36', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('37@gmail.com', '37', '37', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('38@gmail.com', '38', '38', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('39@gmail.com', '39', '39', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('40@gmail.com', '40', '40', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('41@gmail.com', '41', '41', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('42@gmail.com', '42', '42', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('43@gmail.com', '43', '43', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('44@gmail.com', '44', '44', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('45@gmail.com', '45', '45', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('46@gmail.com', '46', '46', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('47@gmail.com', '47', '47', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('48@gmail.com', '48', '48', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('49@gmail.com', '49', '49', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('50@gmail.com', '50', '50', '" + sex + "', '" + address + "', '" + phone + "', null, 1, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('51@gmail.com', '51', '51', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('52@gmail.com', '52', '52', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('53@gmail.com', '53', '53', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('54@gmail.com', '54', '54', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('55@gmail.com', '55', '55', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('56@gmail.com', '56', '56', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('57@gmail.com', '57', '57', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('58@gmail.com', '58', '58', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('59@gmail.com', '59', '59', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('60@gmail.com', '60', '60', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('61@gmail.com', '61', '61', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('62@gmail.com', '62', '62', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('63@gmail.com', '63', '63', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('64@gmail.com', '64', '64', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('65@gmail.com', '65', '65', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('66@gmail.com', '66', '66', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('67@gmail.com', '67', '67', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('68@gmail.com', '68', '68', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('69@gmail.com', '69', '69', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('70@gmail.com', '70', '70', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('71@gmail.com', '71', '71', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('72@gmail.com', '72', '72', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('73@gmail.com', '73', '73', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('74@gmail.com', '74', '74', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('75@gmail.com', '75', '75', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('76@gmail.com', '76', '76', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('77@gmail.com', '77', '77', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('78@gmail.com', '78', '78', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('79@gmail.com', '79', '79', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('80@gmail.com', '80', '80', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('81@gmail.com', '81', '81', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('82@gmail.com', '82', '82', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('83@gmail.com', '83', '83', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('84@gmail.com', '84', '84', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('85@gmail.com', '85', '85', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('86@gmail.com', '86', '86', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('87@gmail.com', '87', '87', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('88@gmail.com', '88', '88', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('89@gmail.com', '89', '89', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('90@gmail.com', '90', '90', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('91@gmail.com', '91', '91', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('92@gmail.com', '92', '92', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('93@gmail.com', '93', '93', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('94@gmail.com', '94', '94', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('95@gmail.com', '95', '95', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('96@gmail.com', '96', '96', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('97@gmail.com', '97', '97', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('98@gmail.com', '98', '98', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('99@gmail.com', '99', '99', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
//        database.QueryData("INSERT INTO Users VALUES('100@gmail.com', '100', '100', '" + sex + "', '" + address + "', '" + phone + "', null, 2, 'An H???i', 2)");
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
//        Cursor cursor = database.GetData("SELECT * FROM Stores");
//        while (cursor.moveToNext()){
//            Log.d("TAG", "CreateDatabase: " + cursor.getInt(0));
//            Log.d("TAG", "CreateDatabase: " + cursor.getString(1));
//            Log.d("TAG", "CreateDatabase: " + cursor.getString(8));
//        }
    }
}