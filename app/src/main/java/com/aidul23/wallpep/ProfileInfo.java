package com.aidul23.wallpep;

import android.net.Uri;

public class ProfileInfo {
    String name;
    String email;
    Uri image;

    public ProfileInfo(String name, String email,Uri image) {
        this.name = name;
        this.email = email;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Uri getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
