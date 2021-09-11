package com.aidul23.wallpep;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {
    private String imageDescription;
    private String imageName;
    private String imageUrl;

    public ImageModel() {
    }

    public ImageModel(String imageDescription, String imageName, String imageUrl) {
        this.imageDescription = imageDescription;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }

    protected ImageModel(Parcel in) {
        imageDescription = in.readString();
        imageName = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageDescription);
        parcel.writeString(imageName);
        parcel.writeString(imageUrl);
    }
}
