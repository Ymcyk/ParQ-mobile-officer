package com.parq.parqofficer.connection;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parq.parqofficer.LoginActivity;
import com.parq.parqofficer.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by piotr on 28.12.16.
 */

public class LoginActivityAPI {
    private LoginActivity loginActivity;
    private SharedPreferences sharedPref;
    private ParQURLs url;
    private String token;

    public LoginActivityAPI(LoginActivity loginActivity){
        this.loginActivity = loginActivity;
        this.sharedPref = loginActivity.getSharedPreferences(
                this.loginActivity.getString(R.string.sharedpref_file_key),
                Context.MODE_PRIVATE);
        this.url = new ParQURLs(getURLFromSharedPref(), this.loginActivity);
    }

    public String getToken() {
        return this.token;
    }

    public boolean tryLoginWithToken() {
        return false;
    }

    public void login(final String username, final String password) {
        StringRequest loginRequest = new StringRequest(Request.Method.POST, url.getLoginURL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //JSONObject jsonResponse = new JSONObject(response).getJSONObject("form");
                            JSONObject jsonResponse = new JSONObject(response);
                            token = jsonResponse.getString("token");
                            System.out.println(String.format("Token: %s", token));
                            loginActivity.loginSucceeded();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse != null && error.networkResponse.statusCode == 400){
                            System.out.println("Bad username or password");
                            Toast toast = Toast.makeText(
                                    loginActivity,
                                    loginActivity.getString(R.string.bad_login_or_pass),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            System.out.println("Connection error");
                            Toast toast = Toast.makeText(
                                    loginActivity,
                                    loginActivity.getString(R.string.connection_error),
                                    Toast.LENGTH_LONG);
                            toast.show();
                            error.printStackTrace();
                        }
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String data = String.format("username=%s&password=%s", username, password);
                return data.getBytes();
            }
        };

        Volley.newRequestQueue(loginActivity).add(loginRequest);
    }

    private String getURLFromSharedPref() {
        return sharedPref.getString(loginActivity.getString(R.string.sharedpref_url_slug), null);
    }
}


