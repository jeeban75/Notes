package com.example.notes;

public class Note {


    public Note(){

    }
    private String Subtitle;

    public Note(String Subtitle, String Text, String Title) {
        this.Subtitle = Subtitle;
        this.Text = Text;
        this.Title = Title;
    }

    private String Text;
    private String Title;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubtitle() {
        return Subtitle;
    }

    public void setSubtitle(String subtitle) {
        Subtitle = subtitle;
    }

    public String getext() {
        return Text;
    }

    public void setext(String text) {
        Text = text;
    }






}
