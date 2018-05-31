package com.fanny.ghmf;

/**
 * Created by Fanny on 17/10/13.
 */

public class UserEvent {
    public UserEvent(String name, String psw, String photo, String phone) {
        this.name = name;
        this.psw = psw;
        this.photo = photo;
        this.phone = phone;
    }

    private String name;
    private String psw;
    private String photo;
    private String phone;

    public void setName(String name) {
        this.name = name;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPsw() {
        return psw;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPhone() {
        return phone;
    }
}
