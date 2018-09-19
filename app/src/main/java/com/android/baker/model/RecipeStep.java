package com.android.baker.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class RecipeStep implements Parcelable {

    private int id;
    private String shortDescription;
    private String detailedDescription;
    private String videoUrl;
    private String thumbnailUrl;
    private static final String ID = "id";
    private static final String SHORTDESCRIPTION = "shortDescription";
    private static final String DESCRIPTION = "description";
    private static final String VIDEOURL = "videoURL";
    private static final String THUMBNAILURL = "thumbnailURL";

    public RecipeStep() {
    }

    public RecipeStep(JSONObject jsonObj) throws Exception {
        this.id = jsonObj.getInt(ID);
        this.shortDescription = jsonObj.optString(SHORTDESCRIPTION);
        this.detailedDescription = jsonObj.optString(DESCRIPTION);
        this.videoUrl = jsonObj.optString(VIDEOURL);
        this.thumbnailUrl = jsonObj.optString(THUMBNAILURL);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(detailedDescription);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
    }

    private RecipeStep(Parcel parcel) {
        id = parcel.readInt();
        shortDescription = parcel.readString();
        detailedDescription = parcel.readString();
        videoUrl = parcel.readString();
        thumbnailUrl = parcel.readString();
    }

    public static final Parcelable.Creator<RecipeStep> CREATOR
            = new Parcelable.Creator<RecipeStep>() {
        public RecipeStep createFromParcel(Parcel in) {
            return new RecipeStep(in);
        }

        public RecipeStep[] newArray(int size) {
            return new RecipeStep[size];
        }
    };
}
