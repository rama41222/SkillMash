package cloud.viyana.skillmash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.AccessToken;

import io.realm.Realm;

public class RateFriendsActivity extends AppCompatActivity {

    final String tag = "FACELOGMAIN";
    final String BASE_URL = "http://35.230.71.49:3410/api/v1";
    private Realm realm;
    ProgressDialog mProgressDialog;
    FloatingActionButton addSkills;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_friends);
        realm = Realm.getDefaultInstance();
        setupui();
        setupEventListeners();
        loadSkills();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isLoggedIn()) {
            Intent newCityIntent = new Intent(RateFriendsActivity.this, LoginActivity.class);
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


    private void loadSkills() {
    }

    private void setupui() {
        addSkills = (FloatingActionButton) findViewById(R.id.add_skills);
    }

    private void setupEventListeners() {
        addSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newCityIntent = new Intent(RateFriendsActivity.this, MainActivity.class);
                startActivity(newCityIntent);
            }
        });
    }
}
