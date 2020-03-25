package com.example.kanbanlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText signupemail;
    private EditText passone;
    private EditText passtwo;
    private EditText username;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        signupemail=findViewById(R.id.sign_up_email);
        passone=findViewById(R.id.sign_up_pass_one);
        passtwo=findViewById(R.id.sign_up_pass_two);
        username=findViewById(R.id.sign_up_username);
    }

    public void signupToMain(View view){
        startActivity(new Intent(this,MainActivity.class));
    }

    public void signup(View view){
        String email=signupemail.getText().toString();
        String password=passone.getText().toString();
        String password2=passtwo.getText().toString();
        final String userName=username.getText().toString();
        if (email.equals("") || password.equals("") || password2.equals("") || userName.equals("")){
            Toast.makeText(this,"Please enter credentials",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(password2)){
            Toast.makeText(this,"Passwords don't match.",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserClass temp=new UserClass(userName,new ArrayList<String>());
                            myRef.child("Users").child(user.getUid()).setValue(temp);
                            finish();
                            startActivity(new Intent(SignUpActivity.this,HomeActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
