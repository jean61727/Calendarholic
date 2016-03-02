package com.example.jeanlee.calendar;

/**
 * Created by jeanlee on 2015/1/20.
 */
import android.graphics.Bitmap;

public class PetItem {
    private Bitmap image;
    private String title;

    public PetItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}