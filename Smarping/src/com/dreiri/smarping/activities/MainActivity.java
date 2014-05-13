package com.dreiri.smarping.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
            EditText edtPsw = (EditText) findViewById(R.id.edtPsw);
            btnCreate.setEnabled(false);
            btnCreate.setOnClickListener(new SignupOnClickListener());
            edtPsw.addTextChangedListener(new PasswordTextWatcher(btnCreate));
        } else {
            setContentView(R.layout.activity_authenticate_login);
            Button btnLogin = (Button) findViewById(R.id.btnLogin);
            EditText edtPsw = (EditText) findViewById(R.id.edtPsw);
            btnLogin.setEnabled(false);
            btnLogin.setOnClickListener(new LoginOnClickListener());
            edtPsw.addTextChangedListener(new PasswordTextWatcher(btnLogin));
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

    private class SignupOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            createPassword();
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }

    private class LoginOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (verifyPassword()) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                finish();
            } else {
                String toast_wrong_password = getResources().getString(R.string.toast_wrong_password);
                Toast.makeText(MainActivity.this, toast_wrong_password, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class PasswordTextWatcher implements TextWatcher {

        private Button button;

        public PasswordTextWatcher(Button button) {
            this.button = button;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (button.isEnabled() && s.length() < 3) {
                button.setEnabled(false);
            } else if (!button.isEnabled() && s.length() >= 3) {
                button.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

}
