package cloud.viyana.skillmash.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendSkillSet {

    @SerializedName("skills")
    @Expose

    private List<FriendSkillSet> skillset;

    public List<FriendSkillSet> getSkills() {
        return skillset;
    }

    public void setSkills(List<FriendSkillSet> skills) {
        this.skillset = skillset;
    }
}
