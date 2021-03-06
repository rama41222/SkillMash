
package cloud.viyana.skillmash.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Skill {

    @SerializedName("skill")
    @Expose
    private String skill;
    @SerializedName("rating")
    @Expose
    private List<Integer> rating = null;
    @SerializedName("_id")
    @Expose
    private String id;

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public List<Integer> getRating() {
        return rating;
    }

    public void setRating(List<Integer> rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int ratingSummary(){
        if(rating.isEmpty()) {
            return 0;
        } else {
            int sum = 0;
            int size = this.rating.size();
            for(int rate : this.rating ){
                if(rate <=5 && rate >= 1) {
                    sum += Math.abs(rate);
                }
            }
            if(sum != 0) {
                double average = sum/size;
                return  (int)Math.floor(average);
            } else {
                return 0;
            }
        }
    }

}
