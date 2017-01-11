package com.parq.parqofficer.connection;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parq.parqofficer.App;
import com.parq.parqofficer.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by piotr on 28.12.16.
 */

public class LoginActivityAPI {
    private LoginActivity loginActivity;

    public LoginActivityAPI(LoginActivity loginActivity){
        this.loginActivity = loginActivity;
    }

    public void login(final String username, final String password) {
        StringRequest loginRequest = new StringRequest(Request.Method.POST, App.getUrl().getLoginURL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String token = jsonResponse.getString("token");
                            System.out.println(String.format("Token: %s", token));
                            loginActivity.loginSuccess(token);
                        } catch (JSONException e) {
                            Log.d("Login", "JSON parse error");
                            e.printStackTrace();
                            loginActivity.connectionError(App.PARSE_ERROR);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse er = error.networkResponse;
                        if(er != null && er.statusCode == 400) {
                            loginActivity.loginFailure();
                            Log.i("Login", "Bad login or password");
                        } else if(er != null && er.statusCode == 403) {
                            loginActivity.connectionError(App.UNAUTHENTICATED);
                            Log.d("Login", "Bad role");
                        } else {
                            error.printStackTrace();
                            Log.d("Login", "Connection error");
                            loginActivity.connectionError(App.CONNECTION_ERROR);
                        }
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String data = String.format("username=%s&password=%s&role=officer", username, password);
                return data.getBytes();
            }
        };

        Volley.newRequestQueue(loginActivity).add(loginRequest);
    }
}


