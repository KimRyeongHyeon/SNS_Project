package com.myandroid.sns_project;

public class MemberInfo {
    private String name;
    private String phoneNumber;
    private String birthday;
    private String address;
    private String photoUrl;

    public MemberInfo(String name, String phoneNumber, String birthday, String address, String photoUrl) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.address = address;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
