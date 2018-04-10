package cloud.viyana.skillmash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.*;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import cloud.viyana.skillmash.Models.Profile;
import cloud.viyana.skillmash.Models.User;
import cz.msebera.android.httpclient.Header;
import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {
    CallbackManager mCallbackManager;
    ProgressDialog mProgressDialog;
    public static String PACKAGE_NAME;
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String EMAIL = "email";
    private final String tag = "FACELOG";
    final String WEATHER_URL = "http://35.230.71.49:3410/api/v1";
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrintHashKey();
        setContentView(R.layout.activity_login);
        realm = Realm.getDefaultInstance();
        mCallbackManager = CallbackManager.Factory.create();
        setLoader();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE));

        Profile prof = realm.where(Profile.class).findFirst();
        if(prof != null) {
            mProgressDialog.dismiss();
            if(isLoggedIn()) {
                Intent newCityIntent = new Intent(LoginActivity.this, MainActivity.class);
                newCityIntent.putExtra("prof", prof.toString());
                startActivity(newCityIntent);
            } else {
                Toast.makeText(LoginActivity.this, "Please, Login Again", Toast.LENGTH_SHORT);

            }
        } else {
            Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT);
            mProgressDialog.dismiss();
        }


        if(isLoggedIn()) {
            Intent newCityIntent = new Intent(LoginActivity.this, MainActivity.class);
            newCityIntent.putExtra("prof", prof.toString());
            startActivity(newCityIntent);
        }

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mProgressDialog.show();
                String accessToken = "Bearer " + loginResult.getAccessToken().getToken();

                getUserProfile(accessToken);

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

    private void PrintHashKey() {
        PACKAGE_NAME = getApplicationContext().getPackageName();
        Log.d(tag, PACKAGE_NAME);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    PACKAGE_NAME,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


    private void setLoader() {
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setTitle("Logging in");
        mProgressDialog.setMessage("Retriving data");
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
                    mProgressDialog.dismiss();
                    if(isLoggedIn()) {
                        Intent newCityIntent = new Intent(LoginActivity.this, MainActivity.class);
                        newCityIntent.putExtra("prof", prof.toString());
                        startActivity(newCityIntent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Please, Login Again", Toast.LENGTH_SHORT);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT);
                    mProgressDialog.dismiss();
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                Log.d(tag,responseString);
                mProgressDialog.dismiss();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void saveToDb(final String token, final String name, final String url, final String id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Profile profile = new Profile();
                profile.setName(name);
                profile.setProfileUrl(url);
                profile.setToken(token);
                profile.setId(id);
                realm.insertOrUpdate(profile);
            }

        });
    }

    public boolean isLoggedIn() {
        try {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            return accessToken != null;
        } catch (Exception e) {
            Log.e(tag, e.getMessage());
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
