package com.example.lines.Acticites.Model;

import java.util.List;

public class Vechicel {
    String licences;
    List<String>uriList;
    String driverID;

    public Vechicel() {
    }

    public Vechicel(String licences, List<String> uriList, String driverID) {
        this.licences = licences;
        this.uriList = uriList;
        this.driverID = driverID;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getLicences() {
        return licences;
    }

    public void setLicences(String licences) {
        this.licences = licences;
    }

    public List<String> getUriList() {
        return uriList;
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }
}
