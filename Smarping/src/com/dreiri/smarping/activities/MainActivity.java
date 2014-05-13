package com.dreiri.smarping.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dreiri.smarping.R;
import com.dreiri.smarping.services.Authenticator;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        Authenticator.setup(this);
        if (Authenticator.isNewUser()) {
            setContentView(R.layout.activity_authenticate_register);
            Button btnCreate = (Button) findViewById(R.id.btnCreate);
            btnCreate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    createPassword();
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                    finish();
                }
            });
        } else {
            setContentView(R.layout.activity_authenticate_login);
            Button btnLogin = (Button) findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (verifyPassword()) {
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong password, please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean createPassword() {
        EditText editTextPassword = (EditText) findViewById(R.id.edtPsw);
        String password = editTextPassword.getText().toString();
        if (!password.isEmpty()) {
            Authenticator.hashAndStorePassword(password);
            return true;
        } else {
            return false;
        }
    }

    private boolean verifyPassword() {
        EditText editTextPassword = (EditText) findViewById(R.id.edtPsw);
        String password = editTextPassword.getText().toString();
        return Authenticator.isPasswordMatching(password);
    }

}
