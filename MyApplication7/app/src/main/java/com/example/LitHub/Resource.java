package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Resource implements Parcelable {
    private String title;
    private String type; // "book", "pyq", or "note"
    private String pdfUrl;
    private String subject;

    public Resource() {
        // Default constructor for Firebase
    }

    public Resource(String title, String type, String pdfUrl, String subject) {
        this.title = title;
        this.type = type;
        this.pdfUrl = pdfUrl;
        this.subject = subject;
    }

    protected Resource(Parcel in) {
        title = in.readString();
        type = in.readString();
        pdfUrl = in.readString();
        subject = in.readString();
    }

    public static final Creator<Resource> CREATOR = new Creator<Resource>() {
        @Override
        public Resource createFromParcel(Parcel in) {
            return new Resource(in);
        }

        @Override
        public Resource[] newArray(int size) {
            return new Resource[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(pdfUrl);
        dest.writeString(subject);
    }
} 