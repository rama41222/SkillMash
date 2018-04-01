package cloud.viyana.skillmash.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rama41222 on 4/1/18.
 */

public class SkillSet {

    @SerializedName("skills")
    @Expose

    private List<Skill> skillset;

    public List<Skill> getSkills() {
        return skillset;
    }

    public void setSkills(List<Skill> skills) {
        this.skillset = skillset;
    }
}
