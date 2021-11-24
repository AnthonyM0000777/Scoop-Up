package com.cite.newscoopup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText nameEdt,userNameEdt, passwordEdt, confirmPwdEdt;
    private TextView loginTV;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_register);

        nameEdt = findViewById (R.id.idEdtName);
        userNameEdt = findViewById(R.id.idEdtUserName);
        passwordEdt = findViewById(R.id.idEdtPwd);
        loadingPB = findViewById(R.id.idPBLoading);
        confirmPwdEdt = findViewById(R.id.idedtCnfPwd);
        loginTV = findViewById(R.id.idTVLogin);
        registerBtn = findViewById(R.id.idBtnRegister);
        mAuth = FirebaseAuth.getInstance();

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);

                String fullName = nameEdt.getText ().toString ();
                String userName = userNameEdt.getText().toString();
                String pwd = passwordEdt.getText().toString();
                String cnfPwd = confirmPwdEdt.getText().toString();

                if (!pwd.equals(cnfPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please check both having same password", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cnfPwd) && TextUtils.isEmpty (fullName)) {

                    Toast.makeText(RegisterActivity.this, "Please enter your credentials", Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.createUserWithEmailAndPassword(userName, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult> () {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loadingPB.setVisibility(View.GONE);
                             /*   Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();*/
                                User user = new User (fullName, userName, pwd);
                                FirebaseDatabase.getInstance ().getReference ("Users")
                                        .child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ())
                                        .setValue (user).addOnCompleteListener (new OnCompleteListener<Void> () {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText (RegisterActivity.this, "User Registered", Toast.LENGTH_SHORT).show ();
                                        Intent i = new Intent (RegisterActivity.this, LoginActivity.class);
                                        startActivity (i);
                                        finish ();
                                    }
                                });
                            } else {

                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "Fail to register user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}