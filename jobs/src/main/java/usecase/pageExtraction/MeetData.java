package usecase.pageExtraction;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;

@Builder
public class MeetData {
    @SerializedName("MeetID") public String athleticId;
    @SerializedName("Name") public String name;
    @SerializedName("Date") public String date;
    @SerializedName("Type") public String type;

    @Override
    public String toString() {
        // TODO use this to convert local meet date to utc (somehow i need to get the local zone)
        return "MeetData{" +
                "id='" + athleticId + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", type=" + type +
                '}';
    }
}
