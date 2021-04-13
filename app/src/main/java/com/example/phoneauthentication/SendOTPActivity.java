package com.example.phoneauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {

    private EditText edtFname, edtUsername, edtNumber;
    private Button btnRegister;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_o_t_p);

        edtFname = findViewById(R.id.fname);
        edtUsername = findViewById(R.id.username);
        edtNumber = findViewById(R.id.phone);
        btnRegister = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.progressBar);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = edtFname.getText().toString().trim();
                String username = edtUsername.getText().toString().trim();
                String number = edtNumber.getText().toString().trim();

                if (fname.isEmpty()){
                    edtFname.setError("Full name is required");
                    edtFname.requestFocus();
                    return;
                }
                if (username.isEmpty()){
                    edtUsername.setError("username is required");
                    edtUsername.requestFocus();
                    return;
                }
                if (number.isEmpty()){
                    edtNumber.setError("username is required");
                    edtNumber.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        number,
                        60,
                        TimeUnit.SECONDS,
                        SendOTPActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(SendOTPActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);

                                Intent intent = new Intent(SendOTPActivity.this,VerifyOTPActivity.class);
                                intent.putExtra("mobile",number);
                                intent.putExtra("verificationId",verificationId);
                                startActivity(intent);
                            }
                        });

            }
        });
    }
}