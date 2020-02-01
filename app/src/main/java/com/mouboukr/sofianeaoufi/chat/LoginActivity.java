package com.mouboukr.sofianeaoufi.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText mail,pass;
    private Button login;
    private FirebaseAuth ath;
    private ProgressDialog pdiag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mail=(EditText) findViewById(R.id.email_login);
        pass=(EditText) findViewById(R.id.password_login);
        pdiag =new ProgressDialog(this);

        ath=FirebaseAuth.getInstance();

    }

    public  void Login (View v) {




        if (!(TextUtils.isEmpty(pass.getText().toString().trim())) & (!TextUtils.isEmpty(mail.getText().toString().trim()))) {
            pdiag.setMessage("log in please wait...");
            pdiag.show();
            final String em = mail.getText().toString();
            final String ps = pass.getText().toString();
            ath.signInWithEmailAndPassword(em, ps).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    pdiag.dismiss();


                    if (task.isSuccessful()) {
                        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference pas = FirebaseDatabase.getInstance().getReference().child("Users").child(user);
                        pas.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    //return;


                                } else {
                                    View view = findViewById(R.id.login_lay);


                                    final Snackbar snack = Snackbar.make(view, "please Verify your account or register", Snackbar.LENGTH_INDEFINITE)
                                            .setAction("Ok", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                }
                                            });
                                    snack.show();
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                    else{
                        View view = findViewById(R.id.login_lay);


                    final Snackbar snack = Snackbar.make(view, "please Verify your acount or register", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                    snack.show();
                    }

                }
            });

        }

        if (TextUtils.isEmpty(mail.getText().toString().trim())) {
            View view = findViewById(R.id.login_lay);

            final Snackbar snack = Snackbar.make(view, "please enter your Email Address", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
            snack.show();
        }


        if (TextUtils.isEmpty(pass.getText().toString().trim())) {
            View view = findViewById(R.id.login_lay);

            final Snackbar snack = Snackbar.make(view, "please enter your Password", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
            snack.show();
        }





    }


    public void register(View view) {
        Intent i=new Intent(this,RegisterActivity.class);
        startActivity(i);
        finish();
    }
}
