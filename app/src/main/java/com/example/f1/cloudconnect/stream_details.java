package com.example.f1.cloudconnect;

import java.io.InputStream;

public class stream_details {
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getLocation() {
        return location;
    }

    public String getFile_name() {
        return file_name;
    }

    InputStream inputStream;

    public void setLocation(String location) {
        this.location = location;
    }

    String location;

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    String file_name;
}
