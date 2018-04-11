package cloud.viyana.skillmash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RatingBar;
import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BlackholeHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import cloud.viyana.skillmash.Adapters.SkillItemCardAdapter;
import cloud.viyana.skillmash.Models.Profile;
import cloud.viyana.skillmash.Models.Skill;
import cloud.viyana.skillmash.Models.SkillSet;
import cloud.viyana.skillmash.Models.User;
import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.json.JSONObject;

import io.realm.Realm;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    final String tag = "FACELOGMAIN";
    final String BASE_URL = "http://35.230.71.49:3410/api/v1";
    private Realm realm;
    ProgressDialog mProgressDialog;
    private List<Skill> mSkillList = new ArrayList<>();
    RecyclerView mSkillItem;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fab;
    FloatingActionButton nextPage;
    public RatingBar mRatingBar;
    SpotsDialog mMainAlertBox;

    public MaterialEditText mSkillText;
    public int initRating = 0;
    public boolean isUpdate = false;

    SkillItemCardAdapter mSkillItemCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        setupUi();
        loadData();
        setLoader();
    }

    private void loadData() {

        if(mSkillList.size() > 0) {
            mSkillList.clear();
        }

        Profile prof = realm.where(Profile.class).findFirst();
        String accessToken = prof.getToken();
        String userId = prof.getId();
        getUserSkills(accessToken, userId, false);
    }

    private void setLoader() {
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setTitle("Skilling...");
        mProgressDialog.setMessage("Adding new skill");
    }

    private void setupUi() {

        mMainAlertBox = new SpotsDialog(this);
        mSkillText = (MaterialEditText) findViewById(R.id.title);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mRatingBar = (RatingBar) findViewById(R.id.skill_rating_bar);
        nextPage = (FloatingActionButton) findViewById(R.id.rate_others);
        mSkillItem = (RecyclerView) findViewById(R.id.list_skills);
        mLayoutManager = new LinearLayoutManager(this);
        mSkillItem.setLayoutManager(mLayoutManager);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile prof = realm.where(Profile.class).findFirst();
                String accessToken = prof.getToken();
                String userId = prof.getId();
                String newSkill =  mSkillText.getText().toString();
                if(newSkill == null || newSkill == "") {
                    Toast.makeText(MainActivity.this, "Skill can\'t be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(tag, newSkill);
                postUserSkills(accessToken,newSkill,userId);

            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newCityIntent = new Intent(MainActivity.this, RateFriendsActivity.class);
                startActivity(newCityIntent);
            }
        });
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

    private void getUserSkills(String accessToken, String userId, final boolean update) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", accessToken);
        client.addHeader("Content-Type", "Application/json");
        final String url  = BASE_URL+"/users/"+userId+"/skills";
        client.get(url, new JsonHttpResponseHandler() {
            public void onStart() {
                super.onStart();
                mMainAlertBox.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                GsonBuilder mGsonBuilder = new GsonBuilder();
                Gson gson = mGsonBuilder.create();
                SkillSet skillSet = gson.fromJson(String.valueOf(response), SkillSet.class);
                Log.d(tag,skillSet.getSkills().toString());
                mSkillList.clear();
                for(Skill s : skillSet.getSkills()) {
                    Log.d(tag, s.getSkill());
                    mSkillList.add(s);
                }
                if(update) {

                    mSkillItemCardAdapter.update(mSkillList);
                    mSkillItemCardAdapter.notifyDataSetChanged();
                    mSkillItem.refreshDrawableState();
                } else {
                    mSkillItemCardAdapter = new SkillItemCardAdapter(MainActivity.this, mSkillList);
                    mSkillItem.setAdapter(mSkillItemCardAdapter);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                Log.d(tag,responseString);
            }

            public void onFinish() {
                super.onFinish();
                mMainAlertBox.dismiss();
            }
        });
    }

    private void postUserSkills(final String accessToken, String skill, final String userId) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", accessToken);
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.put("skill", skill);
        final String url  = BASE_URL+"/users/"+userId+"/skills";
        client.post(url,params, new BlackholeHttpResponseHandler() {

            public void onStart() {
                super.onStart();
                mMainAlertBox.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                getUserSkills(accessToken, userId, true);
                Toast.makeText(MainActivity.this, "New Skill, You are more competent than before", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                mMainAlertBox.dismiss();

                Log.d(tag,responseBody.toString());
            }

            public void onFinish() {
                super.onFinish();
            }
        });
    }
}
