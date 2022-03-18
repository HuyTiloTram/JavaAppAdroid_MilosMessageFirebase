package com.l19cntt1b.milos.Models;

public class Users {
    String profilepic, userName, phone, password, userId, lastMessage, about, sex, birthday, gallerypic;

    public Users(String profilepic, String userName, String phone, String password, String userId, String lastMessage, String about, String sex, String birthday, String gallerypic) {
        this.profilepic = profilepic;
        this.userName = userName;
        this.phone = phone;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.about = about;
        this.sex = sex;
        this.birthday = birthday;
        this.gallerypic = gallerypic;
    }

    // Signup Constructor
    public Users(String userName, String phone, String password) {
      this.userName = userName;
        this.phone = phone;
        this.password = password;
    }
    public Users(){}

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGallerypic() {
        return gallerypic;
    }

    public void setGallerypic(String gallerypic) {
        this.gallerypic = gallerypic;
    }
}
