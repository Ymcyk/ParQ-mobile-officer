package com.parq.parqofficer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parq.parqofficer.connection.LoginActivityAPI;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameLabel;
    private EditText passwordLabel;
    private Button loginButton;
    private LoginActivityAPI api;

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkLabelsForEmptyValues();
        }
    };

    private void checkLabelsForEmptyValues() {
        String username = usernameLabel.getText().toString();
        String password = passwordLabel.getText().toString();

        if (username.isEmpty() || password.isEmpty())
            loginButton.setEnabled(false);
        else
            loginButton.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameLabel = (EditText) findViewById(R.id.username_label);
        passwordLabel = (EditText) findViewById(R.id.password_label);
        loginButton = (Button) findViewById(R.id.login_button);

        usernameLabel.addTextChangedListener(loginTextWatcher);
        passwordLabel.addTextChangedListener(loginTextWatcher);

        showTypeUrlDialog();

        api = new LoginActivityAPI(this);
    }

    private void showTypeUrlDialog() {
        FragmentManager fm = getSupportFragmentManager();
        TypeUrlDialog dialog = new TypeUrlDialog();
        dialog.show(fm, "fragment_type_url");
    }

    public void loginOnClick(View view) {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
        // api.login(
        //         usernameLabel.getText().toString(),
        //         passwordLabel.getText().toString());
    }

    public void loginSucceeded() {

    }

}
