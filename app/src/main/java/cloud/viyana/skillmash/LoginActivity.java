package cloud.viyana.skillmash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.*;
import org.json.JSONObject;

import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.protocol.RequestDefaultHeaders;

public class LoginActivity extends AppCompatActivity {
    CallbackManager mCallbackManager;
    ProgressDialog mProgressDialog;

    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String EMAIL = "email";
    private final String tag = "FACELOG";
    final String WEATHER_URL = "http://35.230.71.49:3410/api/v1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE));

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mProgressDialog = new ProgressDialog(LoginActivity.this);
                mProgressDialog.setMessage("Retriving data");
                mProgressDialog.show();

                String accessToken = "Bearer " + loginResult.getAccessToken().getToken();
                System.out.println(accessToken);
                Log.d(tag, "success");
                Log.d(tag, loginResult.toString());
                Log.d(tag, "User ID: "
                        + loginResult.getAccessToken().getUserId()
                        + "\n" +
                        "Auth Token: "
                        + loginResult.getAccessToken().getToken());

                getUserProfile(accessToken);
                mProgressDialog.dismiss();
            }

            @Override
            public void onCancel() {
                Log.d(tag, "Cancelled");

            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(tag, "Error");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserProfile(String accessToken) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", accessToken);
        client.addHeader("Content-Type", "Application/json");
        final String url  = WEATHER_URL+"/users/auth/facebook";
        client.post(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                GsonBuilder mGsonBuilder = new GsonBuilder();
                Gson gson = mGsonBuilder.create();
                User user = gson.fromJson(String.valueOf(response), User.class);
                String accessToken = "";
                if (headers != null) {
                    for (Header value : headers) {
                        if (value.getName().equalsIgnoreCase("token")) {
                            accessToken = value.getValue();
                        }
                    }
                }

                if(accessToken != null && user  !=null) {

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                Log.d(tag,responseString);
            }
        });
    }
}
