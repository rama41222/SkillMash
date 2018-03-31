package cloud.viyana.skillmash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

import cloud.viyana.skillmash.Models.Profile;
import cloud.viyana.skillmash.Models.User;
import cz.msebera.android.httpclient.Header;
import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {
    CallbackManager mCallbackManager;
    ProgressDialog mProgressDialog;

    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String EMAIL = "email";
    private final String tag = "FACELOG";
    final String WEATHER_URL = "http://35.230.71.49:3410/api/v1";
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        realm = Realm.getDefaultInstance();
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
                    saveToDb(accessToken, user.getName(), user.getAvatar(), user.getId());
                }

                Profile prof = realm.where(Profile.class).equalTo("id", user.getId()).findFirst();
                if(prof != null) {



                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                Log.d(tag,responseString);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void saveToDb(final String token, final String name, final String url, final String id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Profile profile = new Profile();
                profile.setName(name);
                profile.setProfileUrl(url);
                profile.setToken(token);
                profile.setId(id);
                bgRealm.insertOrUpdate(profile);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.v(tag,"Fu");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e(tag, error.getMessage());
                // Transaction failed and was automatically canceled.
            }
        });
    }
}
