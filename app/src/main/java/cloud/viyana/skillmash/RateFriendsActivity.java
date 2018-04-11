package cloud.viyana.skillmash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import cloud.viyana.skillmash.Adapters.SkillItemCardAdapter;
import cloud.viyana.skillmash.Models.FriendSkill;
import cloud.viyana.skillmash.Models.Skill;
import dmax.dialog.SpotsDialog;
import io.realm.Realm;

public class RateFriendsActivity extends AppCompatActivity {

    final String tag = "FACELOGMAIN";
    final String BASE_URL = "http://35.230.71.49:3410/api/v1";
    private Realm realm;
    ProgressDialog mProgressDialog;
    FloatingActionButton addSkills;

    private List<FriendSkill> mSFriendkillList = new ArrayList<>();
    RecyclerView mFriendSkillItem;
    RecyclerView.LayoutManager mLayoutManager;
    public RatingBar mFriendSkillRatingBar;
    private ImageView profPic;
    public TextView mFriendSkillText;
    public TextView mFriendSkillCurrentRating;
    public int initFriendRating = 0;
    public boolean isUpdate = false;

    SkillItemCardAdapter mSkillItemCardAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_friends);
        realm = Realm.getDefaultInstance();
        setupui();
        setLoader();
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
        mFriendSkillRatingBar = (RatingBar) findViewById(R.id.skill_stars);
        mFriendSkillItem = (RecyclerView) findViewById(R.id.list_friend_skills);
        mFriendSkillText = (TextView) findViewById(R.id.skill_title);
        profPic = (ImageView) findViewById(R.id.prof_pic);
        mFriendSkillCurrentRating = (TextView) findViewById(R.id.skill_current_rating);
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
