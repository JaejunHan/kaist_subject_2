package com.jackrutorial.test1.Data;

public class Preview {
    private String id;
    private String name;
    private String title;
    private String subtitle;
    private String content;
    private String score;

//    public Preview(){}
//
//    public Preview(String id, String name, String title, String subtitle, String content, String score){
//        this.id = id;
//        this.name = name; //nickname
//        this.title = title;
//        this.subtitle = subtitle;
//        this.content = content;
//        this.score = score;
//    }

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setSubtitle(String subtitle){
        this.subtitle = subtitle;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setScore(String score){
        this.score = score;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public String getContent()
    {
        return content;
    }

    public String getScore()
    {
        return score;
    }

}
