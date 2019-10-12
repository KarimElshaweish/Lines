package com.example.lines.Acticites.Model;

public class CustomerContract {
    String id,price,phoneNumber,place,email,supsecriptionPeriod,school,fullName;
    Boolean isactivated;
    int passngers;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getPassngers() {
        return passngers;
    }

    public void setPassngers(int passngers) {
        this.passngers = passngers;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSupsecriptionPeriod() {
        return supsecriptionPeriod;
    }

    public void setSupsecriptionPeriod(String supsecriptionPeriod) {
        this.supsecriptionPeriod = supsecriptionPeriod;
    }

    public Boolean getIsactivated() {
        return isactivated;
    }

    public void setIsactivated(Boolean isactivated) {
        this.isactivated = isactivated;
    }
}
