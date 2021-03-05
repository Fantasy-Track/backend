package usecase.pageExtraction;

import com.google.gson.annotations.SerializedName;

public class AthleteData {
    @SerializedName("ID") private String id;
    @SerializedName("Gender") private String gender;
    @SerializedName("Name") private String name;

    public String getGender() {
        return gender.toLowerCase().startsWith("f") ? "female" : "male";
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
