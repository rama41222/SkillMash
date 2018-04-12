package cloud.viyana.skillmash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cloud.viyana.skillmash.Adapters.FriendSkillCardAdapter;
import cloud.viyana.skillmash.Adapters.SkillItemCardAdapter;
import cloud.viyana.skillmash.Models.FriendSkill;
import cloud.viyana.skillmash.Models.FriendSkillSet;
import cloud.viyana.skillmash.Models.Profile;
import cloud.viyana.skillmash.Models.Skill;
import cloud.viyana.skillmash.Models.SkillSet;
import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import io.realm.Realm;

public class RateFriendsActivity extends AppCompatActivity {

    final String tag = "FACELOGMAIN";
    final String BASE_URL = "http://35.230.71.49:3410/api/v1";
    private Realm realm;
    ProgressDialog mProgressDialog;
    FloatingActionButton addSkills;

    private List<Skill> mSFriendkillList = new ArrayList<>();
    RecyclerView mFriendSkillItem;
    RecyclerView.LayoutManager mLayoutManager;
    public RatingBar mFriendSkillRatingBar;
    private ImageView profPic;
    public TextView mFriendSkillText;
    public TextView mFriendSkillCurrentRating;
    public int initFriendRating = 0;
    public boolean isUpdate = false;

    FriendSkillCardAdapter mFriendSkillItemCardAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_friends);
        realm = Realm.getDefaultInstance();
        setupui();
        setLoader();
        setupEventListeners();
        loadData();
    }

    private void loadData() {
        if(mSFriendkillList.size() > 0) {
            mSFriendkillList.clear();
        }

        Profile prof = realm.where(Profile.class).findFirst();
        String accessToken = prof.getToken();
        String userId = prof.getId();
        getUserSkills(accessToken, userId, false);
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


    private void getUserSkills(String accessToken, String userId, final boolean update) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", accessToken);
        client.addHeader("Content-Type", "Application/json");
        final String url  = BASE_URL+"/users/"+userId+"/skills";
        client.get(url, new JsonHttpResponseHandler() {
            public void onStart() {
                super.onStart();
                mProgressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                GsonBuilder mGsonBuilder = new GsonBuilder();
                Gson gson = mGsonBuilder.create();
                SkillSet skillSet = gson.fromJson(String.valueOf(response), SkillSet.class);
                Log.d(tag,skillSet.getSkills().toString());
                mSFriendkillList.clear();
                for(Skill s : skillSet.getSkills()) {
                    mSFriendkillList.add(s);
                }
                if(update) {
                    mFriendSkillItemCardAdapter.update(mSFriendkillList);
                    mFriendSkillItemCardAdapter.notifyDataSetChanged();
                    mFriendSkillItem.refreshDrawableState();
                } else {
                    mFriendSkillItemCardAdapter = new FriendSkillCardAdapter(RateFriendsActivity.this, mSFriendkillList);
                    mFriendSkillItem.setAdapter(mFriendSkillItemCardAdapter);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                Log.d(tag,responseString);
            }

            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
            }
        });
    }

    private void setupui() {
        addSkills = (FloatingActionButton) findViewById(R.id.add_skills);
        mFriendSkillRatingBar = (RatingBar) findViewById(R.id.skill_stars);
        mFriendSkillItem = (RecyclerView) findViewById(R.id.list_friend_skills);
        mFriendSkillText = (TextView) findViewById(R.id.skill_title);
        profPic = (ImageView) findViewById(R.id.prof_pic);
        mFriendSkillCurrentRating = (TextView) findViewById(R.id.skill_current_rating);
        mLayoutManager = new LinearLayoutManager(this);
        mFriendSkillItem.setLayoutManager(mLayoutManager);
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

    private void setLoader() {
        mProgressDialog = new ProgressDialog(RateFriendsActivity.this);
        mProgressDialog.setTitle("Skilling...");
        mProgressDialog.setMessage("Adding new skill");
    }
}
