package com.aidul23.wallpep;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {

    private String id;
    private String imageDescription;
    private String imageName;
    private String imageUrl;
    private boolean isLiked;


    public ImageModel() {
    }

    public ImageModel(String imageDescription, String imageName, String imageUrl, boolean isLiked) {
        this.imageDescription = imageDescription;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.isLiked = isLiked;
    }


    protected ImageModel(Parcel in) {
        id = in.readString();
        imageDescription = in.readString();
        imageName = in.readString();
        imageUrl = in.readString();
        isLiked = in.readByte() != 0;
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

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(imageDescription);
        parcel.writeString(imageName);
        parcel.writeString(imageUrl);
        parcel.writeByte((byte) (isLiked ? 1 : 0));
    }

    @Override
    public String toString() {
        return "ImageModel{" +
                "id='" + id + '\'' +
                ", imageDescription='" + imageDescription + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isLiked=" + isLiked +
                '}';
    }
}
