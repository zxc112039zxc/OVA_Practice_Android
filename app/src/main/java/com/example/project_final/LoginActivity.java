package com.example.project_final;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button login, register;
    TextView message, forgotPassword;

    DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle userInfoBundle = getIntent().getExtras();
        Boolean userLoggedIn = userInfoBundle.getBoolean("loggedIn");
        message = (TextView) findViewById(R.id.message);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        mDBHelper = new DBHelper(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);

        if (userLoggedIn)
        {
            message.setText("Already logged in!");
            login.setEnabled(false);
        }
        else
        {
            message.setText("");
            login.setEnabled(true);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userLoggedIn)
                {
                    finish();
                }
                else
                {
                    String usernameTXT = username.getText().toString();
                    String passwordTXT = password.getText().toString();

                    if (mDBHelper.checkLogin(usernameTXT, passwordTXT))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("username",usernameTXT);
                        bundle.putBoolean("loggedIn", true);

                        Toast.makeText(LoginActivity.this, "Log in successfully!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtras(bundle));
                    }
                    else
                    {
                        message.setText("Wrong account or password!");
                        password.setText("");
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
                View register_layout = inflater.inflate(R.layout.register_layout,null);
                final EditText username = (EditText) register_layout.findViewById(R.id.registerUsername);
                final EditText password = (EditText) register_layout.findViewById(R.id.registerPassword);
                final EditText mail = (EditText) register_layout.findViewById(R.id.registerMail);
                AlertDialog.Builder ad = new AlertDialog.Builder(LoginActivity.this);

                ad.setCancelable(true);
                ad.setTitle("REGISTER");
                ad.setView(register_layout);
                ad.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                ad.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String usernameTXT = username.getText().toString();
                        String passwordTXT = password.getText().toString();
                        String mailTXT = mail.getText().toString();

                        Boolean checkInsertData = mDBHelper.insertUserData(usernameTXT, passwordTXT, mailTXT);
                        if (checkInsertData)
                        {
                            Toast.makeText(LoginActivity.this, "Register success!", Toast.LENGTH_LONG).show();
                            Log.d("Debug", "New Entry Inserted");
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Register fail!\nAccount or Mail already exists.", Toast.LENGTH_LONG).show();
                            Log.d("Debug", "New Entry Not Inserted");
                        }
                    }
                });
                ad.show();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}