package com.example.notes;

import java.io.Serializable;

public class Note implements Serializable {


    public Note(){

    }
    private String Subtitle;

    public String getWeb_Link() {
        return Web_Link;
    }

    public void setWeb_Link(String web_Link) {
        Web_Link = web_Link;
    }

    private String Web_Link;

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    private String DateTime;
    public Note(String Title, String Subtitle, String DateTime) {
        this.Subtitle = Subtitle;
        this.DateTime = DateTime;
        this.Title = Title;
    }

    private String Text;
    private String Title;

    public  String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public  String getSubtitle() {
        return Subtitle;
    }

    public void setSubtitle(String subtitle) {
        Subtitle = subtitle;
    }

    public  String gettext() {
        return Text;
    }

    public void settext(String text) {
        Text = text;
    }






}
