package com.example.petshopp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopp.R;
import com.example.petshopp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OtpTextView;


public class SignUpActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    EditText et_full_name, et_phone_code, et_phone_number, et_password, et_re_password;
    Button btn_signup, btn_verify;
    TextView btn_forggotpassword, btn_resend;
    String fullname, phone, password, verificationID;
    ScrollView signup;
    LinearLayout otp;
    OtpTextView otp_view;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        et_full_name = findViewById(R.id.et_full_name);
        et_phone_code = findViewById(R.id.et_phone_code);
        et_phone_number = findViewById(R.id.et_phone_number);
        et_password = findViewById(R.id.et_password);
        et_re_password = findViewById(R.id.et_re_password);
        btn_signup = findViewById(R.id.btn_signup);
        btn_forggotpassword = findViewById(R.id.btn_forgotpassword);
        btn_verify = findViewById(R.id.btn_verify);
        btn_resend = findViewById(R.id.btn_resend);
        signup = findViewById(R.id.signup);
        otp = findViewById(R.id.otp);
        otp_view = findViewById(R.id.otp_view);
        progressBar = findViewById(R.id.progressBar);




        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = et_full_name.getText().toString();
                password = et_password.getText().toString();
                String re_password = et_re_password.getText().toString();
                String phone_code = et_phone_code.getText().toString();
                String phone_number = et_phone_number.getText().toString();
                phone = phone_code + phone_number;

                if(fullname.isEmpty()){
                    et_full_name.setError("Vui long nhap ten");
                    et_full_name.requestFocus();

                } else if (password.isEmpty()) {
                    et_password.setError("Vui long nhap mat khau");
                    et_password.requestFocus();
                }else if (phone_number.isEmpty()) {
                    et_phone_number.setError("Vui long nhap so dien thoai");
                    et_phone_number.requestFocus();
                }else if (re_password.isEmpty()) {
                    et_re_password.setError("Vui long nhap lai mat khau");
                    et_re_password.requestFocus();
                }else if(!password.equals(re_password)){
                    et_re_password.setError("Sai ");
                    et_re_password.requestFocus();
                }
                else{
                    sendVerificationCode(phone);
                }
                progressBar.setVisibility(View.VISIBLE);
                btn_signup.setVisibility(View.INVISIBLE);
            }
        });
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otps = otp_view.getOTP();
                if(otps.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Nhap ma otp",Toast.LENGTH_SHORT).show();
                }else{
                    verifyCode(otps);

                }
            }
        });

    }


    private void sendVerificationCode(String phone) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        String code = phoneAuthCredential.getSmsCode();
                        if(code != null){
                            otp_view.setOTP(code);
                            verifyCode(code);
                        }
                        progressBar.setVisibility(View.GONE);
                        btn_signup.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        btn_signup.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressBar.setVisibility(View.GONE);
                        btn_signup.setVisibility(View.VISIBLE);
                        signup.setVisibility(View.GONE);
                        otp.setVisibility(View.VISIBLE);

                        verificationID = s;

                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private void verifyCode(String otps) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, otps);
        signInWithCredential(credential);
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Khởi tạo đối tượng User mới
                            User user = new User(fullname, password);

                            // Lưu thông tin người dùng vào Realtime Database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference userRef = database.getReference("User");
                            userRef.child(phone).setValue(user);
                            finish();


                        }else{
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }



}