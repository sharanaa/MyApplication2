package com.example.student.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roger.catloadinglibrary.CatLoadingView;

public class MainActivity extends AppCompatActivity {
    EditText mEmail,mPwd;
    Button mLogin;
    TextView mNewUser;
    String email,password;
    private FirebaseAuth mAuth;
    CatLoadingView mView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView=new CatLoadingView();
        mView.setText("Loading...");
        mView.setCanceledOnTouchOutside(false);
        mLogin=(Button)findViewById(R.id.login);
        mNewUser=(TextView)findViewById(R.id.txt_newUser);
        mEmail=(EditText)findViewById(R.id.log_email);
        mPwd=(EditText)findViewById(R.id.log_pwd);
        mAuth = FirebaseAuth.getInstance();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LOGIN
                login();
                mView.show(getSupportFragmentManager(),"");


            }
        });
        mNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //REGISTER
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            //redirect to home activity
        }
    }

    private void login() {
         email=mEmail.getText().toString();
         password=mPwd.getText().toString();
         if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //logged in
                        Toast.makeText(MainActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
         }else {
             Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
         }
        // mProgress.dismiss();

    }
}
