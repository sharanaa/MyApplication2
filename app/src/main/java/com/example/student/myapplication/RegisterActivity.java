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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    EditText mUserName,mEmail,mPwd,mAddress,mMobile;
    Button mRegister;
    String unam,email,pwd,address,mobile;
    ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //init
        mAuth=FirebaseAuth.getInstance();
        mRegister= (Button)findViewById(R.id.register) ;
        mAddress=(EditText)findViewById(R.id.reg_address);
        mMobile=(EditText)findViewById(R.id.reg_mobile);
        mPwd=(EditText)findViewById(R.id.reg_pwd);
        mEmail=(EditText)findViewById(R.id.reg_email);
        mUserName =(EditText)findViewById(R.id.reg_username);
        mProgress=new ProgressDialog(this);
        mProgress.setTitle("Loading..");
        mProgress.setMessage("Please wait..");
        mProgress.setCanceledOnTouchOutside(false);
        mDatabase=FirebaseDatabase.getInstance().getReference();
        //listener
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               unam=mUserName.getText().toString();
                email=mEmail.getText().toString();
                pwd=mPwd.getText().toString();
                mobile=mMobile.getText().toString();
                address=mAddress.getText().toString();

                if(!TextUtils.isEmpty(email)
                        &&!TextUtils.isEmpty(pwd)
                       &&!TextUtils.isEmpty(address)
                        &&!TextUtils.isEmpty(unam)
                        &&!TextUtils.isEmpty(mobile))

                {
                    mProgress.show();
                    mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(),mPwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                String currentUser=mAuth.getCurrentUser().getUid();

                                Map<String,String> map=new HashMap<>();
                                map.put("username",unam);
                                map.put("password",pwd);
                                map.put("address",address);
                                map.put("mobile",mobile);
                                map.put("email",email);

                                mDatabase.child("users").child(currentUser).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"Successfully created",Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else
                                        {
                                            Toast.makeText(RegisterActivity.this, "Failed to create", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                mProgress.dismiss();
                            }else{
                                Toast.makeText(RegisterActivity.this, "Failed to connect", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
