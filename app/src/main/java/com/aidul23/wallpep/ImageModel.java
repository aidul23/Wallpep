package com.aidul23.wallpep;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {
    private String imageDescription;
    private String imageName;
    private String imageUrl;
    private Boolean isLiked;

    public ImageModel() {
    }

    public ImageModel(String imageDescription, String imageName, String imageUrl, Boolean isLiked) {
        this.imageDescription = imageDescription;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.isLiked = isLiked;
    }


    protected ImageModel(Parcel in) {
        imageDescription = in.readString();
        imageName = in.readString();
        imageUrl = in.readString();
        byte tmpIsLiked = in.readByte();
        isLiked = tmpIsLiked == 0 ? null : tmpIsLiked == 1;
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

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageDescription);
        dest.writeString(imageName);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (isLiked == null ? 0 : isLiked ? 1 : 2));
    }
}
