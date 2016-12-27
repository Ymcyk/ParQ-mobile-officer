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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameLabel;
    private EditText passwordLabel;
    private EditText urlLabel;
    private Button loginButton;

    private SharedPreferences sharedPref;

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
        String url = urlLabel.getText().toString();

        if (username.isEmpty() || password.isEmpty() || url.isEmpty())
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
        urlLabel = (EditText) findViewById(R.id.url_label);
        loginButton = (Button) findViewById(R.id.login_button);

        usernameLabel.addTextChangedListener(loginTextWatcher);
        passwordLabel.addTextChangedListener(loginTextWatcher);
        urlLabel.addTextChangedListener(loginTextWatcher);

        createSharedPref();
        setURLLabel();
    }

    public void loginOnClick(View view) {
        saveURLToSharedPref();
        ParQURLs.setAuthority(urlLabel.getText().toString());

        // final String url = "http://192.168.1.20:8000/api-token-auth/";
        final String url = ParQURLs.getLoginURL();
        System.out.println("MÓJ URLLLLLLLLLLLL: " + url);

        StringRequest loginRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("WSZEDŁĘ∞!!!!!!!!!!!");
                        try {
                            //JSONObject jsonResponse = new JSONObject(response).getJSONObject("form");
                            JSONObject jsonResponse = new JSONObject(response);
                            String token = jsonResponse.getString("token");
                            System.out.println(String.format("Token: %s", token));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error ListeneRRRRRRRRRRRRRRRRRR " + error.networkResponse.statusCode);
                        if(error.networkResponse.statusCode == 400){
                            Toast toast = Toast.makeText(
                                    getApplicationContext(),
                                    getString(R.string.bad_login_or_pass),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String username = usernameLabel.getText().toString();
                String password = passwordLabel.getText().toString();
                String data = String.format("username=%s&password=%s", username, password);
                return data.getBytes();
            }
        };

        Volley.newRequestQueue(this).add(loginRequest);
    }

    private void createSharedPref() {
        Context context = this;
        sharedPref = context.getSharedPreferences(getString(R.string.sharedpref_file_key),
                Context.MODE_PRIVATE);
    }

    private void saveURLToSharedPref() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.sharedpref_url_slug),
                urlLabel.getText().toString());
        editor.apply();
    }

    private void setURLLabel() {
        String url = sharedPref.getString(getString(R.string.sharedpref_url_slug), null);
        if (url != null) {
            urlLabel.setText(url);
            ParQURLs.setAuthority(urlLabel.getText().toString());
        }
    }
}
