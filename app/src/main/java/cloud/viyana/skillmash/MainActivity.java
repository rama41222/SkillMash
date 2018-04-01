package cloud.viyana.skillmash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    final String tag = "FACELOGMAIN";
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        Log.d(tag, "main act");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isLoggedIn()) {
            Intent newCityIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(newCityIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
