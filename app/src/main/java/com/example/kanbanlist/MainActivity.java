package com.example.kanbanlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.allyants.boardview.BoardView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText signinemail;
    private EditText signinpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        signinemail=findViewById(R.id.email_id);
        signinpass=findViewById(R.id.password);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            finish();
            startActivity(new Intent(this,HomeActivity.class));
        }
    }

    public void signIn(View view){
        String email=signinemail.getText().toString();
        String password=signinpass.getText().toString();
        if (email.equals("") || password.equals("")){
            Toast.makeText(this,"Please enter credentials",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            finish();
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        return;
    }

    public void mainToSignIn(View view){
        startActivity(new Intent(this,SignUpActivity.class));
    }

}
