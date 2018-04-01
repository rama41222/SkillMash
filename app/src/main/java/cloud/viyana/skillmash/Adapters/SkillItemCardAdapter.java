package cloud.viyana.skillmash.Adapters;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import cloud.viyana.skillmash.MainActivity;
import cloud.viyana.skillmash.Models.Skill;
import cloud.viyana.skillmash.R;

/**
 * Created by Rama41222 on 4/1/18.
 */

public class SkillItemCardAdapter extends  RecyclerView.Adapter <SkillItemsViewHolder> {
    MainActivity mMainActivity;
    List<Skill> skillsList;

    public SkillItemCardAdapter(MainActivity mainActivity, List<Skill> skillsList) {
        this.mMainActivity = mainActivity;
        this.skillsList = skillsList;
    }

    @Override
    public SkillItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mMainActivity.getBaseContext());
        View view = inflater.inflate(R.layout.skill_list_item, parent , false);

        return new SkillItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SkillItemsViewHolder holder, int position) {
        Skill object = skillsList.get(position);
        String id = object.getId();
        String title = object.getSkill();
        String rating = Integer.toString(object.ratingSummary());

        mMainActivity.isUpdate = true;
        mMainActivity.skill.setText(title);
        mMainActivity.rating.setText(rating);

        holder.skillTitle.setText(title);
        holder.skillRating.setText(rating);
        holder.skillRatingbar.setRating(object.ratingSummary());
        Log.d("FACELOG", object.getSkill());
    }

    @Override
    public int getItemCount() {
        return skillsList.size();
    }
}

class SkillItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    SkillitemCardListener mSkillitemCardListener;
    TextView skillTitle, skillRating;
    RatingBar skillRatingbar;

    public SkillItemsViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
        skillRatingbar = (RatingBar) itemView.findViewById(R.id.skill_rating_bar);
        skillTitle = (TextView) itemView.findViewById(R.id.skill_name);
        skillRating = (TextView) itemView.findViewById(R.id.skill_rating);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
       menu.setHeaderTitle("Manage Skills");
        menu.add(0,0,getAdapterPosition(),"Rate Skill");
        menu.add(0,0,getAdapterPosition(),"Rate Skill");

    }

    @Override
    public void onClick(View v) {
        mSkillitemCardListener.onClick(v, getAdapterPosition(), false);
    }
}
