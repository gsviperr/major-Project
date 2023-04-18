package com.example.petshopp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopp.R;
import com.example.petshopp.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    EditText et_phone_code, et_phone_number, et_password;
    Button btn_login, btn_gglogin, btn_fblogin;
    TextView btn_forgotpassword, btn_signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        et_phone_code = findViewById(R.id.et_phone_code);
        et_phone_number = findViewById(R.id.et_phone_number);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_forgotpassword = findViewById(R.id.btn_forgotpassword);
        btn_signup = findViewById(R.id.btn_signup);
        btn_gglogin = findViewById(R.id.btn_gglogin);
        btn_fblogin = findViewById(R.id.btn_fblogin);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("User");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                String phone_code = et_phone_code.getText().toString();
                String phone_number = et_phone_number.getText().toString();
                String phone = phone_code + phone_number;

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(phone).exists()) {

                            mDialog.dismiss();
                            User user = snapshot.child(phone).getValue(User.class);
                            if (user.getPassword().equals(et_password.getText().toString())) {
                                Toast.makeText(SignInActivity.this, "Sign in successfully !", Toast.LENGTH_SHORT).show();
                                gotoMainActivity();
                            }
                            else {
                                Toast.makeText(SignInActivity.this, "Sign in failed !", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            mDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "User not exist in Database !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            private void gotoMainActivity() {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


    }
}