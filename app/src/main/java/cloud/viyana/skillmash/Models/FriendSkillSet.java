package cloud.viyana.skillmash.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendSkillSet {

    @SerializedName("skills")
    @Expose

    private List<FriendSkill> skillset;

    public List<FriendSkill> getSkills() {
        return skillset;
    }

    public void setSkills(List<FriendSkillSet> skills) {
        this.skillset = skillset;
    }
}
