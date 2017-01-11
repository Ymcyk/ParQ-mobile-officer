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
import android.widget.Toast;

import com.parq.parqofficer.connection.LoginActivityAPI;
import com.parq.parqofficer.connection.ParQURLConstructor;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameLabel;
    private EditText passwordLabel;
    private Button loginButton;
    private LoginActivityAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setViews();
        setApp();

        showTypeUrlDialog();

        api = new LoginActivityAPI(this);
    }

    private void setViews() {
        usernameLabel = (EditText) findViewById(R.id.username_label);
        passwordLabel = (EditText) findViewById(R.id.password_label);
        loginButton = (Button) findViewById(R.id.login_button);

        usernameLabel.addTextChangedListener(loginTextWatcher);
        passwordLabel.addTextChangedListener(loginTextWatcher);
    }

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

    private void setApp(){
        SharedPreferences sharedPref = this.getSharedPreferences(
                this.getString(R.string.sharedpref_file_key),
                Context.MODE_PRIVATE);
        String authority = sharedPref.getString(this.getString(R.string.sharedpref_url_slug), null);
        ParQURLConstructor url = new ParQURLConstructor(authority, getApplicationContext());
        App.setSharedPref(sharedPref);
        App.setUrl(url);
    }

    private void showTypeUrlDialog() {
        FragmentManager fm = getSupportFragmentManager();
        TypeUrlDialog dialog = new TypeUrlDialog();
        dialog.show(fm, "fragment_type_url");
    }

    public void loginOnClick(View view) {
        api.login(
                usernameLabel.getText().toString(),
                passwordLabel.getText().toString()
        );
    }

    public void loginSuccess(String token) {
        App.setToken(token);
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }

    public void loginFailure() {
        Toast.makeText(this, "Bad login or password", Toast.LENGTH_LONG).show();
    }

    public void connectionError(int errorCode) {
        switch (errorCode){
            case App.PARSE_ERROR:
                Toast.makeText(this, "Parse error", Toast.LENGTH_LONG).show();
                break;
            case App.CONNECTION_ERROR:
                Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show();
                break;
            case App.UNAUTHENTICATED:
                Toast.makeText(this, "Only officers can login", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
