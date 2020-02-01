package com.mouboukr.sofianeaoufi.chat;



public class Groups {

    private String name,email;
    private String statu;
    private String photo;
    private  String primaryKey;
 private Boolean aBoolean;

public  Groups()
{

}

    public Groups(String email, String name, String statu, String photo, String primaryKey, Boolean b) {
        this.name = name;
        this.email=email;

        this.statu = statu;
        this.photo = photo;
        this.primaryKey=primaryKey;
        this.aBoolean=b;

    }

    // getters and setters



    public String getEmail() {

        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public String getName() {
        return name;
    }

    public String getStatu() {
        return statu;
    }

    public String getPhoto() {
        return photo;
    }



//setters
    public void setPrimaryKey(String primaryKey) {this.primaryKey = primaryKey;   }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }




}
