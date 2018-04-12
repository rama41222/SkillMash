package cloud.viyana.skillmash.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import cloud.viyana.skillmash.Models.FriendSkill;
import cloud.viyana.skillmash.Models.Skill;
import cloud.viyana.skillmash.R;
import cloud.viyana.skillmash.RateFriendsActivity;

class ListItemViewholder extends RecyclerView.ViewHolder implements  View.OnClickListener, View.OnCreateContextMenuListener{

    FriendSkillCardClickListener mFriendSkillCardClickListener;
    TextView mSkill, mRating;
    ImageView mProfImage;
    RatingBar mStarRatingBar;

    public ListItemViewholder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
        setupui(itemView);
    }

    private void setupui(View itemView) {
        mSkill = (TextView) itemView.findViewById(R.id.skill_title);
        mRating = (TextView) itemView.findViewById(R.id.skill_current_rating);
        mProfImage = (ImageView) itemView.findViewById(R.id.prof_pic);
        mStarRatingBar = (RatingBar) itemView.findViewById(R.id.skill_stars);
    }

    public void setmFriendSkillCardClickListener(FriendSkillCardClickListener mFriendSkillCardClickListener) {
        this.mFriendSkillCardClickListener = mFriendSkillCardClickListener;
    }

    @Override
    public void onClick(View view) {
        mFriendSkillCardClickListener.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), "Clear");
    }
}

public class FriendSkillCardAdapter extends RecyclerView.Adapter<ListItemViewholder>{

    RateFriendsActivity mRateFriendsActivity;
    List<Skill> mFriendSkill;

    public FriendSkillCardAdapter(RateFriendsActivity mRateFriendsActivity, List<Skill> mFriendSkill) {
        this.mRateFriendsActivity = mRateFriendsActivity;
        this.mFriendSkill = mFriendSkill;
    }

    public void update(List<Skill> skl){
        this.mFriendSkill = skl;
        notifyDataSetChanged();
    }

    @Override
    public ListItemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mRateFriendsActivity.getBaseContext());
        View view = inflater.inflate(R.layout.skill_list_item, parent , false);
        return new ListItemViewholder(view);
    }

    @Override
    public void onBindViewHolder(ListItemViewholder holder, int position) {
        Skill object = mFriendSkill.get(position);
        String id = object.getId();
        String title = object.getSkill();
       String rating = Integer.toString(3);
        Log.d("FACELOG", title);
        mRateFriendsActivity.isUpdate = true;

        holder.mSkill.setText("Vio");
//        holder.mRating.setText(rating);
//        holder.skillRatingbar.setRating(object.ratingSummary());
//        Log.d("FACELOG", object.getSkill());
    }

    @Override
    public int getItemCount() {
        return mFriendSkill.size();
    }
}
