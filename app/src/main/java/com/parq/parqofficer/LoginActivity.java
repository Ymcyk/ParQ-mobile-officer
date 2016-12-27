package com.parq.parqofficer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameLabel;
    private EditText passwordLabel;
    private Button loginButton;

    private SharedPreferences sharedPref;

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            checkLabelsForEmptyValues();
        }
    };

    private void checkLabelsForEmptyValues() {
        String username = usernameLabel.getText().toString();
        String password = passwordLabel.getText().toString();

        if(username.isEmpty() || password.isEmpty())
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

        createSharedPreferences();
        setURLFromSharedPreferences();
    }

    public void loginOnClick(View view) {
        saveURLToSharedPreferences();
    }

    private void createSharedPreferences() {
        Context context = this;
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
    }

    private void saveURLToSharedPreferences() {
        EditText urlLabel = (EditText) findViewById(R.id.url_label);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.url_shared_preferences_slug),
                urlLabel.getText().toString());
        editor.apply();
    }

    private void setURLFromSharedPreferences() {
        String url = sharedPref.getString(getString(R.string.url_shared_preferences_slug), null);
        if(url != null) {
            EditText urlLabel = (EditText) findViewById(R.id.url_label);
            urlLabel.setText(url);
        }
    }
}
