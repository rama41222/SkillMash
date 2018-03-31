package cloud.viyana.skillmash.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rama41222 on 3/31/18.
 */

public class Profile extends RealmObject {

    private String name;
    private String profileUrl;
    private String token;

    @PrimaryKey
    private String id;

    public Profile() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Profile(String name, String profileUrl, String token) {
        this.name = name;
        this.profileUrl = profileUrl;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
