package com.mouboukr.sofianeaoufi.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText mail, pass, pass2, ps_name, ps_phone;
    private Button registration;
    private FirebaseAuth athi;
    private ProgressDialog pdiag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mail = (EditText) findViewById(R.id.passengerEmail);
        pass = (EditText) findViewById(R.id.passengerPass1);
        pass2 = (EditText) findViewById(R.id.passengerPass2);
        ps_name = (EditText) findViewById(R.id.passenger_Name);

        pdiag = new ProgressDialog(this);
        athi = FirebaseAuth.getInstance();


    }

    public void passenger_registration(View v) {
        pdiag.setMessage("registering now please wait...");
        pdiag.show();
        final String email = mail.getText().toString();
        final String password = pass.getText().toString();
        final String password2 = pass2.getText().toString();

        final String psng_name = ps_name.getText().toString();


        if(password.equals(password2)) {
// Driver Registration
            athi.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pdiag.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Now you are registered", Toast.LENGTH_SHORT).show();
                        pdiag.setMessage("please wait ... ");
                        pdiag.show();
                        // save Driver information to database
                        String user_id = athi.getCurrentUser().getUid();
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);//.child("name");
                        database.child("email").setValue(email);
                        database.child("password").setValue(password);
                        database.child("name").setValue(psng_name);
                        finish();
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);

                    } else {
                        Toast.makeText(RegisterActivity.this, "error try again please...", Toast.LENGTH_LONG).show();

                    }


                }
            });

        }
        else {
            pdiag.dismiss();
            Toast.makeText(RegisterActivity.this, "error make sure that you enter the same password", Toast.LENGTH_LONG).show();

        }


    }






}