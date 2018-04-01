package cloud.viyana.skillmash.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

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

    @Override
    public SkillItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SkillItemsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
        setupUi(itemView);
    }

    private void setupUi(View itemView) {
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
