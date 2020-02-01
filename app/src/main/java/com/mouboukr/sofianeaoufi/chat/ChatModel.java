package com.mouboukr.sofianeaoufi.chat;

/**
 * Created by Acer on 2018-04-27.
 */

public class ChatModel {

    private String Text,id;
   private String photo;


    public ChatModel() {
    }

    public ChatModel(String Text,String photo,String id) {
        this.Text = Text;
        this.photo = photo;
        this.id=id;


    }

    // getters
    public String getPhoto() {
        return photo;
    }


    public String getId() {
        return id;
    }

    public String getText() {
        return Text;
    }






    //setters


    public void setText(String Text) {
        this.Text = Text;
    }


  public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setId(String id) {
        this.id = id;
    }
}